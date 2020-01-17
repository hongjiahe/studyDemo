/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2020-01-17 11:09:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_template_aop
-- ----------------------------
DROP TABLE IF EXISTS `t_template_aop`;
CREATE TABLE `t_template_aop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_code` varchar(255) NOT NULL,
  `pos_action` text,
  `pre_action` text,
  `method_name` varchar(255) DEFAULT NULL,
  `js_or_java` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_template_aop
-- ----------------------------
INSERT INTO `t_template_aop` VALUES ('1', 'user_import', 'com.hohe.importExcel.javaaction.UserAction', null, 'getDataByTemplateCode', 'java');
INSERT INTO `t_template_aop` VALUES ('2', 'user_import', null, 'com.hohe.importExcel.javaaction.UserSaveAction', 'actionSaveDate', 'java');
