package com.atcompany.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import sun.nio.ch.ThreadPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.*;


/**
 * Created by IntelliJ IDEA.
 * User: luna
 * Date: 2022/3/15
 */
public class nettyClient {

    private static nettyClientHandler handler;


    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());



   public Object getProxy(final Class<?> serviceClass,final String providerName){
       return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                                    new Class<?>[]{serviceClass}, new InvocationHandler() {
                   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                       if (handler == null) {
                           initClient();
                       }
                       //设置要发给服务器端的信息:协议头+参数
                       handler.setParam(providerName + args[0]);
                       //获取call()中的结果
                       return executor.submit(handler).get();
                   }
               });

   }



    /**
     * 初始化客户端
     */
    private static void initClient(){
        handler = new nettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
       Bootstrap bootstrap = new Bootstrap();
       bootstrap.group(group)
               .channel(NioSocketChannel.class)
               .option(ChannelOption.TCP_NODELAY,true)
               .handler(
                       new ChannelInitializer<SocketChannel>() {
                           @Override
                           protected void initChannel(SocketChannel socketChannel) throws Exception {
                               ChannelPipeline pipeline = socketChannel.pipeline();
                               pipeline.addLast(new StringDecoder());
                               pipeline.addLast(new StringEncoder());
                               pipeline.addLast(handler);
                           }
                       }
               );
       try {
           bootstrap.connect("127.0.0.1",7000).sync();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }


   }



}
