## 1. 参考

> https://blog.csdn.net/wuyongpeng0912/article/details/88895659
>
> 日志：# https://blog.csdn.net/wangpengfei163/article/details/80423863



## 2. 范例

```python
import logging

from apscheduler.triggers.cron import CronTrigger
from logging import FileHandler

file_handle = FileHandler(filename='apscheduler.log')

logging.basicConfig(format='%(asctime)s - %(pathname)s[line:%(lineno)d] - %(levelname)s: %(message)s')
logging.getLogger('apscheduler').setLevel(logging.INFO)
logging.getLogger('apscheduler').addHandler(file_handle)
# logging.getLogger('apscheduler')
# 输出日志



from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime
dt = datetime.now()


def tick():
    print(f'Tick! The time is: {dt.strftime("%Y-%m-%d %H:%M:%S %f")}')


if __name__ == '__main__':
    sched = BlockingScheduler()
    # sched.add_job(tick, CronTrigger.from_crontab("0/4 * * * *"))
    sched.add_job(tick, 'interval', seconds=2)
    sched.start()
    # 0 / 4 * * * * ?
    # CronTrigger 不支持秒
```

