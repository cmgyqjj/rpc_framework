package com.qjj.client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * @author:qjj
 * @create: 2023-07-14 23:37
 * @Description: rpc客户端的启动器,使用BIO实现数据的传输
 * 这个类是为了测试服务端是否会因为错误而阻塞
 */

public class RPCClient2 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8899);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeChar(1);
            objectOutputStream.flush();
            Object o = objectInputStream.readObject();
            String user = (String) o;
            System.out.println("服务端返回的User:"+user);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("客户端启动失败");
        }
    }
}
