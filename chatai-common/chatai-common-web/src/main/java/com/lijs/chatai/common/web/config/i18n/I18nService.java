package com.lijs.chatai.common.web.config.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author ljs
 * @date 2024-09-30
 * @description 国际化业务类
 */
public class I18nService {

    private final MessageSource messageSource;

    public I18nService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

    public String getMessage(String key, Object singleArg) {
        String byLocale = getMessage(key, LocaleContextHolder.getLocale());
        if (StringUtils.isBlank(byLocale)) {
            return "";
        }
        // 获取转化类
        MessageFormat messageFormat = new MessageFormat(byLocale, LocaleContextHolder.getLocale());
        // 替换参数到值中
        return messageFormat.format(singleArg);
    }

    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, new Object[]{}, locale);
    }

    /**
     * 获取国际化消息并填充参数
     *
     * @param key  国际化消息key
     * @param args 参数
     * @return 国际化后的消息
     */
    public String getMessageArgs(String key, Object... args) {
        return getMessage(key, LocaleContextHolder.getLocale(), args);
    }

    /**
     * 获取国际化消息并填充参数
     *
     * @param key  国际化消息key
     * @param args 参数
     * @return 国际化后的消息
     */
    public String getMessageArgsAndDefault(String key, String defaultMessage, Object... args) {
        return getMessage(key, defaultMessage, LocaleContextHolder.getLocale(), args);
    }

    /**
     * 指定语言获取国际化消息并填充参数
     *
     * @param key    国际化消息key
     * @param locale 语言信息
     * @param args   参数
     * @return 国际化后的消息
     */
    public String getMessage(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }

    /**
     * 指定语言获取国际化消息并填充参数
     *
     * @param key    国际化消息key
     * @param locale 语言信息
     * @param args   参数
     * @return 国际化后的消息
     */
    public String getMessage(String key, String defaultMessage, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, defaultMessage, locale);
    }

    /**
     * 是否是英文环境
     */
    public boolean isUSLocale() {
        return Locale.US.getLanguage().equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage());
    }

}
