-- DB 생성
create database test;

-- 사용 DB 변경
use test;

-- 주택금융지원현황 테이블 생성
CREATE TABLE `hou_fnc_supp_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `citi` bigint(20) DEFAULT NULL,
  `etc` bigint(20) DEFAULT NULL,
  `hana` bigint(20) DEFAULT NULL,
  `jutaeck` bigint(20) DEFAULT NULL,
  `ke` bigint(20) DEFAULT NULL,
  `kookmin` bigint(20) DEFAULT NULL,
  `month` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `nonghyup` bigint(20) DEFAULT NULL,
  `shinhan` bigint(20) DEFAULT NULL,
  `woori` bigint(20) DEFAULT NULL,
  `year` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=latin1;

-- 금융기관 테이블 생성
CREATE TABLE `institute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `institute_code` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `institute_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- 롤 테이블 생성
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- 유저랑 매핑된 롤 테이블 생성
CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 유저 테이블 생성
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `userid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKjyjiwnaabof8kpd0gclhcj2lh` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
