## 1. 测试环境

```shell
# centos7 
# python 2.75
# scapy
# tcpdump
```

> 参考：https://cjting.me/2019/08/28/tcp-queue/

## 2. 配置

- 源

```shell
yum install epel-release
# 换华为源
cp -a /etc/yum.repos.d/epel.repo /etc/yum.repos.d/epel.repo.backup
mv /etc/yum.repos.d/epel-testing.repo /etc/yum.repos.d/epel-testing.repo.backup
sed -i "s/#baseurl/baseurl/g" /etc/yum.repos.d/epel.repo
sed -i "s/metalink/#metalink/g" /etc/yum.repos.d/epel.repo
sed -i "s@http://download.fedoraproject.org/pub@https://mirrors.huaweicloud.com@g" /etc/yum.repos.d/epel.repo
##########
yum update
#######
yum install -y python-pip tcpdump
```

- 包

```shell
mkdir .pip
~/.pip/pip.conf
[global]
index-url=https://pypi.tuna.tsinghua.edu.cn/simple
trusted-host = pypi.tuna.tsinghua.edu.cn
########################
pip install scapy -y
```

### 3. 半连接

- 客户端代码

```python
from scapy.config import conf
from scapy.layers.inet import IP, TCP
from scapy.sendrecv import send
from scapy.supersocket import L3RawSocket
import random

conf.L3socket = L3RawSocket

while 1:
    ip = IP(src='10.1.1.{num}'.format(num=random.randint(1,255)), dst="192.168.1.65")
    tcp = TCP(dport=7788, flags="S")
    send(ip/tcp)
    time.sleep(0.1)
```

- shell指令

```shell
# 拒绝 RST 信号
iptables -A OUTPUT -p tcp --tcp-flags RST RST -j DROP
# 关闭 net.ipv4.tcp_syncookies 选项 
sysctl net.ipv4.tcp_syncookies=0
# 查看所有系统配置
sysctl -a
#
tcpdump -tn port 7788
## 查看端口状态
netstat -tn | grep 7788
## netstat -tn | grep 7788 | wc -l
```

## 4. 全连接

```shell
# 全连接队列的长度。
ss -tln
# Recv-Q 表示全连接队列当前的长度，Send-Q 表示全连接队列配置的长度

# 溢出查看,i不区分大小写
netstat -s 
netstat -s | grep -i listen

可见backlog在Linux 2.2之后表示的是已完成三次握手但还未被应用程序accept的队列长度。
正好等于256(apache prefork模式MaxClients即apache可以响应的最大并发连接数) + 128(backlog即已完成三次握手等待apache accept的最大连接数)。说明全连接队列长度等于min(backlog,somaxconn);

net.ipv4.tcp_abort_on_overflow
```



