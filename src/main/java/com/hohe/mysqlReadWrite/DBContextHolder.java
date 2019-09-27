package com.hohe.mysqlReadWrite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class DBContextHolder {


    //创建一个TheadLocal变量 通过ThreadLocal将数据源设置到每个线程上下文中
   private static final ThreadLocal<String> threadLocal =  new ThreadLocal<>();

   private static final AtomicInteger counter = new AtomicInteger(-1);

   public static void set(String dbType){
       threadLocal.set(dbType);
   }

   public static String get(){
       return threadLocal.get();
   }

   public static void master(){
       threadLocal.set(DataSourceEnum.MASTER.getCode());
       System.out.println("切换到master库");
   }

    public static void slave() {
        //  轮询
        int index = counter.getAndIncrement() % 2;
        if (counter.get() > 9999) {
            counter.set(-1);
        }
//        if (index == 0) {
//            set(DBTypeEnum.SLAVE1);
//            System.out.println("切换到slave1");
//        }else {
//            set(DBTypeEnum.SLAVE2);
//            System.out.println("切换到slave2");
//        }
        set(DataSourceEnum.SLAVE1.getCode());
        System.out.println("切换到slave1");
    }


}
