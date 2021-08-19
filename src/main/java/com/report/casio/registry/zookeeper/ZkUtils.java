package com.report.casio.registry.zookeeper;

import com.report.casio.registry.cache.ConsumerServiceDiscoveryCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
/*
  路径：casio/serviceName/providers/hostname：服务提供者注册的路径格式
       casio/serviceName/routers：暂不考虑，参考dubbo，用于服务治理
  consumers节点是否需要?
  providers和routers不采用持久化节点
 */
public class ZkUtils {
    private ZkUtils() {}

    private static final Map<String, CuratorFramework> ZK_MAP = new ConcurrentHashMap<>();
    private static final Integer CONNECT_TIMEOUT = 3 * 1000;    // 连接超时时间
    private static final Integer SESSION_TIMEOUT = 30 * 1000;   // 回话超时时间

    public static void connect(String hostname) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        if (!ZK_MAP.containsKey(hostname)) {
            CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                    .connectString(hostname)
                    .retryPolicy(retryPolicy)
                    .connectionTimeoutMs(CONNECT_TIMEOUT)
                    .sessionTimeoutMs(SESSION_TIMEOUT)
                    .build();
            curatorFramework.start();
            ZK_MAP.putIfAbsent(hostname, curatorFramework);
        }
    }

    public static void close(String hostname) {
        if (ZK_MAP.containsKey(hostname)) {
            ZK_MAP.get(hostname).close();
        }
    }

    public static boolean exist(String hostname, String path) throws Exception {
        if (ZK_MAP.containsKey(hostname)) {
            CuratorFramework curatorFramework = ZK_MAP.get(hostname);
            return curatorFramework.checkExists().forPath(path) != null;
        }
        return false;
    }

    public static void create(String hostname, String path, byte[] data) throws Exception {
        if (!ZK_MAP.containsKey(hostname)) {
            connect(hostname);
        }
        if (!exist(hostname, path)) {
            CuratorFramework curatorFramework = ZK_MAP.get(hostname);
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, data);
        }
    }

    public static List<String> getChildNode(String hostname, String path) throws Exception {
        if (!exist(hostname, path)) {
            connect(hostname);
        }
        return ZK_MAP.get(hostname)
                .getChildren()
                .forPath(path);
    }

    public static void addNodeWatcher(String hostname, String path) throws Exception {
        if (exist(hostname, path)) {
            CuratorFramework framework = ZK_MAP.get(hostname);
            framework.getData()
                    .usingWatcher((CuratorWatcher) watchedEvent -> ConsumerServiceDiscoveryCache.remove(path))
                    .forPath(path);
        } else {
            log.error("{} not exist node, can not add watcher", hostname);
        }
    }

}
