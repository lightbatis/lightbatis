CREATE TABLE `member`  (
           `id` bigint(11) NOT NULL AUTO_INCREMENT,
           `create_by` bigint(11) NULL,
           `create_time` datetime(0) NULL,
           `kind_id` int(0) NULL,
           `member_name` varchar(255) NULL,
           `revision` int(255) NULL,
           `staff_id` bigint(11) NULL,
           `staff_name` varchar(255) NULL,
           `status` int(255) NULL,
           `update_by` bigint(11) NULL,
           `update_time` datetime(0) NULL,
           `verify_status` varchar(255) NULL,
           PRIMARY KEY (`id`)
);