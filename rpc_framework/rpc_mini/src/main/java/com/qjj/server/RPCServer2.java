package com.qjj.server;






import com.qjj.service.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author:qjj
 * @create: 2023-07-14 23:28
 * @Description: rpc服务器端的启动器,使用BIO实现数据的传输
 *  RPCServer2相对于RPCServer1，使用了线程池，以免大量的请求创建大量线程
 */


public class RPCServer2 {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                3,//线程数
                10,//最大线程数=线程数+临时线程
                1000,//临时线程的保留时间
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(6),//等待队列长度 6
                Executors.defaultThreadFactory(),//生产线程的工厂
                new ThreadPoolExecutor.AbortPolicy()
        );
        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动了");
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                            Integer id = objectInputStream.readInt();
                            String result = userService.getUserByUserId(id);
                            objectOutputStream.writeObject(result);
                            System.out.println(Thread.currentThread() + ":服务端发送数据成功:" + result);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("从IO中读取数据错误");
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            threadPool.shutdown();
            System.out.println("服务器启动失败");
        }
    }
}
