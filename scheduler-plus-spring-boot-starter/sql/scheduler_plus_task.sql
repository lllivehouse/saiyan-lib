CREATE TABLE `scheduler_plus_task`  (
                                        `scheduler_id` varchar(20) NOT NULL COMMENT '定时器id',
                                        `scheduled_mode` int unsigned NOT NULL DEFAULT 0 COMMENT '调度模式 0:单实例 1:全实例',
                                        `job_name` varchar(30) NOT NULL DEFAULT '' COMMENT '继承自SchedulerPlusJob的bean类名',
                                        `cron_expression` varchar(30) NOT NULL DEFAULT '' COMMENT 'cron表达式',
                                        `task_args` varchar(300) NOT NULL DEFAULT '' COMMENT '任务入参 map对象序列化字符串',
                                        `task_desc` varchar(50) NOT NULL DEFAULT '' COMMENT '任务描述',
                                        `task_status` int unsigned NOT NULL DEFAULT 0 COMMENT '任务状态 0:未执行 1:执行中 2:已执行',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                        PRIMARY KEY (`scheduler_id`),
                                        KEY `idx_task_status` (`task_status`),
                                        KEY `idx_deleted` (`deleted`),
                                        KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci comment='定时任务表';