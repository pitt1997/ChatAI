package com.lijs.nex.common.spi.utils;

import com.lijs.nex.common.spi.service.permission.DataPermissionService;

import java.util.ServiceLoader;

/**
 * @author ljs
 * @date 2024-12-16
 * @description SPI服务加载工具类
 */
public class SpiLoaderUtils {

    public static <T> T load(Class<T> serviceClass) {
        ServiceLoader<T> loader = ServiceLoader.load(serviceClass);
        for (T service : loader) {
            return service; // 返回找到的第一个实现
        }
        throw new IllegalStateException("No implementation found for " + serviceClass.getName());
    }

    public static void main(String[] args) {
        DataPermissionService service = SpiLoaderUtils.load(DataPermissionService.class);
    }

}
