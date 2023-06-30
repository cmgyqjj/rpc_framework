package com.qjj.register;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:qjj
 * @create: 2023-06-30 10:32
 * @Description: 本地注册器
 */

public class LocalRegister {

    private static Map<String, Class> map = new HashMap<>();

    public static void register(String interfaceName,String version, Class implClass) {
        map.put(interfaceName+version, implClass);
    }

    public static Class get(String interfaceName,String version) {
        return map.get(interfaceName+version);
    }

}
