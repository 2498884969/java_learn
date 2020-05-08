## 1. 入门

> 参考：https://blog.csdn.net/hmsiwtv/article/details/46960053

**kafka的主要特点**

Kafka是分布式发布-订阅消息系统。它最初由LinkedIn公司开发，之后成为Apache项目的一部分。Kafka是一个分布式的，可划分的，冗余备份的持久性的日志服务，它主要用于处理活跃的流式数据。

- kafka的主要特点：
  - 同时为发布和订阅提供高吞吐量。据了解，Kafka每秒可以生产约25万消息（50 MB），每秒处理55万消息（110 MB）。
  - 可进行持久化操作。将消息持久化到磁盘，因此可用于批量消费，例如ETL，以及实时应用程序。通过将数据持久化到硬盘以及replication防止数据丢失。
  - 分布式系统，易于向外扩展。所有的producer、broker和consumer都会有多个，均为分布式的。无需停机即可扩展机器。
  - 消息被处理的状态是在consumer端维护，而不是由server端维护。当失败时能自动平衡。
  - 支持online和offline的场景。



**kafka的架构**

------

Kafka的整体架构非常简单，是显式分布式架构，producer、broker（kafka）和consumer都可以有多个。Producer，consumer实现Kafka注册的接口，数据从producer发送到broker，broker承担一个中间缓存和分发的作用。broker分发注册到系统中的consumer。broker的作用类似于缓存，即活跃的数据和离线处理系统之间的缓存。客户端和服务器端的通信，是基于简单，高性能，且与编程语言无关的TCP协议。

- 基本概念：
  - Topic：特指Kafka处理的消息源（feeds of messages）的不同分类。
  - Partition：Topic物理上的分组，一个topic可以分为多个partition，每个partition是一个有序的队列。partition中的每条消息都会被分配一个有序的id（offset）。
  - Message：消息，是通信的基本单位，每个producer可以向一个topic（主题）发布一些消息。
  - Producers：消息和数据生产者，向Kafka的一个topic发布消息的过程叫做producers。
  - Consumers：消息和数据消费者，订阅topics并处理其发布的消息的过程叫做consumers。
  - Broker：缓存代理，Kafka集群中的一台或多台服务器统称为broker。
- 发送消息的流程：
  - Producer根据指定的partition方法（round-robin、hash等），将消息发布到指定topic的partition里面
  - kafka集群接收到Producer发过来的消息后，将其持久化到硬盘，并保留消息指定时长（可配置），而不关注消息是否被消费。
  - Consumer从kafka集群pull数据，并控制获取消息的offset

**kafka的优秀设计**

---

接下来，我们和小伙伴一起探讨一些kafka在实际工作中的一些重要应用场景，帮助小伙伴在以后的学习和工作里能够合理的运用kafka中间件服务于业务和架构设计。

- 消息队列
  - 比起大多数的消息系统来说，Kafka有更好的吞吐量，内置的分区，冗余及容错性，这让Kafka成为了一个很好的大规模消息处理应用的解决方案。消息系统一般吞吐量相对较低，但是需要更小的端到端延时，并常常依赖于Kafka提供的强大的持久性保障。在这个领域，Kafka足以媲美传统消息系统，如ActiveMQ或RabbitMQ。
- 行为跟踪
  - Kafka的另一个应用场景是跟踪用户浏览页面、搜索及其他行为，以发布-订阅的模式实时记录到对应的topic里。那么这些结果被订阅者拿到后，就可以做进一步的实时处理，或实时监控，或放到hadoop/离线数据仓库里处理。
- 元信息监控
  - 作为操作记录的监控模块来使用，即汇集记录一些操作信息，可以理解为运维性质的数据监控吧。
- 日志收集
  - 日志收集方面，其实开源产品有很多，包括Scribe、Apache Flume。很多人使用Kafka代替日志聚合（log aggregation）。日志聚合一般来说是从服务器上收集日志文件，然后放到一个集中的位置（文件服务器或HDFS）进行处理。然而Kafka忽略掉文件的细节，将其更清晰地抽象成一个个日志或事件的消息流。这就让Kafka处理过程延迟更低，更容易支持多数据源和分布式数据处理。比起以日志为中心的系统比如Scribe或者Flume来说，Kafka提供同样高效的性能和因为复制导致的更高的耐用性保证，以及更低的端到端延迟。
- 流处理
  - 这个场景可能比较多，也很好理解。保存收集流数据，以提供之后对接的Storm或其他流式计算框架进行处理。很多用户会将那些从原始topic来的数据进行阶段性处理，汇总，扩充或者以其他的方式转换到新的topic下再继续后面的处理。例如一个文章推荐的处理流程，可能是先从RSS数据源中抓取文章的内容，然后将其丢入一个叫做“文章”的topic中；后续操作可能是需要对这个内容进行清理，比如回复正常数据或者删除重复数据，最后再将内容匹配的结果返还给用户。这就在一个独立的topic之外，产生了一系列的实时数据处理的流程。[Storm](http://storm.incubator.apache.org/)和[Samza](http://samza.incubator.apache.org/)是非常著名的实现这种类型数据转换的框架。
- 事件源
  - 事件源是一种应用程序设计的方式，该方式的状态转移被记录为按时间顺序排序的记录序列。Kafka可以存储大量的日志数据，这使得它成为一个对这种方式的应用来说绝佳的后台。比如动态汇总（News feed）
- 持久性日志（commit log）
  - Kafka可以为一种外部的持久性日志的分布式系统提供服务。这种日志可以在节点间备份数据，并为故障节点数据回复提供一种重新同步的机制。Kafka中日志压缩功能为这种用法提供了条件。在这种用法中，Kafka类似于Apache BookKeeper项目。

### 搭建zk集群

接下来本神带着小伙伴们简单了解一下Apache-Zookeeper，并进行Zookeeper集群的环境搭建；

- ###### Zookeeper基础知识、体系结构、数据模型

  - Zookeeper是一个类似linux、hdfs的树形文件结构，zookeeper可以用来保证数据在(Zookeeper)集群之间的数据的事务性一致性，zookeeper也是我们常说的CAP理论中的CP（强一致性）；
  - Zookeeper有一个概念叫watch（也称之为事件），是一次性触发的，当watch监视的数据发生变化时，通知设置了该watch的client端，即watcher实例对象（用于改变节点的变化而做出相应的行为）
  - 关于Zookeeper其他相关内容，小伙伴可以参考一些相关的资料，在这里我们仅仅使用Zookeeper做注册中心；

- ###### Zookeeper有三个角色：Leader，Follower，Observer

  - Leader：数据总控节点，用于接收客户端连接请求，分发给所有的Follower节点后，各个Follower节点进行更新数据操作并返回给Leader节点，如果满足半数以上（所以Zookeeper集群一般是奇数个节点）更新成功则此次操作成功；
  - Follower：相当于跟随者的角色，Zookeeper的Leader宕机（挂掉）时，所有的Follower角色内部会产生选举机制，选举出新的Leader用于总控；
  - Observer：顾名思义，就是我们的客户端，用于观察Zookeeper集群的数据发送变化，如果产生变化则zookeeper会主动推送watch事件给Observer（客户端），用于对数据变化的后续处理；当然Observer（客户端）也可以发送数据变更请求；

- ###### Zookeeper应用场景：

  - 统一命名服务（Name Service）
  - 配置管理（Configuration Management）
  - 集群管理（Group Membership）
  - 共享锁（Locks）
  - 队列管理

**Zookeeper集群环境搭建与配置：**

```
1. 准备工作：
## 准备3个节点，要求配置好主机名称，服务器之间系统时间保持一致
## 注意 /etc/hostname 和 /etc/hosts 配置主机名称（在这个里我准备bhz125,bhz126,bhz127三节点）
## 特别注意 以下操作3个节点要同时进行操作哦！


2. 上传zk到三台服务器节点
## 注意我这里解压到/usr/local下
2.1 进行解压： tar zookeeper-3.4.6.tar.gz
2.2 重命名： mv zookeeper-3.4.6 zookeeper
2.3 修改环境变量： vim /etc/profile 
## 这里要添加zookeeper的全局变量
export ZOOKEEPER_HOME=/usr/local/zookeeper
export PATH=.:$ZOOKEEPER_HOME/bin
2.4 刷新环境变量： source /etc/profile
2.5 到zookeeper下修改配置文件： 
2.5.1 首先到指定目录： cd /usr/local/zookeeper/conf
2.5.2 然后复制zoo_sample.cfg文件，复制后为zoo.cfg： mv zoo_sample.cfg zoo.cfg
2.5.3 然后修改两处地方, 最后保存退出：
(1) 修改数据的dir
dataDir=/usr/local/zookeeper/data
(2) 修改集群地址
server.0=qxh60:2888:3888
server.1=qxh61:2888:3888
server.2=qxh62:2888:3888

2.6 增加服务器标识配置，需要2步骤，第一是创建文件夹和文件，第二是添加配置内容： 
(1) 创建文件夹： mkdir -p /usr/local/zookeeper/data
(2) 创建文件myid 路径应该创建在/usr/local/zookeeper/data下面，如下：
	vim /usr/local/zookeeper/data/myid
	注意这里每一台服务器的myid文件内容不同，分别修改里面的值为0，1，2；与我们之前的zoo.cfg配置文件里：server.0，server.1，server.2 顺序相对应，然后保存退出；
![图片描述](http://climg.mukewang.com/5e983fb8093e7bdd09060944.png)
2.7 到此为止，Zookeeper集群环境大功告成！启动zookeeper命令
	启动路径：/usr/local/zookeeper/bin（也可在任意目录，因为配置了环境变量）
	执行命令：zkServer.sh start (注意这里3台机器都要进行启动，启动之后可以查看状态)
	查看状态：zkServer.sh status (在三个节点上检验zk的mode, 会看到一个leader和俩个follower)
```

- zookeeper客户端操作

```
zkCli.sh 进入zookeeper客户端
根据提示命令进行操作： 
查找：ls /   ls /zookeeper
创建并赋值： create /imooc zookeeper
获取： get /imooc 
设值： set /imooc zookeeper1314 
PS1: 任意节点都可以看到zookeeper集群的数据一致性
PS2: 创建节点有俩种类型：短暂（ephemeral） 持久（persistent）, 这些小伙伴们可以查找相关资料，我们这里作为入门不做过多赘述！
```

> 参考：https://www.cnblogs.com/zzzmublog/p/11127657.html  -----节点操作

```
（1）tickTime：基本事件单元，以毫秒为单位。这个时间是作为 Zookeeper 服务器之间或客户端与服务器之间维持心跳的时间间隔，也就是每隔 tickTime时间就会发送一个心跳。
				
（2）initLimit：这个配置项是用来配置 Zookeeper 接受客户端初始化连接时最长能忍受多少个心跳时间间隔数，当已经超过 10 个心跳的时间（也就是 tickTime）长度后 Zookeeper 服务器还没有收到客户端的返回信息，那么表明这个客户端连接失败。总的时间长度就是 10*2000=20 秒。
		
（3）syncLimit：这个配置项标识 Leader 与 Follower 之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度，总的时间长度就是 5*2000=10 秒
				
（4）dataDir：存储内存中数据库快照的位置，顾名思义就是 Zookeeper 保存数据的目录，默认情况下，Zookeeper 将写数据的日志文件也保存在这个目录里。
   
（5）clientPort： 这个端口就是客户端连接 Zookeeper 服务器的端口，Zookeeper 会监听这个端口，接受客户端的访问请求。

（6）至于最后的配置项：server.A = B:C:D: 
A表示这个是第几号服务器,
B 是这个服务器的 ip 地址；
C 表示的是这个服务器与集群中的 Leader 服务器交换信息的端口；
D 表示的是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的 Leader
```

### kafka入门

```
## 解压命令：
tar -zxvf kafka_2.12-2.1.0.tgz -C /usr/local/
## 改名命令：
mv kafka_2.12-2.1.0/ kafka_2.12
## 进入解压后的目录，修改server.properties文件：
vim /usr/local/kafka_2.12/config/server.properties
## 修改配置：
broker.id=0
port=9092
host.name=192.168.1.60
advertised.host.name=192.168.1.60
log.dirs=/usr/local/kafka_2.12/kafka-logs
num.partitions=2
zookeeper.connect=192.168.1.60:2181,192.168.1.61:2181:2181,192.168.1.62:2181

## 建立日志文件夹：
mkdir -p /usr/local/kafka_2.12/kafka-logs

##启动kafka：
/usr/local/kafka_2.12/bin/kafka-server-start.sh /usr/local/kafka_2.12/config/server.properties &
```

### kafka常用命令

```
## 简单操作：
#（1）创建topic主题命令：（创建名为test的topic， 1个分区分别存放数据，数据备份总共1份）
kafka-topics.sh --zookeeper 192.168.1.60:2181 --create --topic topic1 --partitions 1  --replication-factor 1  
## --zookeeper 为zookeeper服务列表地址配置项，这里任意指定zookeeper其中一个服务列表地址即可
## --create 命令后 --topic 为创建topic 并指定 topic name
## --partitions 为指定分区数量配置项
## --replication-factor 为指定副本集数量配置项

#（2）查看topic列表命令：
kafka-topics.sh --zookeeper 192.168.1.160:2181 --list

#（3）kafka命令发送数据：（然后我们就可以编写数据发送出去了）
kafka-console-producer.sh --broker-list 192.168.1.60:9092 --topic topic1
## --brokerlist kafka服务的broker节点列表

#（4）kafka命令接受数据：（然后我们就可以看到消费的信息了）
kafka-console-consumer.sh --bootstrap-server 192.168.11.1:9092 --topic topic1 --from-beginning


#（5）删除topic命令：
kafka-topics.sh --zookeeper 192.168.1.60:2181 --delete --topic topic1

#（6）kafka查看消费进度：（当我们需要查看一个消费者组的消费进度时，则使用下面的命令）
kafka-consumer-groups.sh --bootstrap-server 192.168.1.60:9092 --describe --group group1
## --describe --group 为订阅组， 后面指定 group name
```

### api急速入门

- Pom

```
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka_2.12</artifactId>
</dependency> 
```

- producer

```java
package com.bfxy.mix.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import com.alibaba.fastjson.JSON;

public class CollectKafkaProducer {

    // 创建一个kafka生产者
	private final KafkaProducer<String, String> producer;
	// 定义一个成员变量为topic
	private final String topic;
    
     // 初始化kafka的配置文件和实例：Properties & KafkaProducer
	public CollectKafkaProducer(String topic) { 
		Properties props = new Properties(); 
         // 配置broker地址
		props.put("bootstrap.servers", "192.168.11.51:9092"); 
         // 定义一个 client.id
		props.put("client.id", "demo-producer-test"); 
		
         // 其他配置项：
        
//		props.put("batch.size", 16384);			//16KB -> 满足16KB发送批量消息
//		props.put("linger.ms", 10); 			//10ms -> 满足10ms时间间隔发送批量消息
//		props.put("buffer.memory", 33554432);	 //32M -> 缓存提性能
		
         // kafka 序列化配置：
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); 
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); 
         
         // 创建 KafkaProducer 与 接收 topic
		this.producer = new KafkaProducer<>(props);
		this.topic = topic; 
	}

    // 发送消息 （同步或者异步）
	public void send(Object message, boolean syncSend) throws InterruptedException { 
		try { 
             // 同步发送
			if(syncSend) {
				producer.send(new ProducerRecord<>(topic, JSON.toJSONString(message)));		
			} 
             // 异步发送（callback实现回调监听）
             else {
				producer.send(new ProducerRecord<>(topic, 
                              JSON.toJSONString(message)), 
                              new Callback() {
					@Override
					public void onCompletion(RecordMetadata recordMetadata, Exception e) {
	                    if (e != null) {
	                        System.err.println("Unable to write to Kafka in CollectKafkaProducer [" + topic + "] exception: " + e);
	                    }
					}
				});				
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	} 
	
    // 关闭producer
	public void close() {
		producer.close();
	}

    // 测试函数
	public static void main(String[] args) throws InterruptedException {
		String topic = "topic1";
		CollectKafkaProducer collectKafkaProducer = new CollectKafkaProducer(topic);

		for(int i = 0 ; i < 10; i ++) {
			User user = new User();
			user.setId(i+"");
			user.setName("张三");
			collectKafkaProducer.send(user, true);
		}
        
		Thread.sleep(Integer.MAX_VALUE);
	
	}
	
}
```

- consumer

```java
package com.bfxy.mix.kafka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import kafka.consumer.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CollectKafkaConsumer {
	
    // 定义消费者实例
	private final KafkaConsumer<String, String> consumer;
	// 定义消费主题
	private final String topic;


     // 消费者初始化
	public CollectKafkaConsumer(String topic) { 
		Properties props = new Properties();
         // 消费者的zookeeper 地址配置
		props.put("zookeeper.connect", "192.168.11.111:2181"); 
         // 消费者的broker 地址配置
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.51:9092");
		// 消费者组定义
         props.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-group-id"); 
         // 是否自动提交（auto commit，一般生产环境均设置为false，则为手工确认）
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
         // 自动提交配置项
//		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
         // 消费进度（位置 offset）重要设置: latest,earliest 
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
         // 超时时间配置
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000"); 
		// kafka序列化配置
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer"); 
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer"); 
        
         // 创建consumer对象 & 赋值topic
		consumer = new KafkaConsumer<>(props); 
		this.topic = topic; 
         // 订阅消费主题
		consumer.subscribe(Collections.singletonList(topic));
		
	} 
	
	// 循环拉取消息并进行消费，手工ACK方式
    private void receive(KafkaConsumer<String, String> consumer) {
        while (true) {
            // 	拉取结果集(拉取超时时间为1秒)
        	ConsumerRecords<String, String> records = consumer.poll(1000);
            //  拉取结果集后获取具体消息的主题名称 分区位置 消息数量
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                String topic = partition.topic();
                int size = partitionRecords.size();
                log.info("获取topic:{},分区位置:{},消息数为:{}", topic, partition.partition(), size);
            	// 分别对每个partition进行处理
                for (int i = 0; i< size; i++) {
                	System.err.println("-----> value: " + partitionRecords.get(i).value());
                    long offset = partitionRecords.get(i).offset() + 1;
                	// consumer.commitSync(); // 这种提交会自动获取partition 和 offset 
                     // 这种是显示提交partition 和 offset 进度
				   consumer.commitSync(Collections.singletonMap(partition, 
                                                            new OffsetAndMetadata(offset)));
                    log.info("同步成功, topic: {}, 提交的 offset: {} ", topic, offset);
                }

            }
        }
    }
	
    // 测试函数
	public static void main(String[] args) {
		String topic = "topic1";
		CollectKafkaConsumer collectKafkaConsumer = new CollectKafkaConsumer(topic);
		collectKafkaConsumer.receive(collectKafkaConsumer.consumer);
	}
}
```

