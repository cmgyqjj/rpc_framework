package com.qjj.loadbalance;

import com.qjj.common.URL;

import java.util.List;
import java.util.Random;

/**
 * @author:qjj
 * @create: 2023-07-02 00:12
 * @Description: 负载均衡
 */

public class LoadBalance {

    public static URL random(List<URL> urls) {
//        TODO 这个随机算法需要优化
//        实际上的负载均衡的随机不是这样的
        Random random = new Random();
        int n = random.nextInt(urls.size());
        return urls.get(n);
    }

}
