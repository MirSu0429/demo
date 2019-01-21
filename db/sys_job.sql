/*
Navicat MySQL Data Transfer

Source Server         : my
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : lenos

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2019-01-21 11:49:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `id` varchar(32) NOT NULL,
  `job_name` varchar(255) NOT NULL COMMENT '描述任务',
  `cron` varchar(255) NOT NULL COMMENT '任务表达式',
  `status` tinyint(1) NOT NULL COMMENT '状态:0未启动false/1启动true',
  `clazz_path` varchar(255) NOT NULL COMMENT '任务执行方法',
  `job_desc` varchar(255) DEFAULT NULL COMMENT '其他描述',
  `create_by` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES ('3467974bfe47405586079933b300a23f', 'a', '0/1 0 0 1/1 * ? ', '0', 'a', 's', 'acfc0e9232f54732a5d9ffe9071bf572', '2019-01-04 15:25:15', 'acfc0e9232f54732a5d9ffe9071bf572', '2019-01-04 15:27:05');
INSERT INTO `sys_job` VALUES ('55147ebdf2f611e7a4fe201a068c6482', '测试定时demo1', '0/5 * * * * ?', '0', 'com.len.core.quartz.CustomQuartz.JobDemo1', '测试定时demo1', 'acfc0e9232f54732a5d9ffe9071bf572', '2018-01-07 12:30:00', 'acfc0e9232f54732a5d9ffe9071bf572', '2019-01-21 08:59:45');
INSERT INTO `sys_job` VALUES ('ab648a22f38d11e7aca0201a068c6482', '任务demo2', '0 0/1 * * * ?', '0', 'com.len.core.quartz.CustomQuartz.JobDemo2', '任务demo2', 'acfc0e9232f54732a5d9ffe9071bf572', '2018-01-07 17:32:36', 'acfc0e9232f54732a5d9ffe9071bf572', '2019-01-04 15:27:26');
