package com.lijs.chatai.common.cache.service;

import com.lijs.chatai.common.cache.serializer.TenantKeyRedisSerializer;
import com.lijs.chatai.common.base.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


public abstract class BaseService implements Serializable {

    /**
     * 缓存对象key值定义
     */
    public String getKey(String key) {
        String tenantId = TenantContextHolder.get();
        if (key == null) {
            return null;
        } else if (StringUtils.isBlank(tenantId) || TenantContextHolder.getIgnore()) {
            return key;
        } else {
            return TenantKeyRedisSerializer.generateTenantKey(tenantId, key);
        }
    }

}
