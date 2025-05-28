package com.lijs.chatai.agent.ip_check;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.net.InetAddress;

/**
 * @author ljs
 * @date 2025-05-28
 * @description
 */
public class GeoIPChecker {
    private static DatabaseReader reader;

    static {
        try {
            // 从 resources/ip_database 加载文件
            ClassPathResource resource = new ClassPathResource("ip_database/GeoLite2-City.mmdb");
            InputStream dbStream = resource.getInputStream();
            reader = new DatabaseReader.Builder(dbStream).build(); // 使用 InputStream 初始化
        } catch (Exception e) {
            throw new RuntimeException("Failed to load GeoIP database", e);
        }
    }
    public static boolean isForeignIP(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
            String countryCode = response.getCountry().getIsoCode();
            return !"CN".equals(countryCode); // 非中国即境外
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid IP or database error: " + ip, e);
        }
    }

    public static void main(String[] args) {
        System.out.println(isForeignIP("8.8.8.8")); // true（Google DNS，美国）
        System.out.println(isForeignIP("114.114.114.114")); // false（中国 DNS）
        System.out.println(isForeignIP("184.26.222.209"));
        System.out.println(isForeignIP("172.16.140.1"));
    }
}