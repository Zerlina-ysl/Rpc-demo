package com.atcompany.customer;

import com.atcompany.netty.nettyClient;
import com.atcompany.publicInterface.HelloService;

/**
 * Created by IntelliJ IDEA.
 * User: luna
 * Date: 2022/3/15
 */
public class clientBootStrap {
   //定义协议头
   public static final String providerName = "helloService#hello#";




    public static void main(String[] args) throws InterruptedException {

        //创建一个消费者
        nettyClient customer = new nettyClient();
        //创建代理对象
        HelloService service = (HelloService) customer.getProxy(HelloService.class, providerName);

        for(;;) {

            Thread.sleep(1*1000);
            //通过代理对象调用服务提供者的方法
            String res = service.hello("netty的rpcdemo");
            System.out.println("调用的结果res:" + res);
        }
    }



}
