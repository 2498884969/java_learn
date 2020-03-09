# 1. 安装nginx

```
docker run -d -p 3001:22 -p 3080:80 -p 3880:8080 -p 3888:8088 --name nginx_test centos_custom_base:v2 /bin/bash /run.sh

yum install net-tools -y

# 指定repo进行安装
yum install nginx --enablerepo=nginx
```

# 2. 生成centos_nginx镜像

```
docker commit nginx_test centos_nginx:v1
```

# 3.

```
静态文件的默认目录
/usr/share/nginx/html

```