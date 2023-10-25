package com.qjj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
*@Param: 
*@return: 
*@Author: qjj
*@describe: 初始启动类，采用注解扫描的方式启动，并为Runtime添加关闭钩子函数
*/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@ComponentScan(basePackages={"com.qjj"})
public class ApplicationMainV1 {

    public static void main(String[] args){
        SpringApplication.run(ApplicationMainV1.class, args);
    }

}
