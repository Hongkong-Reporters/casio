package com.report.casio.registry.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZkUtils {
    private ZkUtils() {}

    private static final Map<InetSocketAddress, ZooKeeper> ZK_MAP = new ConcurrentHashMap<>();
    private static final CountDownLatch LATCH = new CountDownLatch(1);
    private static final int TIMEOUT = 5000;

    public static ZooKeeper connect(InetSocketAddress inetSocketAddress) {
        return connect(inetSocketAddress, TIMEOUT);
    }

    public static ZooKeeper connect(InetSocketAddress inetSocketAddress, int timeout) {
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(inetSocketAddress.getHostString(), timeout, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected && event.getType() == Watcher.Event.EventType.None) {
                    log.info("zookeeper主机名 {} 连接成功", inetSocketAddress);
                    LATCH.countDown();
                }
                if (event.getState() == Watcher.Event.KeeperState.Closed) {
                    ZK_MAP.remove(inetSocketAddress);
                }
            });
            ZK_MAP.putIfAbsent(inetSocketAddress, zooKeeper);
            LATCH.await();
        } catch (IOException | InterruptedException e) {
            log.error("连接zookeeper失败");
        } finally {
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return zooKeeper;
    }

    public static byte[] getData(InetSocketAddress inetSocketAddress, String path) {
        if (exist(inetSocketAddress, path)) {
            ZooKeeper zooKeeper = ZK_MAP.get(inetSocketAddress);
            try {
                return zooKeeper.getData(path, false, new Stat());
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }  else {
            log.error("address {} not exist", inetSocketAddress);
            return null;
        }
        return null;
    }

    public static boolean exist(InetSocketAddress inetSocketAddress, String path) {
        if (!ZK_MAP.containsKey(inetSocketAddress)) {
            return false;
        }
        try {
            return ZK_MAP.get(inetSocketAddress).exists(path, false) != null;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
