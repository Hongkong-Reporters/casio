package com.report.casio.boostrap;

import com.report.casio.common.Constants;
import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.ServiceConfig;
import com.report.casio.config.context.RpcConfigContext;
import com.report.casio.config.parser.ConfigParser;
import com.report.casio.registry.cache.ServiceCacheFactory;
import com.report.casio.rpc.proxy.RpcProxyUtil;
import com.report.casio.timer.WheelTimerJob;
import lombok.SneakyThrows;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Bootstraps implements Bootstrap {
    private ProviderBootstrap providerBootstrap;
    private ConsumerBootstrap consumerBootstrap;
    private WheelTimerJob wheelTimerJob;
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

    @SneakyThrows
    public void start() {

        // todo：type判断哪种读取方式
        ConfigParser configParser = ExtensionLoader.getExtensionLoader(ConfigParser.class).getDefaultExtension();
        RpcConfigContext configContext = configParser.parse(Constants.DEFAULT_CONFIG_PATH);

        providerBootstrap = new ProviderBootstrap(configContext.getProviderConfig());
        service.execute(() -> providerBootstrap.start());

        consumerBootstrap = new ConsumerBootstrap(configContext.getConsumerConfig());
        service.execute(() -> consumerBootstrap.start());

        this.wheelTimerJob = new WheelTimerJob(consumerBootstrap.getNettyClient());
        this.wheelTimerJob.execute();

        ServiceCacheFactory cacheFactory = ExtensionLoader.getExtensionLoader(ServiceCacheFactory.class).getDefaultExtension();
        cacheFactory.createCache(configContext.getConsumerConfig().getCacheSize());

        RpcProxyUtil.createProxy(consumerBootstrap.getNettyClient());
    }

    @Override
    public void close() {
        this.providerBootstrap.close();
        this.consumerBootstrap.close();

        this.wheelTimerJob.close();
    }

    public void registerService(Class<?> clazz) {
        this.registerService(clazz, "1.0.0");
    }

    @SneakyThrows
    public void registerService(Class<?> clazz, String version) {
        ServiceConfig serviceConfig = ServiceConfig.builder()
                .serviceName(clazz)
                .ref(clazz.getName())
                .version(version)
                .build();
        this.providerBootstrap.registerService(serviceConfig);
    }
}
