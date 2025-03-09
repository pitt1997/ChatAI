package com.lijs.fizz.common.base.utils;

import com.lijs.fizz.common.base.constant.CommonConstants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Cookie的工具类
 */
public class CookieUtils {

    /**
     * 设置 Cookie，过期时间为 60 分钟。
     *
     * @param response 响应对象
     * @param name     Cookie 名称
     * @param value    Cookie 值
     * @param path     Cookie 路径
     * @return 创建的 Cookie 对象
     */
    public static Cookie setCookie(HttpServletResponse response, String name, String value, String path) {
        return addCookie(response, name, value, path, 60 * 60);
    }

    /**
     * 设置 Cookie，过期时间自定义。
     *
     * @param response 响应对象
     * @param name     Cookie 名称
     * @param value    Cookie 值
     * @param path     Cookie 路径
     * @param maxAge   Cookie 过期时间，单位为秒
     * @return 创建的 Cookie 对象
     */
    public static Cookie addCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        Cookie cookie = null;
        try {
            cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
            cookie.setPath(path);
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cookie;
    }

    /**
     * 设置 Cookie，支持自定义过期时间和域名。
     *
     * @param response 响应对象
     * @param name     Cookie 名称
     * @param value    Cookie 值
     * @param domain   Cookie 域名
     * @param path     Cookie 路径
     * @param maxAge   Cookie 过期时间，单位为秒
     * @return 创建的 Cookie 对象
     */
    public static Cookie addCookie(HttpServletResponse response, String name, String value, String domain, String path, int maxAge) {
        Cookie cookie = null;
        try {
            cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            cookie.setMaxAge(maxAge);
            if (null != path) {
                cookie.setPath(path);
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cookie;
    }

    /**
     * 批量设置 Cookies，支持自定义过期时间。
     *
     * @param response 响应对象
     * @param values   Cookie 名称和值的映射
     * @param path     Cookie 路径
     * @param maxAge   Cookie 过期时间，单位为秒
     * @return 创建的 Cookie 列表
     */
    public static ArrayList<Cookie> addCookies(HttpServletResponse response, Map<String, String> values, String path, int maxAge) {
        Set<Map.Entry<String, String>> entries = values.entrySet();
        ArrayList<Cookie> cookies = new ArrayList<Cookie>();
        try {
            for (Map.Entry<String, String> entry : entries) {
                Cookie cookie = new Cookie(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
                cookie.setMaxAge(maxAge);
                if (null != path) {
                    cookie.setPath(path);
                }
                response.addCookie(cookie);
                cookies.add(cookie);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cookies;
    }

    /**
     * 根据 Cookie 名称获取对应的值。
     *
     * @param request 请求对象
     * @param name    Cookie 名称
     * @return Cookie 值，如果不存在返回 null
     */
    public static String getCookie(HttpServletRequest request, String name) {
        return getCookie(request, null, name, false);
    }

    /**
     * 根据 Cookie 名称获取对应的值，并在获取后删除该 Cookie。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     Cookie 名称
     * @return Cookie 值，如果不存在返回 null
     */
    public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        return getCookie(request, response, name, true);
    }

    /**
     * 根据 Cookie 名称获取对应的值，可选择性地删除该 Cookie。
     *
     * @param request   请求对象
     * @param response  响应对象
     * @param name      Cookie 名称
     * @param isRemoved 是否在获取后删除该 Cookie
     * @return Cookie 值，如果不存在返回 null
     */
    public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemoved) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    try {
                        value = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (isRemoved) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * 从 HTTP 头和 Cookie 中获取指定键的值。
     *
     * @param request 请求对象
     * @param key     要获取的键
     * @return 值，如果不存在返回空字符串
     */
    public static String headerOrCookieKey(HttpServletRequest request, String key) {
        String header = request.getHeader(key);
        if (StringUtils.isNotEmpty(header)) {
            return header;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return CommonConstants.STR_EMPTY;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return CommonConstants.STR_EMPTY;
    }

    /**
     * 根据名称前缀删除匹配的 Cookies。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param prefix   Cookie 名称前缀
     */
    public static void clearCookiesWithPrefix(HttpServletRequest request, HttpServletResponse response, String prefix) {
        // 获取所有的cookie
        Cookie[] cookies = request.getCookies();
        // 遍历所有cookie
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 检查cookie名称是否以指定前缀开头
                if (cookie.getName().startsWith(prefix)) {
                    try {
                        cookie = new Cookie(cookie.getName(), URLEncoder.encode("", "UTF-8"));
                        cookie.setPath("/");
                        cookie.setMaxAge(-1);
                        response.addCookie(cookie);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
