## 1. sql

```mysql
-- 1. 创建数据库
CREATE DATABASE ssmdemo;
-- 2. 创建表
DROP TABLE IF EXISTS tb_user;
CREATE TABLE tb_user (
id char(32) NOT NULL,
user_name varchar(32) DEFAULT NULL,
password varchar(32) DEFAULT NULL,
name varchar(32) DEFAULT NULL,
age int(10) DEFAULT NULL,
sex int(2) DEFAULT NULL,
birthday date DEFAULT NULL,
created datetime DEFAULT NULL,
updated datetime DEFAULT NULL,
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 3. 插入数据
INSERT INTO ssmdemo.tb_user (id, user_name, password, name, age, sex, birthday, created, updated) VALUES ('1', 'zpc', '123456', '鹏程', '22', '1', '1990-09-02', sysdate(), sysdate());
INSERT INTO ssmdemo.tb_user (id, user_name, password, name, age, sex, birthday, created, updated) VALUES ('2', 'hj', '123456', '静静', '22', '1', '1993-09-05', sysdate(), sysdate());
```

- mybatis使用步骤总结

```text
1)配置mybatis-config.xml 全局的配置文件 (1、数据源，2、外部的mapper)
2)创建SqlSessionFactory
3)通过SqlSessionFactory创建SqlSession对象
4)通过SqlSession操作数据库 CRUD
5)调用session.commit()提交事务
6)调用session.close()关闭会话
```

## 2. 动态代理