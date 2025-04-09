package com.lijs.chatai.common.web.config.i18n;

import com.lijs.chatai.common.base.enums.ServiceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 国际化基础配置，设置默认 Locale 为简体中文
 * <p>
 * 自定义 i18n 配置排除掉当前类的自动配置
 * <pre>
 * {@literal @}SpringBootApplication(exclude = {
 * BaseI18nWebConfig.class
 * })
 * </pre>
 */
@Configuration
@AutoConfigureBefore(MessageSourceAutoConfiguration.class)
public class BaseI18nWebConfig implements WebMvcConfigurer {

    public static final String PLATFORM_BASE_I18N_NAME = "i18n/messages";

    private static final Logger logger = LoggerFactory.getLogger(BaseI18nWebConfig.class);

    @Autowired
    private Environment environment;

    public static String[] MESSAGES_PRO_FILES = new String[]{
            "i18n/messages",
            "i18n/messages_authentications",
            "i18n/messages_protocols",
            "i18n/messages_system",
            "i18n/messages_user",
            "i18n/messages_res",
    };

    @Bean(name = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
    public ResourceBundleMessageSource messageSource() {
        List<String> profiles = new ArrayList<>();
        profiles.add(PLATFORM_BASE_I18N_NAME);
        logger.info("加载i18n资源文件:{}", PLATFORM_BASE_I18N_NAME);
        for (ServiceEnum service : ServiceEnum.values()) {
            if (environment.containsProperty(service.getPropertyKey())) {
                logger.info("加载i18n资源文件:{}", "i18n/messages_" + service.getCode());
                profiles.add("i18n/messages_" + service.getCode());
            }
        }
        MESSAGES_PRO_FILES = profiles.toArray(new String[profiles.size()]);
        // 默认简体中文
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames(MESSAGES_PRO_FILES);
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return source;
    }

    @Bean
    public I18nService i18nService() {
        return new I18nService(messageSource());
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);
        factoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
        return factoryBean;
    }

    @Bean(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        cookieLocaleResolver.setDefaultTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        cookieLocaleResolver.setRejectInvalidCookies(false);
        cookieLocaleResolver.setCookieHttpOnly(true);
        cookieLocaleResolver.setCookieSecure(true);
        return cookieLocaleResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setIgnoreInvalidLocale(true);
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Override
    public Validator getValidator() {
        return localValidatorFactoryBean(messageSource());
    }

}
