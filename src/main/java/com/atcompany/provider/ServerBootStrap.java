package com.atcompany.provider;

import com.atcompany.netty.nettyServer;

/**
 * Created by IntelliJ IDEA.
 * User: luna
 * Date: 2022/3/14
 * 启动一个服务提供者，即nettyServer
 */
public class ServerBootStrap {
    public static void main(String[] args) {

        nettyServer.startServer("127.0.0.1",7000);

    }
}
