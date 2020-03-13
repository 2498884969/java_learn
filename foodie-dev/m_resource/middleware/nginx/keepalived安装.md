## 1. 安装

```shell
yum -y install libnl libnl-devel  libnfnetlink-devel

tar -zxvf keepalived-2.0.18.tar.gz

./configure --prefix=/usr/local/keepalived --sysconf=/etc

make && make install
```

- prefix：keepalived安装的位置
- sysconf：keepalived核心配置文件所在位置，固定位置，改成其他位置则keepalived启动不了，/var/log/messages中会报错

```shell
将keeplive加入服务当中去
# /root/keepalived-1.3.4/keepalived/etc
cp init.d/keepalived /etc/init.d/
cp sysconfig/keepalived /etc/sysconfig/
cp /usr/local/keepalived/sbin/keepalived /usr/sbin/


```



## 2.配置

```
# vim keepalived.conf

global_defs {
   # 路由id：当前安装keepalived的节点主机标识符，保证全局唯一
   router_id keep_171
}

vrrp_instance VI_1 {
    # 表示状态是MASTER主机还是备用机BACKUP
    state MASTER
    # 该实例绑定的网卡
    interface ens33
    # 保证主备节点一致即可
    virtual_router_id 51
    # 权重，master权重一般高于backup，如果有多个，那就是选举，谁的权重高，谁就当选
    priority 100
    # 主备之间同步检查时间间隔，单位秒
    advert_int 2
    # 认证权限密码，防止非法节点进入
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    # 虚拟出来的ip，可以有多个（vip）
    virtual_ipaddress {
        192.168.1.161
    }
}
```

---------------------

```shell
# 1.
ip addr
# 2.
cd /usr/local/keepalived/sbin
./keepalived
# 3. 
ps -ef|grep keepalived
# 4.
ip addr
```

> https://www.cnblogs.com/kingsonfu/p/11392470.html

## 从节点配置

```
global_defs {
   router_id keep_172
}

vrrp_instance VI_1 {
    # 备用机设置为BACKUP
    state BACKUP
    interface ens33
    virtual_router_id 51
    # 权重低于MASTER
    priority 80
    advert_int 2
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        # 注意：主备两台的vip都是一样的，绑定到同一个vip
        192.168.1.161
    }
}
```

