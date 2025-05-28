package com.lijs.chatai.agent.ip_check;


import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * @author ljs
 * @date 2025-05-28
 * @description
 */
public class IP2RegionChecker {
    private static Searcher searcher;

    static {
        try {
            // 从 resources/ip_database 加载文件
            ClassPathResource resource = new ClassPathResource("ip_database/ip2region.xdb");
            InputStream dbStream = resource.getInputStream();
            byte[] dbBytes = new byte[dbStream.available()];
            dbStream.read(dbBytes); // 读取全部字节
            searcher = Searcher.newWithBuffer(dbBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load ip2region.xdb", e);
        }
    }

    public static boolean isForeignIP(String ip) {
        try {
            String region = searcher.search(ip);
            // 返回格式：国家|区域|省份|城市|ISP
            return !region.startsWith("中国|"); // 非中国即境外
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid IP: " + ip, e);
        }
    }

    public static void main(String[] args) {
        System.out.println(isForeignIP("8.8.8.8")); // true（Google DNS，美国）
        System.out.println(isForeignIP("114.114.114.114")); // false（中国 DNS）
        System.out.println(isForeignIP("184.26.222.209"));
        System.out.println(isForeignIP("172.16.140.1"));
    }
}