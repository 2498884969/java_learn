## 1.安装

```shell
# 1. 解压redis
tar -zxvf redis-5.0.5.tar.gz

# 2. 安装gcc编译环境，如果已经安装过了，那么就是 nothing to do
yum install gcc-c++ -y

# 3. 进入到 redis-5.0.5 目录，进行安装：
make && make install

# 4. 配置redis在utils下，拷贝redis_init_script到/etc/init.d目录，目的要把redis作为开机自启动

# 5. 创建 /usr/local/redis，用于存放配置文件

# 6. 拷贝redis配置文件：

	1. 修改 daemonize no -> daemonize yes，目的是为了让redis启动在linux后台运行
	
	2. 修改redis的工作目录：
		建议修改为： /usr/local/redis/working，名称随意
		dir /usr/local/redis/working
		
  3. 最关键的是密码，默认是没有的，一定要设置
  	requirepass
  
  REDISPORT=6379
  EXEC=/usr/local/bin
  CLIEXEC=/usr/local/bin/redis-cli
  
  PIDFILE=/var/run/redis_${REDISPORT}.pid
  CONF="/usr/local/redis/${REDISPORT}.conf"
  
# 7. 运行
chmod 777 redis_init_script
./redis_init_script start
# 8.启动脚本添加如下内容 开机自启动
#chkconfig: 22345 10 90
#description: Start and Stop redis

chkconfig redis_init_script on
```

