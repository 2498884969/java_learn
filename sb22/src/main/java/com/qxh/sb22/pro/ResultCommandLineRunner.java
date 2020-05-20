package com.qxh.sb22.pro;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ResultCommandLineRunner implements CommandLineRunner, EnvironmentAware, MyAware {

    private Environment env;

    private Flag flag;

    @Override
    public void run(String... args) throws Exception {
//        System.err.println(env.getProperty("imooc_url"));
//        System.err.println(env.getProperty("imooc_age"));
//        System.err.println(env.getProperty("imooc_path"));
//        System.err.println(env.getProperty("imooc_vm_name"));
//        System.err.println(flag.isCanOperate());

//        imooc_default_name
        System.err.println(env.getProperty("imooc_default_name"));
        System.err.println(env.getProperty("active_test"));
//        my_active2
        System.err.println(env.getProperty("active_test2"));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env=environment;
    }

    @Override
    public void setFlag(Flag fla) {
        flag = fla;
    }
}
