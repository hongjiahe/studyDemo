/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2020-01-17 11:09:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_improt_template
-- ----------------------------
DROP TABLE IF EXISTS `t_improt_template`;
CREATE TABLE `t_improt_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TABLE_NAME` varchar(255) DEFAULT NULL COMMENT '表名称',
  `TEMPLATE_CODE` varchar(255) DEFAULT NULL COMMENT '模板名称',
  `TEMPLATE_NAME_CH` varchar(255) DEFAULT NULL COMMENT '模板名称中文',
  `COLS_NAME` varchar(255) DEFAULT NULL COMMENT '数据库表字段',
  `COLS_TYPE` varchar(255) DEFAULT NULL COMMENT '数据库字段类型',
  `SORT` int(10) DEFAULT NULL COMMENT '排序',
  `COLS_LABLE_NAME` varchar(255) DEFAULT NULL COMMENT '字段中文',
  `FORMAT` varchar(255) DEFAULT NULL COMMENT '字段格式化',
  `IS_KEY` int(11) DEFAULT NULL,
  `IS_CHANGE_VALUE` int(11) DEFAULT NULL,
  `IS_DEFAULT_COLS` int(11) DEFAULT NULL,
  `ROW_NUM` int(2) DEFAULT NULL COMMENT '行号',
  `COL_NUM` int(2) DEFAULT NULL COMMENT '列号',
  `IS_SHOW` int(2) DEFAULT NULL COMMENT '是否显示列',
  `NOT_INSERT` int(2) DEFAULT NULL,
  `IS_NOTNULL` int(11) DEFAULT NULL,
  `IS_SAVE` int(11) DEFAULT NULL,
  `BILLTEMPLATE_ID` varchar(255) DEFAULT NULL,
  `DEFAULT_VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_improt_template
-- ----------------------------
INSERT INTO `t_improt_template` VALUES ('1', 't_user', 'user_import', '用户名称', 'user_name', 'String', '1', '用户名称', null, null, null, null, null, null, null, null, '0', '1', null, null);
INSERT INTO `t_improt_template` VALUES ('2', 't_user', 'user_import', '密码', 'password', 'String', '2', '密码', null, null, null, null, null, null, null, null, '0', '1', null, null);
INSERT INTO `t_improt_template` VALUES ('3', 't_user', 'user_import', '手机号码', 'phone', 'int', '3', '手机号码', null, null, null, null, null, null, null, null, '0', '1', null, null);
INSERT INTO `t_improt_template` VALUES ('4', 't_user', 'user_import', '创建人', 'create_name', 'String', '4', '创建人', null, null, null, '1', null, null, '1', null, '0', '1', null, null);
