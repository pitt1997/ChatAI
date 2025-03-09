package com.lijs.nex.common.mybatis.config;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;

/**
 * 负责配置多个数据库连接，能够根据特定的注解（@DS）动态切换当前使用的数据源。具体来说，它通过面向切面编程（AOP）来实现这个功能。
 */
@Configuration
@ConditionalOnBean(DataSourceRouting.class)
public class DataSourceRoutingConfig {

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor(BeanFactory beanFactory) {
        DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
        DsSessionProcessor sessionProcessor = new DsSessionProcessor();
        DsSpelExpressionProcessor dsSpelExpressionProcessor = new DsSpelExpressionProcessor();
        dsSpelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(dsSpelExpressionProcessor);
        return headerProcessor;
    }

    @Bean
    public Advisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor(true, dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor, DS.class);
        // aop切面顺序，默认小于ignore的切面顺序
        advisor.setOrder(2);
        return advisor;
    }
}
