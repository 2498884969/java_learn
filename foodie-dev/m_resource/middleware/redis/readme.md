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

### 5. AOF特点

1. 以日志的形式来记录用户请求的写操作。读操作不会记录，因为写操作才会存存储。
2. 文件以追加的形式而不是修改的形式。
3. redis的aof恢复其实就是把追加的文件从开始到结尾读取执行写操作。

### 6. AOF优势

1. AOF更加耐用，可以以秒级别为单位备份，如果发生问题，也只会丢失最后一秒的数据，大大增加了可靠性和数据完整性。所以AOF可以每秒备份一次，使用fsync操作。
2. 以log日志形式追加，如果磁盘满了，会执行 redis-check-aof 工具
3. 当数据太大的时候，redis可以在后台自动重写aof。当redis继续把日志追加到老的文件中去时，重写也是非常安全的，不会影响客户端的读写操作。
4. AOF 日志包含的所有写操作，会更加便于redis的解析恢复。

### 7. AOF劣势

1. 相同的数据，同一份数据，AOF比RDB大
2. 针对不同的同步机制，AOF会比RDB慢，因为AOF每秒都会备份做写操作，这样相对与RDB来说就略低。 每秒备份fsync没毛病，但是如果客户端的每次写入就做一次备份fsync的话，那么redis的性能就会下降。
3. AOF发生过bug，就是数据恢复的时候数据不完整，这样显得AOF会比较脆弱，容易出现bug，因为AOF没有RDB那么简单，但是呢为了防止bug的产生，AOF就不会根据旧的指令去重构，而是根据当时缓存中存在的数据指令去做重构，这样就更加健壮和可靠了。

### 8. AOF配置

```shell
# AOF 默认关闭，yes可以开启
appendonly no

# AOF 的文件名
appendfilename "appendonly.aof"

# no：不同步
# everysec：每秒备份，推荐使用
# always：每次操作都会备份，安全并且数据完整，但是慢性能差
appendfsync everysec

# 重写的时候是否要同步，no可以保证数据安全
no-appendfsync-on-rewrite no

# 重写机制：避免文件越来越大，自动优化压缩指令，会fork一个新的进程去完成重写动作，新进程里的内存数据会被重写，此时旧的aof文件不会被读取使用，类似rdb
# 当前AOF文件的大小是上次AOF大小的100% 并且文件体积达到64m，满足两者则触发重写
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
```

### 9. 到底采用RDB还是AOF呢？

1. 如果你能接受一段时间的缓存丢失，那么可以使用RDB
2. 如果你对实时性的数据比较care，那么就用AOF
3. 使用RDB和AOF结合一起做持久化，RDB做冷备，可以在不同时期对不同版本做恢复，AOF做热备，保证数据仅仅只有1秒的损失。当AOF破损不可用了，那么再用RDB恢复，这样就做到了两者的相互结合，也就是说Redis恢复会先加载AOF，如果AOF有问题会再加载RDB，这样就达到冷热备份的目的了。

### 10. RDB-AOF混合

> https://blog.csdn.net/yhl_jxy/article/details/91879874

### 11.主从复制

> https://blog.csdn.net/caokun12321/article/details/81225410



