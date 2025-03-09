package com.lijs.fizz.common.cache.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @author ljs
 * @date 2024-12-30
 * @description
 */
@Configuration
@Slf4j
public class CanalConfig {

    @Value("${canal.client.hostname:127.0.0.1}")
    private String hostname;

    @Value("${canal.client.port:11111}")
    private int port;

    @Value("${canal.client.destination:example}")
    private String destination;

    @Value("${canal.client.username:canal}")
    private String username;

    @Value("${canal.client.password:canal}")
    private String password;

    //@Bean
    public CanalConnector canalConnector() {
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(hostname, port), destination, username, password);
        connector.connect();
        connector.subscribe(".*\\..*"); // 订阅所有数据库和表
        connector.rollback();
        return connector;
    }
}