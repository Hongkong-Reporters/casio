package com.report.casio.config.parser;

import com.report.casio.common.Constants;
import com.report.casio.common.exception.ContextException;
import com.report.casio.config.ConsumerConfig;
import com.report.casio.config.ProviderConfig;
import com.report.casio.config.RegistryConfig;
import com.report.casio.config.ServiceConfig;
import com.report.casio.config.context.RpcContextFactory;
import com.report.casio.test.YamlParserTest;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class YamlConfigParser implements ConfigParser {

    @Override
    public void parse(String path) throws ContextException {
        Yaml yaml = new Yaml();
        InputStream inputStream = YamlParserTest.class
                .getClassLoader()
                .getResourceAsStream(path);
        Map<String, Object> obj = yaml.load(inputStream);

        int providerPort = Constants.DEFAULT_PROVIDER_PORT;
        int providerTimeout = Constants.DEFAULT_TIMEOUT;
        String registryAddress = Constants.DEFAULT_REGISTRY_ADDRESS;
        int registryPort = Constants.DEFAULT_REGISTRY_PORT;

        try {
            if (obj.containsKey(Constants.PROJECT)) {
                Map<String, Object> configs = (Map<String, Object>) obj.get(Constants.PROJECT);
                Map<String, Object> providerConfig = (Map<String, Object>) configs.get(Constants.PROVIDER);
                if (providerConfig != null) {
                    if (providerConfig.containsKey(Constants.PORT)) {
                        providerPort = Integer.parseInt(providerConfig.get(Constants.PORT).toString());
                    }
                }
                Map<String, Object> registryConfig = (Map<String, Object>) configs.get(Constants.REGISTRY);
                if (registryConfig != null) {
                    if (registryConfig.containsKey(Constants.ADDRESS)) {
                        registryAddress = registryConfig.get(Constants.ADDRESS).toString();
                    }
                    if (registryConfig.containsKey(Constants.PORT)) {
                        registryPort = Integer.parseInt(registryConfig.get(Constants.PORT).toString());
                    }
                    if (registryConfig.containsKey(Constants.TIMEOUT)) {
                        providerTimeout = Integer.parseInt(registryConfig.get(Constants.TIMEOUT).toString());
                    }
                }
            }

            ProviderConfig providerConfig = new ProviderConfig(providerPort, providerTimeout);
            ConsumerConfig consumerConfig = new ConsumerConfig();
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setAddress(registryAddress);
            registryConfig.setPort(registryPort);
            List<RegistryConfig> registryConfigs = new ArrayList<>();
            registryConfigs.add(registryConfig);
            List<ServiceConfig> serviceConfigs = new ArrayList<>();
            RpcContextFactory.createConfigContext(providerConfig, consumerConfig, registryConfigs, serviceConfigs);
        } catch (Exception e) {
            throw new ContextException(path + " parser error", e.getCause());
        }
    }

}
