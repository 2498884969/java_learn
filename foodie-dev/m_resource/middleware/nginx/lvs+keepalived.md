- Master 配置

```
global_defs {
   router_id LVS_151
}

vrrp_instance VI_1 {
    state MASTER
    interface enp0s3
    virtual_router_id 51
    priority 100
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        192.168.1.151
    }
}

virtual_server 192.168.1.151 80 {
    # 健康检查的时间，单位：秒
    delay_loop 6
    # 配置负载均衡的算法。默认是轮询
    lb_algo rr
    # 设置lvs的模式 NAT|TUN|DR
    lb_kind DR
    # 设置会话持久化的时间
    persistence_timeout 5
    # 协议类型
    protocol TCP
    
    # 负载均衡的真实服务器，也就是nginx节点
    real_server 192.168.1.60 80 {
        weight 1
        
        TCP_CHECK {
            # 检查80端口
            connect_port 80
            # 超时时间2s
            connect_timeout 2
            # 重试次数 2次
            nb_get_retry 2
            # 间隔时间3s
            delay_brefore_retry 3
        }
    }
    
    real_server 192.168.1.61 80 {
        weight 1
       
        TCP_CHECK {
            # 检查80端口
            connect_port 80
            # 超时时间2s
            connect_timeout 2
            # 重试次数 2次
            nb_get_retry 2
            # 间隔时间3s
            delay_brefore_retry 3
        }     
    }
}
```

- Slave 配置

```shell
! Configuration File for keepalived

global_defs {
   router_id LVS_152
}

vrrp_instance VI_1 {
    state BACKUP
    interface enp0s3
    virtual_router_id 51
    priority 50
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        192.168.1.151
    }
}

virtual_server 192.168.1.151 80 {
    # 健康检查的时间，单位：秒
    delay_loop 6
    # 配置负载均衡的算法。默认是轮询
    lb_algo rr
    # 设置lvs的模式 NAT|TUN|DR
    lb_kind DR
    # 设置会话持久化的时间
    persistence_timeout 5
    # 协议类型
    protocol TCP
    
    # 负载均衡的真实服务器，也就是nginx节点
    real_server 192.168.1.60 80 {
        weight 1
        
        TCP_CHECK {
            # 检查80端口
            connect_port 80
            # 超时时间2s
            connect_timeout 2
            # 重试次数 2次
            nb_get_retry 2
            # 间隔时间3s
            delay_brefore_retry 3
        }
    }
    
    real_server 192.168.1.61 80 {
        weight 1
       
        TCP_CHECK {
            # 检查80端口
            connect_port 80
            # 超时时间2s
            connect_timeout 2
            # 重试次数 2次
            nb_get_retry 2
            # 间隔时间3s
            delay_brefore_retry 3
        }     
    }
}
```

