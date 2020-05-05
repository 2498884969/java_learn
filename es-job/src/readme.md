```
systemctl start docker

1. 使用docker启动zookeeper

docker pull zookeeper

docker run --privileged=true -d --name zookeeper --publish 2181:2181  -d zookeeper:latest

```