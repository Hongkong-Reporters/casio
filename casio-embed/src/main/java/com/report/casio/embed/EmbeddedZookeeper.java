package com.report.casio.embed;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.admin.AdminServer;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
public class EmbeddedZookeeper {
    private final int port;
    private final boolean daemon;
    private final String dataDir;
    private ZooKeeperServerMain zkServer;

    public EmbeddedZookeeper(int port, boolean daemon, String dataDir) {
        this.port = port;
        this.daemon = daemon;
        this.dataDir = dataDir;
    }

    public void start() {
        Thread zkServerThread = new Thread(new ZkServerRunnable(), "zk server start");
        zkServerThread.setDaemon(daemon);
        zkServerThread.start();
    }

    public void stop() {
        zkServer.close();
    }

    private class ZkServerRunnable implements Runnable {

        @Override
        public void run() {
            try {
                zkServer = new ZooKeeperServerMain();
                Properties properties = new Properties();
                properties.setProperty("dataDir", dataDir);
                properties.setProperty("clientPort", port + "");
                QuorumPeerConfig quorumPeerConfig = new QuorumPeerConfig();
                quorumPeerConfig.parseProperties(properties);
                ServerConfig serverConfig = new ServerConfig();
                serverConfig.readFrom(quorumPeerConfig);
                zkServer.runFromConfig(serverConfig);
            } catch (IOException | QuorumPeerConfig.ConfigException | AdminServer.AdminServerException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EmbeddedZookeeper(2181, false, "C:\\Ceilzcx\\project\\data\\zookeeper").start();
    }

}
