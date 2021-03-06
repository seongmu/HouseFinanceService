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
  `month` varchar(255) DEFAULT NULL,
  `nonghyup` bigint(20) DEFAULT NULL,
  `shinhan` bigint(20) DEFAULT NULL,
  `woori` bigint(20) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 금융기관 테이블 생성
CREATE TABLE `institute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `institute_code` varchar(255) DEFAULT NULL,
  `institute_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 롤 테이블 생성
CREATE TABLE `roles` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 유저랑 매핑된 롤 테이블 생성
CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 유저 테이블 생성
CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;