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

### 12. redis缓存过期处理与内存淘汰机制

- **已经过期的key如何处理？**

设置了expire的key缓存过期了，但是服务器的内存还是会被占用，这是因为redis所基于的两种删除策略
redis有两种策略：

1. （主动）定时删除
   - 定时随机的检查过期的key，如果过期则清理删除。（每秒检查次数在redis.conf中的hz配置）
2. （被动）惰性删除
   - 当客户端请求一个已经过期的key的时候，那么redis会检查这个key是否过期，如果过期了，则删除，然后返回一个nil。这种策略对cpu比较友好，不会有太多的损耗，但是内存占用会比较高。

所以，虽然key过期了，但是只要没有被redis清理，那么其实内存还是会被占用着的。

- **如果内存被redis占用满了怎么办？**

内存占满了，可以使用硬盘，来保存，但是没意义，因为硬盘没有内存快，会影响redis性能。
所以，当内存占用满了以后，redis提供了一套缓存淘汰机制：MEMORY MANAGEMENT

`maxmemory`：当内存已使用率到达，则开始清理缓存

```
* noeviction：旧缓存永不过期，新缓存设置不了，返回错误
* allkeys-lru：清除最少用的旧缓存，然后保存新的缓存（推荐使用）
* allkeys-random：在所有的缓存中随机删除（不推荐）
* volatile-lru：在那些设置了expire过期时间的缓存中，清除最少用的旧缓存，然后保存新的缓存
* volatile-random：在那些设置了expire过期时间的缓存中，随机删除缓存
* volatile-ttl：在那些设置了expire过期时间的缓存中，删除即将过期的
```

## 3. Redis-sentinel

- **主从配置选项**

```shell
# 主节点密码
masterauth pass
# 指定主节点
replicaof <masterip> <masterport>
```

- **redis-sentien相关配置**

```shell
# Base
protected-mode no
port 26379
daemonize yes
pidfile /var/run/redis-sentinel.pid
logfile /usr/local/redis/sentinel/redis-sentinel.log
dir /usr/local/redis/sentinel

# Core
# 告诉sentinel去监听地址为ip:port的一个master，这里的master-name可以自定义，quorum是一个数字，指明当有多少个sentinel认为一个master失效时，master才算真正失效。
sentinel monitor imooc-master 192.168.1.60 6379
# 设置连接master和slave时的密码
# sentinel auth-pass <master-name> <password>
sentinel auth-pass imooc-master imooc
这个配置项指定了需要多少失效时间，一个master才会被这个sentinel主观地认为是不可用的。
sentinel down-after-milliseconds mymaster 10000

# sentinel parallel-syncs <master-name> <numslaves> 
# 这个配置项指定了在发生failover主备切换时最多可以有多少个slave同时对新的master进行 同步，这个数字越小，完成failover所需的时间就越长，但是如果这个数字越大，就意味着越 多的slave因为replication而不可用。可以通过将这个值设为 1 来保证每次只有一个slave 处于不能处理命令请求的状态。
sentinel parallel-syncs imooc-master 1

# sentinel failover-timeout <master-name> <milliseconds>
# failover-timeout 可以用在以下这些方面： 
#      1. 同一个sentinel对同一个master两次failover之间的间隔时间。
#      2. 当一个slave从一个错误的master那里同步数据开始计算时间。直到slave被纠正为向正确的master那里同步数据时。
#      3.当想要取消一个正在进行的failover所需要的时间。  
#     4.当进行failover时，配置所有slaves指向新的master所需的最大时间。不过，即使过了这个超时，slaves依然会被正确配置为指向master，但是就不按parallel-syncs所配置的规则来了。
sentinel failover-timeout imooc-master 20000
```

> 参考：https://www.cnblogs.com/xuliangxing/p/7149322.html

```shell
# Base
protected-mode no
port 26379
daemonize yes
pidfile /var/run/redis-sentinel.pid
logfile /usr/local/redis/sentinel/redis-sentinel.log
dir /usr/local/redis/sentinel
# Core
sentinel monitor imooc-master 192.168.0.60 6379 2
sentinel auth-pass imooc-master imooc
sentinel down-after-milliseconds imooc-master 10000
sentinel parallel-syncs imooc-master 1
sentinel failover-timeout imooc-master 20000
```

- 哨兵相关命令**

```
# 查看imooc-master下的master节点信息
sentinel master imooc-master

# 查看imooc-master下的slaves节点信息
sentinel slaves imooc-master

# 查看imooc-master下的哨兵节点信息
sentinel sentinels imooc-master
```

- **spring-boot继承哨兵**

```shell
spring:
  redis:
    database: 1
    password: imooc
    sentinel:
      master: imooc-master
      nodes: 192.168.1.60:26379,192.168.1.65:26379,192.168.1.66:26379
```

## 4. Redis-cluster

### 1. 特点

1. 每个节点知道彼此之间的关系，也会知道自己的角色，当然他们也会知道自己存在与一个集群环境中，他们彼此之间可以交互和通信，比如ping pong。那么这些关系都会保存到某个配置文件中，每个节点都有，这个我们在搭建的时候会做配置的。
2. 客户端要和集群建立连接的话，只需要和其中一个建立关系就行。
3. 某个节点挂了，也是通过超过半数的节点来进行的检测，客观下线后主从切换，和我们之前在哨兵模式中提到的是一个道理。
4. Redis中存在很多的插槽，又可以称之为槽节点，用于存储数据，这个先不管，后面再说。

### 2. 搭建

```shell
# 开启集群模式
cluster-enabled yes
# 每一个节点需要有一个配置文件，需要6份。每个节点处于集群的角色都需要告知其他所有节点，彼此知道，这个文件用于存储集群模式下的集群状态等信息，这个文件是由redis自己维护，我们不用管。如果你要重新创建集群，那么把这个文件删了就行
cluster-config-file nodes-201.conf
# 超时时间，超时则认为master宕机，随后主备切换
cluster-node-timeout 5000
# 开启AOF
appendonly yes
```

-

```shell
#####
# 注意1：如果你使用的是redis3.x版本，需要使用redis-trib.rb来构建集群，最新版使用C语言来构建了，这个要注意
# 注意2：以下为新版的redis构建方式
#####

# 创建集群，主节点和从节点比例为1，1-3为主，4-6为从，1和4，2和5，3和6分别对应为主从关系，这也是最经典用的最多的集群模式
redis-cli --cluster create ip1:port1 ip2:port2 ip3:port3 ip4:port4 ip5:port5 ip6:port6 --cluster-replicas 1

redis-cli --cluster create 192.168.1.60:6379 192.168.1.65:6379 192.168.1.66:6379 192.168.1.67:6379 192.168.1.68:6379 192.168.1.69:6379 --cluster-replicas 1 -a imooc
```

- **集群信息检查**

redis-cli --cluster check 192.168.25.64:6380

## 面试题

- 缓存击穿、穿透以及雪崩

> https://blog.csdn.net/kongtiao5/article/details/82771694

