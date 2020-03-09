# 1.启动基础镜像

```
docker run -it -p 2224:22 centos_custom_base:v2 bash
```

# 2. 安装jdk

```
1.
java -version

2.
rpm -qa|grep openjdk -i
rpm -e --nodeps 需要删除的软件

3.
mkdir /usr/java
4.
tar -zxvf jdk-8u191-linux-x64.tar.gz
5.
mv jdk1.8.0_191/ /usr/java/
6.
vim /etc/profile
7.
export JAVA_HOME=/usr/java/jdk1.8.0_191
export CLASSPATH=.:%JAVA_HOME%/lib/dt.jar:%JAVA_HOME%/lib/tools.jar  
export PATH=$PATH:$JAVA_HOME/bin
8.
source /etc/profile

```

# 3. 打镜像

```
docker commit -a "qxh" -m "centos7_jdk8"  da5897bb1b9c centos7_jdk8:v1
docker tag centos7_jdk8:v1 registry.cn-hangzhou.aliyuncs.com/qxh_2020/centos7_jdk8:v1
docker push registry.cn-hangzhou.aliyuncs.com/qxh_2020/centos7_jdk8:v1
```

# 4.tomcat 镜像

```
docker commit -a "centos7_tomcat9" -m "centos7_tomcat9"  da5897bb1b9c centos7_tomcat9:v1

# 使得JAVA_HONE生效
source /etc/profile
# tomcat 静态文件挂在目录
/tomcat9/webapps/

yum install net-tools -y
```
