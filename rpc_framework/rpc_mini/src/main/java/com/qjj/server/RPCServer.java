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
 *         这边直接把对象给new出来了，但是RPC实现的话，应该是通过反射的方式
 *         TODO 反射的方式
 *         TODO 可以使用NIO的方式，比如使用Netty代替
 *         TODO 使用线程来避免阻塞
 */


public class RPCServer {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动了");
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Integer id = objectInputStream.readInt();
                    String result = userService.getUserByUserId(id);
                    objectOutputStream.writeObject(result);
                    objectOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("从IO中读取数据错误");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
}
