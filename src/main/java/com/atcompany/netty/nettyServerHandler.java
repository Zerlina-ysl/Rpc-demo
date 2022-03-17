package com.atcompany.netty;

import com.atcompany.customer.clientBootStrap;
import com.atcompany.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: luna
 * Date: 2022/3/15
 * 服务器的handler
 */
public class nettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的信息，并调用服务
        System.out.println("客户端发送的信息："+msg);
        //客户端在调用服务器api时需要定义协议 如-"HelloService#hello#"作为消息前缀

        if(msg.toString().startsWith(clientBootStrap.providerName)){
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf('#') + 1));
            ctx.writeAndFlush(result);

        }



    }
}
