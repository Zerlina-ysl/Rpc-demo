package com.atcompany.provider;

import com.atcompany.publicInterface.HelloService;

/**
 * Created by IntelliJ IDEA.
 * User: luna
 * Date: 2022/3/14
 */
public class HelloServiceImpl implements HelloService {

    private static int count = 0;



    //存在消费方调用该方法时就返回一个结果
    public String hello(String msg) {
        System.out.println("收到客户端消息="+msg);
        if(msg!=null){
            //count的值没有变化 说明每次new了一个新的HelloServiceImpl对象
            return "你好，客户端，我已经收到你的消息["+msg+"],第"+(++count)+"次。";
        }else{
            return "你好，客户端，我已经收到你的消息";
        }

    }
}
