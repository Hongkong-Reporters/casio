package com.report.casio.common.extension;

import com.report.casio.common.annotation.SPI;
import com.report.casio.common.exception.ExtensionException;
import com.report.casio.common.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader<T> {
    private static final String SERVICES_PATH = "META-INF/services/";
    private static final String CASIO_PATH = "META-INF/casio/";
    private static final String NAME_SEPARATOR = "=";

    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER_MAP = new ConcurrentHashMap<>();
    // key: name, value: instance   (name=xxx.xxx.xxx.class)
    private static final Map<Class<?>, Object> EXTENSION_INSTANCE_MAP = new ConcurrentHashMap<>();
    // 与EXTENSION_INSTANCE_MAP存放内容相同，多套了一层Holder，保证线程安全
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> cacheClasses = new Holder<>();

    private final Class<?> type;
    private String defaultName;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    private static boolean withSPIAnnotation(Class<?> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) throws ExtensionException {
        if (type == null) {
            throw new ExtensionException("extension type is null");
        }
        if (!type.isInterface()) {
            throw new ExtensionException("extension type is not interface");
        }
        if (!withSPIAnnotation(type)) {
            throw new ExtensionException("extension type" + type + "without @SPI");
        }
        if (!EXTENSION_LOADER_MAP.containsKey(type)) {
            EXTENSION_LOADER_MAP.putIfAbsent(type, new ExtensionLoader<>(type));
        }
        return (ExtensionLoader<T>) EXTENSION_LOADER_MAP.get(type);
    }

    public T getDefaultExtension() throws ExtensionException {
        getExtensionClasses();
        return getExtension(defaultName);
    }

    @SuppressWarnings("unchecked")
    public T getExtension(String name) throws ExtensionException {
        if (StringUtil.isBlank(name)) {
            throw new ExtensionException("extension name is null");
        }
        if ("true".equals(name)) {
            // dubbo中getDefaultExtension()
            throw new ExtensionException("extension name is true");
        }
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    @SuppressWarnings("unchecked")
    public T createExtension(String name) throws ExtensionException {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new ExtensionException("name: " + name + " has no class");
        }
        try {
            Object instance = EXTENSION_INSTANCE_MAP.get(clazz);
            if (instance == null) {
                EXTENSION_INSTANCE_MAP.putIfAbsent(clazz, clazz.newInstance());
                instance = EXTENSION_INSTANCE_MAP.get(clazz);
            }
            return (T) instance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Class<?>> getExtensionClasses() throws ExtensionException {
        Map<String, Class<?>> cacheClassesMap = cacheClasses.get();
        if (cacheClassesMap == null) {
            synchronized (cacheClasses) {
                cacheClassesMap = cacheClasses.get();
                if (cacheClassesMap == null) {
                    cacheClassesMap = loadExtensionClasses();
                    cacheClasses.set(cacheClassesMap);
                }
            }
        }
        return cacheClasses.get();
    }

    private Map<String, Class<?>> loadExtensionClasses() throws ExtensionException {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi != null) {
            String value = spi.value();
            if (StringUtil.isNotBlank(value)) {
                String[] names = value.split(NAME_SEPARATOR);
                if (names.length == 1) {
                    defaultName = names[0];
                } else {
                    throw new ExtensionException("type：" + type.getName() + " @SPI value is error");
                }
            }
        }
        Map<String, Class<?>> extensionClasses = new HashMap<>();
        loadDirectory(extensionClasses, CASIO_PATH);
        loadDirectory(extensionClasses, SERVICES_PATH);
        return extensionClasses;
    }

    // 加载 resource/META-INF指定文件夹下的文件信息
    private void loadDirectory(Map<String, Class<?>> extensionClasses, String path) {
        String fileName = path + type.getName();
        ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
        Enumeration<URL> resources;
        try {
            if (classLoader == null) {
                resources = ClassLoader.getSystemResources(fileName);
            } else {
                resources = classLoader.getResources(fileName);
            }
            if (resources != null) {
                while (resources.hasMoreElements()) {
                    URL url = resources.nextElement();
                    loadResource(extensionClasses, url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadResource(Map<String, Class<?>> extensionClasses, URL url) {
        try (
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] ss = line.split(NAME_SEPARATOR);
                if (ss.length == 2) {
                    String name = ss[0];
                    String className = ss[1];
                    //
                    Class<?> clazz = Class.forName(className);
                    extensionClasses.putIfAbsent(name, clazz);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
