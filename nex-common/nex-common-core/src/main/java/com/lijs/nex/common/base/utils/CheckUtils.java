package com.lijs.nex.common.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

/**
 * 对象有效性校验工具类，包括Object、String、集合、Map等类型
 */
public final class CheckUtils {

    public static boolean isNotEmpty(String src) {
        return src != null && !src.trim().isEmpty();
    }

    public static boolean isNotEmpty(String[] src) {
        for (String s : src) {
            if (!isNotEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDate(String date, String format) {
        if (date == null || format == null) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            // 忽略
        }
        return false;
    }

    public static boolean isNotNull(Object obj) {
        return !(obj == null);
    }

    public static boolean isAllNotNull(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return false;
        }
        for (Object obj : objs) {
            if (!isNotNull(obj)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(Collection<?> col) {
        return col != null && !col.isEmpty();
    }

    public static boolean isAllNotEmpty(Collection<?>... cols) {
        if (cols == null) {
            return false;
        }
        for (Collection<?> col : cols) {
            if (!isNotEmpty(col)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static boolean isAllNotEmpty(Map<?, ?>... maps) {
        if (maps == null) return false;
        for (Map<?, ?> map : maps) {
            if (!isNotEmpty(map)) {
                return false;
            }
        }
        return true;
    }
}
