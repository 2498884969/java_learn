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

## 2. 持久化

**RDB: Redis DataBase
AOF: Append Only File**

### 1.什么是RDB

RDB：每隔一段时间，把内存中的数据写入磁盘的临时文件，作为快照，恢复的时候把快照文件读进内存。如果宕机重启，那么内存里的数据肯定会没有的，那么再次启动redis后，则会恢复。

### 2. 备份与恢复

内存备份 --> 磁盘临时文件
临时文件 --> 恢复到内存

### 3. RDB优劣势

- 优势

1. 每隔一段时间备份，全量备份
2. 灾备简单，可以远程传输
3. 子进程备份的时候，主进程不会有任何io操作（不会有写入修改或删除），保证备份数据的的完整性
4. 相对AOF来说，当有更大文件的时候可以快速重启恢复

- 劣势

1. 发生故障是，有可能会丢失最后一次的备份数据
2. 子进程所占用的内存比会和父进程一模一样，如会造成CPU负担
3. 由于定时全量备份是重量级操作，所以对于实时备份，就无法处理了。

### 4. RDB的配置

1. 保存位置，可以在redis.conf自定义：
   /user/local/redis/working/dump.rdb
2. 保存机制：

```shell
save 900 1
save 300 10
save 60 10000
save 10 3
```

1. stop-writes-on-bgsave-error
   - yes：如果save过程出错，则停止写操作
   - no：可能造成数据不一致
2. rdbcompression
   - yes：开启rdb压缩模式
   - no：关闭，会节约cpu损耗，但是文件会大，道理同nginx
3. rdbchecksum
   - yes：使用CRC64算法校验对rdb进行数据校验，有10%性能损耗
   - no：不校验