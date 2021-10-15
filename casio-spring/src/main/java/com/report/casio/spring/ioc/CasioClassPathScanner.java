package com.report.casio.spring.ioc;

import com.report.casio.common.annotation.Register;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/*
 * 自定义扫描类
 */
public class CasioClassPathScanner extends ClassPathBeanDefinitionScanner {

    public CasioClassPathScanner(BeanDefinitionRegistry registry) {
        // 不适应Spring默认拦截器
        super(registry, false);
    }
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        // 设置为单例
        for (BeanDefinitionHolder holder : holders) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) holder.getBeanDefinition();
            beanDefinition.setScope("singleton");
        }
        return holders;
    }

    public void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(Register.class));
    }

}
