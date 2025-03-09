package com.lijs.fizz.common.web.license;

import com.lijs.fizz.common.cache.service.RedisCacheService;
import com.lijs.fizz.common.base.constant.CommonConstants;
import com.lijs.fizz.common.base.license.LicenseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ljs
 * @date 2024-11-05
 * @description
 */
@Component
public class LicenseCacheService {

    @Autowired
    private RedisCacheService cacheService;

    public LicenseInfo getLicense() {
        if (cacheService.isExist(CommonConstants.CACHE_KEY.LICENSE_INFO)) {
            return cacheService.get(CommonConstants.CACHE_KEY.LICENSE_INFO, LicenseInfo.class);
        }
        return null;
    }

    public void setLicense(LicenseInfo licenseInfoVO) {
        cacheService.set(CommonConstants.CACHE_KEY.LICENSE_INFO, licenseInfoVO);
    }

}
