package com.report.casio;

import com.report.casio.remoting.transport.netty.client.NettyClient;
import com.report.casio.remoting.transport.netty.server.NettyServer;
import org.junit.Test;

import java.net.InetSocketAddress;

public class NettyTest {

    @Test
    public void testServer() {
        new NettyServer().doOpen();
    }

    @Test
    public void testClient() {
        NettyClient client = new NettyClient();
        InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved("10.1.83.189", 9001);
        client.doConnect(inetSocketAddress);
    }

}
