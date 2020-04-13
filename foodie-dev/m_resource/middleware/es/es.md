## 1. 核心概念

ES -> 数据库
索引index -> 表
文档 document -> 行（记录）
字段 fields -> 列

```shell
stu_index
{
	id: 1001,
	name: jason,
	age: 19
},
{
	id: 1002,
	name: tom,
	age: 18
},
{
	id: 1003,
	name: rose,
	age: 22
}
```

**集群相关**

分片（shard）：把索引库拆分为多份，分别放在不同的节点上，比如有3个节点，3个节点的所有数据内容加在一起是一个完整的索引库。分别保存到三个节点上，目的为了水平扩展，提高吞吐量。

备份（replica）：每个shard的备份。

**简称**

shard = primary shard（主分片）
replica = replica shard（备份节点）

nrt = near real time (近实时)

## 2. 安装

```
tar -xzvf elasticsearch-7.4.2-linux-x86_64.tar.gz

mv elasticsearch-7.4.2 /usr/local/
```



### 1. ES目录介绍

- bin：可执行文件在里面，运行es的命令就在这个里面，包含了一些脚本文件等
- config：配置文件目录
- JDK：java环境
- lib：依赖的jar，类库
- logs：日志文件
- modules：es相关的模块
- plugins：可以自己开发的插件
- data：这个目录没有，自己新建一下，后面要用 -> mkdir data，这个作为索引目录



-------------

### 2. 修改核心配置文件elasticearch.yml

- 修改集群名称，默认是elasticsearch，虽然目前是单机，但是也会有默认的
- 为当前的es节点取个名称，名称随意，如果在集群环境中，都要有相应的名字

```yaml
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
cluster.name: imooc-application
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
node.name: es-node0
```

- 修改data数据保存地址
- 修改日志数据保存地址

```yaml
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
path.data: /usr/local/elasticsearch-7.4.2/data
#
# Path to log files:
#
path.logs: /usr/local/elasticsearch-7.4.2/logs
```

- 绑定es网络ip，原理同redis
- 默认端口号，可以自定义修改

```shell
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 0.0.0.0
#
# Set a custom port for HTTP:
#
#http.port: 9200
```

集群节点，名字可以先改成之前的那个节点名称

```yaml
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when this node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
#discovery.seed_hosts: ["host1", "host2"]
#
# Bootstrap the cluster using an initial set of master-eligible nodes:
#
cluster.initial_master_nodes: ["es-node0"]
```

### 3.修改jvm参数

vim jvm.options 

```yaml
# Xms represents the initial size of total heap space
# Xmx represents the maximum size of total heap space

-Xms128m
-Xmx128m
```

### 4. 添加用户

ES不允许使用root操作es，需要添加用户，操作如下：

```shell
useradd esuser
chown -R esuser:esuser /usr/local/elasticsearch-7.4.2
su esuser
whoami
```

### 5.启动ES

./elasticsearch
如果出现如下错误：

```
ERROR: [3] bootstrap checks failed
[1]: max file descriptors [4096] for elasticsearch process is too low, increase to at least [65535]
[2]: max number of threads [1856] for user [esuser] is too low, increase to at least [4096]
[3]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```

切换到root修改配置

```
vim /etc/security/limits.conf
* soft nofile 65536
* hard nofile 131072
* soft nproc 2048
* hard nproc 4096

vim /etc/sysctl.conf

vm.max_map_count=262145

sysctl -p 
```

### 6. 测试

访问你的虚拟机ip+端口号9200，如下则表示OK

```
{
  "name" : "es-node0",
  "cluster_name" : "imooc-application",
  "cluster_uuid" : "BfeKjkdiTHOmT9Vwgc6FYA",
  "version" : {
    "number" : "7.4.2",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "2f90bbf7b93631e52bafb59b3b049cb44ec25e96",
    "build_date" : "2019-10-28T20:40:44.881551Z",
    "build_snapshot" : false,
    "lucene_version" : "8.2.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

** 停止es**

如果是前台启动，直接ctrl+c就可以停止
后台启动：

```shell
./elasticsearch -d
```

**端口号意义**

- 9200：Http协议，用于外部通讯
- 9300：Tcp协议，ES集群之间是通过9300通讯

## 3. 基本操作

- 集群健康

```
GET     /_cluster/health
```

- 创建索引

```
PUT     /index_test
{
    "settings": {
        "index": {
            "number_of_shards": "2",
            "number_of_replicas": "0"
        }
    }
}
```

- 查看索引

```
GET     _cat/indices?v
```

- 删除索引

```shell
DELETE      /index_test
```

- 创建索引同时创建mappings

```shell
PUT     /index_str
{
    "mappings": {
        "properties": {
            "realname": {
            	"type": "text",
            	"index": true
            },
            "username": {
            	"type": "keyword",
            	"index": false
            }
        }
    }
}
```

- 为已存在的索引创建或创建mappings

```
POST        /index_str/_mapping
{
    "properties": {
        "id": {
        	"type": "long"
        },
        "age": {
        	"type": "integer"
        },
        "nickname": {
            "type": "keyword"
        },
        "money1": {
            "type": "float"
        },
        "money2": {
            "type": "double"
        },
        "sex": {
            "type": "byte"
        },
        "score": {
            "type": "short"
        },
        "is_teenager": {
            "type": "boolean"
        },
        "birthday": {
            "type": "date"
        },
        "relationship": {
            "type": "object"
        }
    }
}
```

- 注：某个属性一旦被建立，就不能修改了，但是可以新增额外属性

**主要数据类型**

- text, keyword, string
- long, integer, short, byte
- double, float
- boolean
- date
- object
- 数组不能混，类型一致

**字符串**

- text：文字类需要被分词被倒排索引的内容，比如`商品名称`，`商品详情`，`商品介绍`，使用text。
- keyword：不会被分词，不会被倒排索引，直接匹配搜索，比如`订单状态`，`用户qq`，`微信号`，`手机号`等，这些精确匹配，无需分词。

