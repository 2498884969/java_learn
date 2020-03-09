## 1. 启动centos7镜像

```
docker run -it --name centos_custom -p 2222:22 centos:7 /bin/bash
```

## 2. 配置yum源

```
1、备份配置文件：
cp -a /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bak

2.修改CentOS-Base.repo文件，取消baseurl开头的行的注释，并增加mirrorlist开头的行的注释。
将文件中的http://mirror.centos.org替换成https://mirrors.huaweicloud.com，可以参考如下命令：
    sed -i "s/#baseurl/baseurl/g" /etc/yum.repos.d/CentOS-Base.repo
    sed -i "s/mirrorlist=http/#mirrorlist=http/g" /etc/yum.repos.d/CentOS-Base.repo
    sed -i "s@http://mirror.centos.org@https://mirrors.huaweicloud.com@g" /etc/yum.repos.d/CentOS-Base.repo

    3、执行yum clean all清除原有yum缓存。

4、执行yum makecache（刷新缓存）或者yum repolist all（查看所有配置可以使用的文件，会自动刷新缓存）。
```

## 3.安装 openssh
- https://www.cnblogs.com/zhenyuyaodidiao/p/4512249.html
```
1.设置密码
passwd 密码：123456

2. 安装openssh相关
yum install openssh -y
yum install openssh-server -y
yum -y install openssh-clients

3. 启动命令
/usr/sbin/sshd -D
创建启动文件 
run.sh

4. 报错
# 有时候需要进行指纹删除~/.ssh/known_hosts
ssh root@127.0.0.1 -p 2222

```
## 4.commit 打包镜像
```
docker commit -a "qxh" -m "centos_yum_ssh_123456"  centos_custom centos_custom_base:v1
启动命令
docker run -d -p 2223:22 centos_custom_base:v1 bash /run.sh
镜像推到阿里云
https://help.aliyun.com/document_detail/51810.html
```

## 5.dockerfile 

## 6.默认时区修改 
https://www.cnblogs.com/chuanzhang053/p/10081693.html
```
# mv /etc/localtime /etc/localtime.bak
ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
```
## 7. 安装网络工具
```
yum install net-tools -y
```