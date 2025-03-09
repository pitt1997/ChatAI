package com.lijs.nex.common.cache.serializer;

import com.lijs.nex.common.base.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 自定义redis的key解析器 主要是针对多租户模式的扩展
 * 兼容了非租户模式String类型的解析器
 */
public class TenantKeyRedisSerializer extends StringRedisSerializer {

    private static final Logger logger = LoggerFactory.getLogger(TenantKeyRedisSerializer.class);

    public static final String TENANT_ID_KEY_PREFIX = "TENANT_ID";

    private final Charset charset;

    public TenantKeyRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public TenantKeyRedisSerializer(Charset charset) {
        super(charset);
        this.charset = charset;
    }

    @Override
    public byte[] serialize(@Nullable String currentKey) {
        String tenantId = TenantContextHolder.get();
        if (StringUtils.isBlank(currentKey)) {
            return null;
        }
        String finalKey = StringUtils.isBlank(tenantId) ? currentKey : generateTenantKey(tenantId, TENANT_ID_KEY_PREFIX, currentKey);
        if (logger.isDebugEnabled()) {
            logger.debug("redis[serialize], finalKey={}", finalKey);
        }
        return finalKey.getBytes(this.charset);
    }

    /**
     * 生成包含租户信息的 Key。
     *
     * @param tenantId  租户 ID
     * @param originKey 原始 Key
     * @return 拼接后的 Key
     */
    public static String generateTenantKey(String tenantId, String originKey) {
        return generateTenantKey(tenantId, TENANT_ID_KEY_PREFIX, originKey);
    }

    /**
     * 生成包含租户信息的 Key。
     *
     * @param tenantId  租户 ID
     * @param keyPrefix Key 前缀
     * @param originKey 原始 Key
     * @return 拼接后的 Key
     */
    public static String generateTenantKey(String tenantId, String keyPrefix, String originKey) {
        return String.join(":", keyPrefix, tenantId, originKey);
    }

    @Override
    public Class<?> getTargetType() {
        return String.class;
    }
}
