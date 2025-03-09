package com.lijs.fizz.common.cache.config;

import com.lijs.fizz.common.cache.serializer.SimpleRedisJsonCodec;
import com.lijs.fizz.common.cache.serializer.TenantKeyRedisSerializer;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ljs
 * @date 2024-10-09
 * @description
 */
@Configuration
@AutoConfigureBefore({RedisAutoConfiguration.class, RedissonAutoConfiguration.class})
public class RedissonConfig {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.sentinel.master}")
    private String sentinelMaster;
    @Value("${spring.redis.sentinel.nodes}")
    private String sentinelNodes;
    @Value("${redisson.threads:4}")
    private int threads;
    @Value("${redisson.nettyThreads:5}")
    private int nettyThreads;
    @Value("${redisson.connectPoolSize:20}")
    private int connectPoolSize;
    @Value("${redisson.connectPoolIdleSize:5}")
    private int connectPoolIdleSize;
    @Value("${redisson.connectTimeout:10000}")
    private int connectTimeout;
    @Value("${redisson.retryAttempts:3}")
    private int retryAttempts;
    @Value("${redisson.retryInterval:1500}")
    private int retryInterval;
    @Value("${redisson.timeout:10000}")
    private int timeout;
    @Value("${redisson.pingConnectionInterval:30000}")
    private int pingConnectionInterval;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        int cpuCores = Runtime.getRuntime().availableProcessors();
        threads = (threads == 0 ? cpuCores * 2 : threads);
        nettyThreads = (nettyThreads == 0 ? cpuCores : nettyThreads);
        // Redisson使用的线程池大小，默认等于 Runtime.getRuntime().availableProcessors() * 2，即 CPU 核心数的两倍。
        config.setThreads(threads);
        // Netty是Redisson底层用于网络通信的框架，此参数决定 Netty I/O 线程数，负责处理 Redis 客户端的网络请求。
        // Netty线程池的大小，默认等于 Runtime.getRuntime().availableProcessors()，即 CPU 核心数。
        config.setNettyThreads(nettyThreads);
        config.setTransportMode(TransportMode.NIO);
        // 设置序列化（禁用class类型信息）
        config.setCodec(new SimpleRedisJsonCodec());
        if (StringUtils.isNotEmpty(sentinelMaster) && StringUtils.isNotEmpty(sentinelNodes)) {
            // 支持哨兵模式
            logger.info("RedissonClient init useSentinelServers");
            String[] sentinels = sentinelNodes.split(",");
            config.useSentinelServers()
                    .setMasterName(sentinelMaster)
                    .setDatabase(database)
                    .setPassword(StringUtils.isEmpty(password) ? null : password)
                    .setTimeout(timeout)
                    .addSentinelAddress(formatRedisUrls(sentinels))
                    .setCheckSentinelsList(false)
//                    .setReadMode(ReadMode.MASTER)
            ; // 强制只读主节点
        } else if (host.contains(",")) {
            // 集群模式
            logger.info("RedissonClient init useClusterServers");
            String[] clusterNodes = host.split(",");
            ClusterServersConfig cConfig = config.useClusterServers();
            for (String node : clusterNodes) {
                cConfig.addNodeAddress("redis://" + node + ":" + port);
            }
            cConfig.setPassword(password);
            cConfig.setMasterConnectionPoolSize(connectPoolSize);
            cConfig.setMasterConnectionMinimumIdleSize(connectPoolIdleSize);
            cConfig.setConnectTimeout(connectTimeout);
            cConfig.setRetryAttempts(retryAttempts);
            cConfig.setRetryInterval(retryInterval);
            cConfig.setTimeout(timeout);
            cConfig.setPingConnectionInterval(pingConnectionInterval);
        } else {
            // 单机模式
            logger.info("RedissonClient init useSingleServer");
            config.useSingleServer()
                    .setAddress("redis://" + host + ":" + port)
                    .setDatabase(database)
                    .setPassword(StringUtils.isEmpty(password) ? null : password)
                    .setConnectionPoolSize(connectPoolSize)
                    .setConnectionMinimumIdleSize(connectPoolIdleSize)
                    .setTimeout(timeout);
        }

        return Redisson.create(config);
    }

    /**
     * 格式化 Redis URL，确保前缀一致
     */
    private String[] formatRedisUrls(String[] nodes) {
        return Arrays.stream(nodes)
                .map(node -> node.startsWith("redis://") ? node : "redis://" + node)
                .toArray(String[]::new);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String[] splitIps = host.split(",");
        logger.info("RedisConnectionFactory init ip:{}", host);
        if (splitIps.length == 1) {
            logger.info("RedisConnectionFactory init RedisStandaloneConfiguration");
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(host);
            config.setPort(Integer.parseInt(port));
            config.setPassword(password);
            return new LettuceConnectionFactory(config);
        } else {
            List<RedisNode> nodes = new ArrayList<>();
            for (String ip : splitIps) {
                RedisNode redisNode = new RedisNode(ip, Integer.parseInt(port));
                nodes.add(redisNode);
            }
            RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
            clusterConfiguration.setClusterNodes(nodes);
            clusterConfiguration.setPassword(password);
            return new LettuceConnectionFactory(clusterConfiguration);
        }
    }

    @Primary
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 使用StringRedisSerializer来序列化和反序列化redis的key
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用StringRedisSerializer序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // 支持多租户模式下对redis的key进行扩展并覆盖
        RedisSerializer<String> customKeyRedisSerializer = keyStringRedisSerializer();
        redisTemplate.setKeySerializer(customKeyRedisSerializer);
        redisTemplate.setHashKeySerializer(customKeyRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * key的序列化器（重写key的序列化器）
     */
    public RedisSerializer<String> keyStringRedisSerializer() {
        return new TenantKeyRedisSerializer();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

}
