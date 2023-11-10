CREATE TABLE `distributed_lock` (
                                       `lock_key` varchar(36) NOT NULL,
                                       `region` varchar(100) NOT NULL,
                                       `client_id` varchar(36),
                                       `created_date` datetime NOT NULL,
                                       constraint pk_lock primary key (`lock_key`, `region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;