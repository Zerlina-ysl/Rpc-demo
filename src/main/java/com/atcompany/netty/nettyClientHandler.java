package com.atcompany.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: luna
 * Date: 2022/3/15
 * 调用顺序：channelActive -> setParam -> call -> channelRead -> call
 */
//线程池创建线程执行任务实现Callable接口
    //1 - 任务结束后提供一个返回值
    //2 - 可以抛出异常

public class nettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    /**
     *AttributeMap 存储多个数据
     * 作为channel handler 和pipelin的桥梁
     */
    private ChannelHandlerContext context;

    private String result;

    //客户端调入方法时传入的参数
    private String param;




    /**
     * 创建与服务器的连接后被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;


    }


    /**
     * 收到服务器的数据后被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //result相当于共享变量
         result = msg.toString();
         //唤醒等待的线程
         notify();


    }
    /**
     * 被代理对象调用->给服务器发送数据->wait-> 等待被channelRead()唤醒
     * @return
     * @throws Exception
     */
    public synchronized Object call() throws Exception {
        //write->flush
        context.writeAndFlush(param);
        //等待channelRead获取到服务器结果后唤醒该线程
        wait();
        return result;
    }


    /**
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }


    void setParam(String param){
        this.param=param;
    }
}
