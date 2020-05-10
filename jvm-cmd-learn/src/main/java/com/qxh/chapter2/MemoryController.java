package com.qxh.chapter2;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MemoryController {

    private List<User> userList = new ArrayList<User>();
    private List<Class<?>>  classList = new ArrayList<Class<?>>();

    /**
     * -Xmx32M -Xms32M
     * # 发生内存溢出时进行导出
     * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./
     * -XX:HeapDumpPath=./
     * jmap -dump:format=b,file=heap.hprof id
     * jmap -dump:live,format=b,file=heap
     * */
    @GetMapping("/heap")
    public String heap() {
        int i=0;
        while(true) {
            userList.add(new User(i++, UUID.randomUUID().toString()));
        }
    }


    /**
     * -XX:MetaspaceSize=32M -XX:MaxMetaspaceSize=32M
     * */
    @GetMapping("/nonheap")
    public String nonheap() {
        while(true) {
            classList.addAll(Metaspace.createClasses());
        }
    }

}