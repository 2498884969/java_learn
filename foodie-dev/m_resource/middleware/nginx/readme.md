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

# 4. 源码安装

```
https://www.cnblogs.com/lichunyang321/p/8889227.html

yum -y install gcc pcre pcre-devel zlib zlib-devel openssl openssl-devel

mkdir /var/temp/nginx -p

./configure     --prefix=/usr/local/nginx     --pid-path=/var/run/nginx/nginx.pid     --lock-path=/var/lock/nginx.lock     --error-log-path=/var/log/nginx/error.log     --http-log-path=/var/log/nginx/access.log     --with-http_gzip_static_module     --http-client-body-temp-path=/var/temp/nginx/client     --http-proxy-temp-path=/var/temp/nginx/proxy     --http-fastcgi-temp-path=/var/temp/nginx/fastcgi     --http-uwsgi-temp-path=/var/temp/nginx/uwsgi     --http-scgi-temp-path=/var/temp/nginx/scgi

make

make install
```