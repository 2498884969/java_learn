## 1. jvm 命令

> jvm参数X、XX：https://blog.csdn.net/longgeqiaojie304/article/details/93851827

```shell
# jinfo命令详解
https://blog.csdn.net/yx0628/article/details/80958488
# jstat
https://www.cnblogs.com/sxdcgaq8080/p/11089841.html
# java导出内存映像文件
https://www.jianshu.com/p/03231abd96ba
https://blog.csdn.net/xigua_0616/article/details/88903510
# oracle官方文档
https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jmap.html
https://docs.oracle.com/javase/8/docs/technotes/tools/unix/index.html
# jstack 检查死锁和高CPU的线程
https://www.jianshu.com/p/8d5782bc596e
# top指令详解
https://www.cnblogs.com/niuben/p/12017242.html
######################
# top -p pid -H
# jstack pid
# printf "%x" id

第五章
psi-probe
https://www.cnblogs.com/sunfie/p/10357052.html
# tomcat_manager
https://www.cnblogs.com/grasp/p/10032103.html
jdwp协议：
https://www.ibm.com/developerworks/cn/java/j-lo-jpda3/
tomcat-manager：
{tomcat}/webapps/docs/manager-howto.html
psi-probe:
https://github.com/psi-probe/psi-probe
tomcat优化相关参数：
${tomcat}/webapps/docs/config/http.html
${tomcat}/webapps/docs/config/host.html
${tomcat}/webapps/docs/config/context.html
${tomcat}/webapps/docs/connectors.html
apr连接器：
http://apr.apache.org/

第七章
jvm的运行时数据区
https://docs.oracle.com/javase/specs/jvms/se8/html/index.html
Metaspace
http://ifeve.com/jvm-troubleshooting-guide-4/
压缩类空间
https://blog.csdn.net/jijijijwwi111/article/details/51564271
CodeCache
https://blog.csdn.net/yandaonan/article/details/50844806
http://engineering.indeedblog.com/blog/2016/09/job-search-web-app-java-8-migration/
GC调优指南：
https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/toc.html
如何选择垃圾收集器
https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/collectors.html
G1最佳实践
https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/g1_gc_tuning.html#recommendations
G1 GC的一些关键技术
https://zhuanlan.zhihu.com/p/22591838
CMS日志格式
https://blogs.oracle.com/poonam/understanding-cms-gc-logs
G1日志格式
https://blogs.oracle.com/poonam/understanding-g1-gc-logs
GC日志分析工具
http://gceasy.io/   
GCViewer
https://github.com/chewiebug/GCViewer
ZGC：
http://openjdk.java.net/jeps/333

```



## 第七章

```shell
jstat -gc pid
################
JAVA_OPTS="$JAVA_OPTS -Xms128M -Xmx128M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=128M -XX:+UseCompressedClassPointers" 
#################
JAVA_OPTS="$JAVA_OPTS -Xms128M -Xmx128M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=128M -XX:-UseCompressedClassPointers"
#################编译方式执行 -Xcomp -Xint
JAVA_OPTS="$JAVA_OPTS -Xcomp -Xms128M -Xmx128M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=128M -XX:-UseCompressedClassPointers"
#################解释方式执行 
JAVA_OPTS="$JAVA_OPTS -Xint -Xms128M -Xmx128M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=128M -XX:-UseCompressedClassPointers"
```

- 

```
-Xms -Xmx													最小最大堆
-XX:NewSize -XX:MaxNewSize				新生代大小
-XX:NewRatio -XX:SurvivorRatio		新生代：老年代	eden:servior
-XX:MetaspaceSize -XX:MaxMetaspaceSize	设置元空间初始和最大大小
-XX:+UseCompressedClassPointers   开启压缩类指针
-XX:CompressedClassSpaceSize			ccs大小
-XX:InitialCodeCacheSize					codecache初始大小
-XX:ReservedCodeCacheSize					codecache最大大小				
```

- 垃圾回收算法

```
思想:枚举根节点,做可达性分析
根节点:类加载器、Thread、 虚拟机栈的本地变量表、static成员、常量引用、本地方法栈的变量等等
################
分代垃圾回收算法
1. young区采用复制算法
2. old区采用标记清除或者标记整理
对象分配
1. 对象优先在Eden区分配
2. 大对象直接进入老年代: -XX:PretenureSizeThreshold
3. 长期存活对象进入老年代: -XX:MaxTenuringThreshold -XX:+PrintTenuringDistribution -XX:TargetSurvivorRatio
```

- 垃圾收集器

```shell
串行收集器Serial : Serial、 Serial Old
并行收集器Parallel : Parallel Scavenge. Parallel Old ,吞吐量优先
并发收集器Concurrent : CMS、G1 ,停顿时间优先
##############并行和并发
并行( Parallel) : 指多条垃圾收集线程并行工作,但此时用户线程仍然处于等待状态。适合科学计算、后台处理等弱交互场景。
并发( Concurrent) :指用户线程与垃圾收集线程同时执行(但不-定是并行的，可能会交替执行) ,垃圾收集线程在执行的时候不会停顿用户程序的运行。适合对响应时间有要求的场景,比如web。
###############停顿时间和吞吐量
停顿时间:垃圾收集器做垃圾回收中断应用执行的时间。-XX:MaxGCPauseMillis
吞吐量:花在垃圾收集的时间和花在应用时间的占比。-XX:GCTimeRatio=<n> ,垃圾收集时间占: 1/1+n .
###############并行垃圾收集器
1. 吞吐量优先
2. -XX:+ UseParallelGC , -XX:+ UseParallelOldGC
3. Server模式下的默认收集器
jinfo -flag UseParallelGC pid
###############并发垃圾收集器
1. 响应时间优先
2. CMS : -XX:+UseConcMarkSweepGC -XX:+UseParNewGC 老年代 年轻代
3. G1: -XX:+UseG1GC
CMS_OPTIOON="-XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=5"
G1_OPTION="-XX:+UseG1GC -XX:+UseStringDeduplication -XX:StringDeduplicationAgeThreshold=3 XX:+UseCompressedClassPointers -XX:MaxGCPauseMillis=200" 
```

![](./img/垃圾收集器.png)

- 如何选择垃圾收集器

```shell
1. 优先调整堆的大小让服务器自己来选择
2. 如果内存小于100M选择串行收集器
3. 如果是单核,并且没有停顿时间的要求,串行或者JVM自己选
4. 如果允许停顿时间超过1秒, 选择并行或者JVM自己选
5. 如果响应时间最重要,并且不能超过1秒,使用并发收集器
```

- Parallel Collector

```
-XX:+ UseParallelGC手动开启, Server默认开启
-XX:ParallelGCThreads= <N> 多少个GC线程
CPU>8	N=5/8
CPU<8	N=CPU
可以自适应调节
```

- CMS

```
1. 并发收集
2. 低停顿、低延迟
3. 老年代收集器
################
1. 初始标记，初始标记ROOT，STW
2. 并发标记
3. 并发预清理
4. 重新标记 STW
5. 并发清除
6. 并发重置
########
缺点：
1. CPU敏感
2. 浮动垃圾（标记了但没有清除）
3. 内存碎片
##########
1. -XX:ConcGCThreads: 并发的GC线程数
2. -XX:+ UseCMSCompactAtFullCollection 	FGC之后做压缩
3. -XX:CMSFulIGCsBeforeCompaction				多少次	FGC后压缩一次
4. -XX:CMSInitiatingOccupancyFraction		碎片到什么程度会出发FGC
# -XX:CMSInitiatingOccupancyFraction=70 是指设定CMS在对内存占用率达到70%的时候开始GC(因为CMS会有浮动垃圾,所以一般都较早启动GC)
5. -XX:+UseCMSInitiatingOccupancyOnly 只是用设定的回收阈值(上面指定的70%),如果不指定,JVM仅在第一次使用设定值,后续则自动调整
6. -XX:+CMSScavengeBeforeRemark	在CMS GC前启动一次ygc，目的在于减少old gen对ygc gen的引用，降低remark时的开销-----一般CMS的GC耗时 80%都在remark阶段
```

> https://blog.csdn.net/varyall/article/details/80041068

- G1

```
https://www.jianshu.com/p/548c67aa1bc0
# Java Hotspot G1 GC的一些关键技术
https://www.cnblogs.com/hushaojun/p/6083906.html

1. 年轻代大小:避免使用-Xmn、-XX:NewRatio等显式设置，Young区大小,会覆盖暂停时间目标。
2. 暂停时间目标:暂停时间不要太严苛,其吞吐量目标是90%的应用程序时间和10%的垃圾回收时间,太严苛会直接影响到吞吐量
## 是否需要切换到G1
1. 50%以上的堆被存活对象占用
2. 对象分配和晋升的速度变化非常大
3. 垃圾回收时间特别长,超过了1秒

```

- GC日志工具

```
GC日志可视化工具：
https://blog.csdn.net/xerjava/article/details/90055986

-XX:+ PrintGCDetails -XX: + PrintGCTimeStamps -XX:+ PrintGCDateStamps -Xloggc:$CATALINA_ HOME/logs/gc.log -XX:+PrintHeapAtGC -XX:+ PrintTenuringDistribution
```

