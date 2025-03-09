package com.lijs.fizz.common.base.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.lijs.fizz.common.base.constant.CommonConstants;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonConfig {

    /**
     * 设置全局日期格式，控制日期和时间对象在序列化和反序列化时的格式化方式，包括 Date、LocalDate、LocalDateTime、LocalTime 类型
     */
    @Bean("baseJackson2ObjectMapperBuilderCustomizer")
    @ConditionalOnMissingBean(name = "baseJackson2ObjectMapperBuilderCustomizer")
    public static Jackson2ObjectMapperBuilderCustomizer baseJackson2ObjectMapperBuilderCustomizer(JacksonProperties jacksonProperties) {
        // 全局日期格式，控制日期和时间对象在序列化和反序列化时的格式化方式
        String dateFormat = jacksonProperties.getDateFormat() == null ? CommonConstants.DATETIME_FORMAT : jacksonProperties.getDateFormat();
        return builder -> {
            builder.simpleDateFormat(dateFormat);

            // 自定义序列化器
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalTimeSerializer(DateTimeFormatter.ofPattern(dateFormat)));

            // 设置时区 GMT+8:00（中国标准时间）确保时间戳在序列化和反序列化时的一致性
            builder.timeZone(jacksonProperties.getTimeZone() == null ?
                    TimeZone.getTimeZone("GMT+8:00") : jacksonProperties.getTimeZone());
            // 控制序列化包含规则 JsonInclude.Include.NON_NULL：忽略值为 null 的字段
            builder.serializationInclusion(jacksonProperties.getDefaultPropertyInclusion() == null
                    ? JsonInclude.Include.NON_NULL : jacksonProperties.getDefaultPropertyInclusion());
            // 启用或禁用某些特性：启用忽略大小写的字段匹配（ACCEPT_CASE_INSENSITIVE_PROPERTIES），即 JSON 中的字段名可以大小写不敏感
            builder.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);

            // 自定义反序列化器
            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.deserializers(new LocalTimeDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
        };
    }
}
