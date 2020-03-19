## 1.逆向工具的使用方法

```shell
sqlacodegen --outfile project_path/mod.py mysql+pymysql://Username:Password@IP/database_name

sqlacodegen --outfile mod.py mysql+pymysql://root:root@127.0.0.1/foodie-shop-dev

连接测试语句
connection-test-query: SELECT 1
常用选项： --help ！

usage: sqlacodegen [-h] [--version] [--schema SCHEMA] [--tables TABLES]
                   [--noviews] [--noindexes] [--noconstraints] [--nojoined]
                   [--noinflect] [--noclasses] [--outfile OUTFILE]
                   [url]

Generates SQLAlchemy model code from an existing database.

positional arguments:
  url                SQLAlchemy url to the database

optional arguments:
  -h, --help         show this help message and exit       # 显示此帮助消息并退出
  --version          print the version number and exit     # 打印版本号并退出
  --schema SCHEMA    load tables from an alternate schema  # 从备用模式加载表
  --tables TABLES    tables to process (comma-separated, default: all)  # 要处理的表（逗号分隔，默认值：全部）
  --noviews          ignore views        # 忽略视图
  --noindexes        ignore indexes      # 忽略索引
  --noconstraints    ignore constraints  # 忽略约束
  --nojoined         don't autodetect joined table inheritance            # 不要自动检测连接表继承
  --noinflect        don't try to convert tables names to singular form   # 不要尝试将表名转换为单数形式
  --noclasses        don't generate classes, only tables                  # 不生成类，只生成表
  --outfile OUTFILE  file to write output to (default: stdout)            # 将输出写入的文件（默认值：stdout）
```

## 2. sql查询范例

```python
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

engine = create_engine("mysql+pymysql://root:root@127.0.0.1/foodie-shop-dev", max_overflow=5)
Session = sessionmaker(bind=engine)
session = Session()

res = session.execute("""SELECT
                            ic.comment_level as commentLevel,
                            ic.content as content,
                            ic.sepc_name as specName,
                            ic.created_time as createdTime,
                            u.face as userFace,
                            u.nickname as nickname
                        from items_comments ic
                        LEFT JOIN users u
                        ON
                            ic.user_id = u.id
                        WHERE
                            ic.item_id = 'cake-1001'
                        AND
                            ic.comment_level = 1""")

print(type(res.fetchone()))		#<class 'sqlalchemy.engine.result.RowProxy'>
print(dict(res.fetchone()))     # 注意这里 返回字典
print(type(res))							# <class 'sqlalchemy.engine.result.ResultProxy'>
```

## 3. 参考

> https://www.cnblogs.com/kirito-c/p/10269485.html