package com.lijs.chatai.common.base.license;

import com.lijs.chatai.common.base.enums.LicenseTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author ljs
 * @date 2024-11-05
 * @description
 */
@Component
public class LicenseService {

    public boolean isUsable(LicenseInfo licenseInfo) {
        if (licenseInfo == null || isLicenseEmpty(licenseInfo)) {
            return false;
        }

        // 正式授权一直可用
        if (LicenseTypeEnum.OFFICIAL.getCode().equals(licenseInfo.getType())) {
            return true;
        }

        LocalDateTime endTime = licenseInfo.getEndTime();
        if (endTime == null || LocalDateTime.now().isAfter(endTime)) { // License 已过期
            return false;
        }

        return true;
    }

    private boolean isLicenseEmpty(LicenseInfo licenseInfo) {
        return licenseInfo == null || StringUtils.isEmpty(licenseInfo.getName());
    }

}
