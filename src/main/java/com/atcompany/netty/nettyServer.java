package com.atcompany.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
 * Created by IntelliJ IDEA.
 * User: luna
 * Date: 2022/3/14
 */
public class nettyServer {


    public static void startServer(String hostname,int port){
        startServer0(hostname,port);
    }



    private static void startServer0(String hostname,int port){

        try {
            //处理socket的线程是boss线程
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                    //服务端接收socket连接，产生一个channel，并把channel交给worker线程处理

                    .channel(NioServerSocketChannel.class)
                    //添加并设置childHandler childHandler在客户端成功connect后执行
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //编码
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new nettyServerHandler());

                        }
                    });
                //bind()将参数转换new InetSocketAddress(hostname, port)
            //不仅仅绑定端口号，还建立连接，代码底层：server.accept()是阻塞的，所以要等待异步的socket绑定事件完成
            ChannelFuture channelFuture = serverBootstrap.bind(hostname,port).sync();
            System.out.println("服务提供方开始提供服务...");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
