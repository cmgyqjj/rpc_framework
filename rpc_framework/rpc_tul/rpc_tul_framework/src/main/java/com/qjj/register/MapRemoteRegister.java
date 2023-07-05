package com.qjj.register;

import com.qjj.common.URL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:qjj
 * @create: 2023-07-02 00:05
 * @Description: 由于没有远程注册中心，所以这里使用本地文件来模拟
 */

public class MapRemoteRegister implements Serializable {

    private static Map<String, List<URL>> map = new HashMap<>();


//    public static void register(String interfaceName,URL url) {
//        List<URL> list =map.get(interfaceName);
//        if(list==null){
//            list=new ArrayList<>();
//        }
//        list.add(url);
//        map.put(interfaceName,list);
//    }
//
//    public static List<URL> get(String interfaceName) {
//        List<URL> list = map.get(interfaceName);
//        return list;
//    }
//    由于没有远程注册中心，所以这里使用本地文件来模拟
    public static void register(String interfaceName, URL url) {
        List<URL> list = map.get(interfaceName);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(url);
        map.put(interfaceName, list);
        saveFile();
    }

    public static List<URL> get(String interfaceName) {
        map=getFile();
        return map.get(interfaceName);
    }

    private static void saveFile(){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("/Users/qiingjiajun/git project/rpc/rpc_framework/rpc_framework/rpc_tul/rpc_tul_framework/src/main/java/com/qjj/temp.txt");
            new ObjectOutputStream(fileOutputStream).writeObject(map);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static Map<String,List<URL>> getFile(){
        try{
            FileInputStream fileInputStream = new FileInputStream("/Users/qiingjiajun/git project/rpc/rpc_framework/rpc_framework/rpc_tul/rpc_tul_framework/src/main/java/com/qjj/temp.txt");
            return (Map<String, List<URL>>) new ObjectInputStream(fileInputStream).readObject();
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
