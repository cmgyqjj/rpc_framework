package com.qjj.server;




import com.qjj.service.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author:qjj
 * @create: 2023-07-14 23:28
 * @Description: rpc服务器端的启动器,使用BIO实现数据的传输
 *  RPCServer1相对于RPCServer，每次有新的请求过来，都会创建一个新的线程来处理
 */


public class RPCServer1 {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动了");
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new Runnable() {
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
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
}
