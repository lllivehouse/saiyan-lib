CREATE TABLE `scheduler_plus_log`  (
                                       `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理表主键',
                                       `scheduler_id` varchar(20) NOT NULL COMMENT '定时器id',
                                       `failed` int unsigned NOT NULL DEFAULT 0 COMMENT '是否失败 1:失败',
                                       `info` varchar(100) NOT NULL DEFAULT '' COMMENT '日志信息',
                                       `run_start_time` datetime(3) COMMENT '运行开始时间',
                                       `run_end_time` datetime(3) COMMENT '运行结束时间',
                                       `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                                       `updated_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
                                       `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                       PRIMARY KEY (`id`),
                                       KEY `idx_scheduler_id` (`scheduler_id`),
                                       KEY `idx_failed` (`failed`),
                                       KEY `idx_run_start_time` (`run_start_time`),
                                       KEY `idx_run_end_time` (`run_end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci comment='定时任务运行日志';