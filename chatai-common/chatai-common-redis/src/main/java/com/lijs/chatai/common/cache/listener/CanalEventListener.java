package com.lijs.chatai.common.cache.listener;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MySQL Binlog 与 Redisson 集成，用于实现缓存与数据库的数据一致性
 * 可以通过监听 Binlog 日志解析数据变更事件，并使用 Redisson 操作 Redis 缓存。以下是适配 Redisson 的具体实现。
 *
 * @author ljs
 * @date 2024-12-30
 * @description
 */
@Component
public class CanalEventListener {

    private final Logger logger = LoggerFactory.getLogger(CanalEventListener.class);

    //@Resource
    private CanalConnector canalConnector;

    private final RedissonClient redissonClient;

    /**
     * 此处注入条件：
     * 1、RedissonClient 已被声明为 Bean
     * 你需要确保 RedissonClient 的实例已经被 Spring 管理，例如在某个配置类中用 @Bean 标注。
     * 2、CanalEventListener 已被 Spring 管理
     * 由于你使用了 @Component 注解，CanalEventListener 类已经是一个 Spring Bean，Spring 会负责管理其生命周期，并在构造函数中注入 RedissonClient。
     * 3、类型匹配
     * Spring 会根据类型自动注入，确保 RedissonClient 类型没有冲突（例如，没有多个相同类型的 Bean）。
     */
    public CanalEventListener(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    //@PostConstruct
    public void start() {
        try {

            while (true) {
                Message message = canalConnector.getWithoutAck(100); // 获取 binlog 数据
                long batchId = message.getId();
                List<Entry> entries = message.getEntries();

                if (batchId != -1 && !entries.isEmpty()) {
                    handleEntries(entries); // 处理变更事件
                }
                canalConnector.ack(batchId); // 确认处理成功
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void handleEntries(List<Entry> entries) {
        for (Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                RowChange rowChange;
                try {
                    rowChange = RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException("Error parsing entry", e);
                }

                EventType eventType = rowChange.getEventType();
                for (RowData rowData : rowChange.getRowDatasList()) {
                    if (eventType == EventType.INSERT) {
                        handleInsert(rowData);
                    } else if (eventType == EventType.UPDATE) {
                        handleUpdate(rowData);
                    } else if (eventType == EventType.DELETE) {
                        handleDelete(rowData);
                    }
                }
            }
        }
    }

    private void handleInsert(RowData rowData) {
        Map<String, String> data = rowData.getAfterColumnsList()
                .stream()
                .collect(Collectors.toMap(Column::getName, Column::getValue));
        RMap<String, Object> map = redissonClient.getMap("table:primary_key:" + data.get("id"));
        map.putAll(data);
    }

    private void handleUpdate(RowData rowData) {
        Map<String, String> data = rowData.getAfterColumnsList()
                .stream()
                .collect(Collectors.toMap(Column::getName, Column::getValue));
        RMap<String, Object> map = redissonClient.getMap("table:primary_key:" + data.get("id"));
        map.putAll(data);
    }

    private void handleDelete(RowData rowData) {
        Map<String, String> data = rowData.getBeforeColumnsList()
                .stream()
                .collect(Collectors.toMap(Column::getName, Column::getValue));
        redissonClient.getMap("table:primary_key:" + data.get("id")).delete();
    }
}
