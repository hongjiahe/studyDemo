package com.hohe.service.listener;


import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 *  自定义事件监听类
 *
 */
@Component
public class MyListener  implements ApplicationListener<MyEvent> {

    @Override
    public void onApplicationEvent(MyEvent myEvent) {
        System.out.println(myEvent.getTimestamp());
        System.out.println(myEvent.getClass());
        System.out.println(myEvent.getSource()+"===================");
    }

}
