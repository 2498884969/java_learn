-- noinspection SqlNoDataSourceInspectionForFile
-- https://blog.csdn.net/foreverling_ling/article/details/103195945
-- https://blog.csdn.net/lanyang123456/article/details/88092961

-- 表 broker_message.broker_message 结构
CREATE TABLE IF NOT EXISTS `broker_message` (
  `message_id` varchar(128) NOT NULL,
  `message` varchar(4000),
  `try_count` int(4) DEFAULT 0,
  `status` varchar(10) DEFAULT '',
  `next_retry` timestamp NOT NULL DEFAULT '1970-01-01 08:00:01',
  `create_time` timestamp NOT NULL DEFAULT '1970-01-01 08:00:01',
  `update_time` timestamp NOT NULL DEFAULT '1970-01-01 08:00:01',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;