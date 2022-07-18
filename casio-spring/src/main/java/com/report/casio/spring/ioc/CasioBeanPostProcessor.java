package com.report.casio.spring.ioc;

import com.report.casio.common.annotation.Reference;
import com.report.casio.common.annotation.Register;
import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.ServiceConfig;
import com.report.casio.registry.ServiceRegistry;
import com.report.casio.rpc.proxy.RpcProxyUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
/*
 * spring 后置处理器
 * 处理Bean对象在实例化和依赖注入后，在显示调用初始化方法前的自定义逻辑
 */
@Component
public class CasioBeanPostProcessor implements BeanPostProcessor {

    @Override
    @SneakyThrows
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(Register.class)) {
            ServiceRegistry serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getDefaultExtension();
            ServiceConfig serviceConfig = ServiceConfig.builder()
                    .serviceName(clazz)
                    .ref(clazz.getName())
                    .version(clazz.getAnnotation(Register.class).version())
                    .build();
            serviceRegistry.register(serviceConfig);
        }
        return bean;
    }

    @Override
    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Reference.class)) {
                Object proxy = RpcProxyUtil.getProxy(clazz);
                field.setAccessible(true);
                field.set(bean, proxy);
            }
        }
        return bean;
    }

}
