package com.qjj;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
*@Param:
*@return:
*@Author: qjj
*@describe: 初始启动类，采用注解扫描的方式启动，并为Runtime添加关闭钩子函数
*/
public class ApplicationMain {

    private static volatile boolean running = true;

    public static void main( String[] args ) {
        try{
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.qjj");
//            在JVM中增加一个关闭的钩子，当JVM关闭时
//            会执行系统中已经设置的所有通过方法addShutdownHook()添加的钩子
//            只有当系统执行完这些钩子之后，JVM才会关闭
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try{
                    context.stop();
                }catch (Throwable e){
                    e.printStackTrace();
                }
                synchronized (ApplicationMain.class){
                    running = false;
                    ApplicationMain.class.notify();
                }
            }));
            context.start();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("服务器已启动=====");
        synchronized (ApplicationMain.class){
            while (running){
                try{
                    ApplicationMain.class.wait();
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        }
    }
}
