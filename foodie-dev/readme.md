## 1. 各种配置项

- 支付中心使用的账号和密码

```$xslt
user:   5877511-2498884969
passwd: dwer-40lr-40tk-32fe
```
- 内网穿透工具

```$xslt
https://natapp.cn/
/Users/qiangxuhui/natapp -authtoken=9fbcaf63a037d96c
```

## 2. 测试网址

```$xslt
http://localhost:8080/foodie-center/userInfo.html

http://localhost:8080/foodie-center/order.html

访问图片的路径
http://localhost:8088/faces/1908017YR51G1XWH/face-1908017YR51G1XWH.jpeg

/Users/qiangxuhui/images/foodie/faces/1908017YR51G1XWH/face-1908017YR51G1XWH.jpeg

//    http://localhost:8088/swagger-ui.html     原路径
//    http://localhost:8088/doc.html     原路径

http://me.local.mukewang.com:8080/foodie-shop/register.html


```

## 3. 环境搭建

```
1. 安装mysql
https://www.cnblogs.com/feipeng8848/p/10470655.html
docker run --name mysqlserver -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d mysql
docker run --name mysqlserver -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:5.7

2. 安装ng

docker run -d -p 8080:80 --name fronted -v /Users/qiangxuhui/fronted:/usr/share/nginx/html nginx
```