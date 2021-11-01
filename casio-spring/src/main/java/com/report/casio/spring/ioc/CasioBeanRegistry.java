package com.report.casio.spring.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CasioBeanRegistry implements BeanDefinitionRegistryPostProcessor {
    private static final String BASE_PACKAGE = "com.report.casio";

    @Override
    // 在标准初始化之后修改应用程序上下文的内部 bean 定义注册表。
    // 所有常规 bean 定义都将被加载，但尚未实例化任何 bean。
    // 这允许在下一个后处理阶段开始之前添加更多的 bean 定义。
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        CasioClassPathScanner scanner = new CasioClassPathScanner(registry);
        scanner.registerFilters();
        // todo: 自定义 @ScanPackage 注解
        Set<BeanDefinitionHolder> definitionHolders = scanner.doScan(BASE_PACKAGE);
        for (BeanDefinitionHolder definitionHolder : definitionHolders) {
            registry.registerBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}