-- MySQL dump 10.13  Distrib 8.0.11, for Win64 (x86_64)
--
-- Host: localhost    Database: workflow
-- ------------------------------------------------------
-- Server version	8.0.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `table_element`
--

DROP TABLE IF EXISTS `table_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `table_element` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tbId` int(11) NOT NULL COMMENT '表单编号',
  `newLabelName` varchar(100) DEFAULT NULL COMMENT '字段标签名称',
  `newFieldName` varchar(100) DEFAULT NULL COMMENT '字段名称名称',
  `newFunctionName` varchar(50) DEFAULT NULL COMMENT '事件名称',
  `functionBelongTo` varchar(10) DEFAULT NULL COMMENT '事件方法所属（标签或元素）',
  `newHiddenFieldName` varchar(50) DEFAULT NULL COMMENT '隐藏项',
  `rowes` int(11) NOT NULL DEFAULT '1' COMMENT '跨行',
  `cols` int(11) NOT NULL DEFAULT '1' COMMENT '跨列',
  `width` int(11) DEFAULT NULL COMMENT '宽度',
  `scope` varchar(10) NOT NULL COMMENT '位置：表头、表体、表尾',
  `formula` varchar(50) DEFAULT NULL COMMENT '计算公式',
  `newDataContent` varchar(100) DEFAULT NULL COMMENT '多选数据的信息',
  `newFieldType` varchar(10) NOT NULL DEFAULT '输入框' COMMENT '字段操作类型',
  `newFieldDataType` varchar(10) DEFAULT 'String' COMMENT '字段类型',
  `seq` int(11) NOT NULL COMMENT '顺序号',
  `list` varchar(10) DEFAULT '无效' COMMENT '显示在列表上',
  `constraint` varchar(10) DEFAULT NULL COMMENT '必输项',
  `newLength` int(11) DEFAULT NULL COMMENT '对字段为String的生效。',
  `defaultValue` varchar(45) DEFAULT NULL COMMENT '字段默认初始值',
  `defaultValueFrom` varchar(100) DEFAULT NULL COMMENT '初始值来源，目前仅支持根据指定的类的属性获取',
  `status` varchar(10) NOT NULL DEFAULT '有效',
  `createdDate` datetime NOT NULL COMMENT '创建时间',
  `stbId` int(11) DEFAULT NULL COMMENT '子表单的编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=340 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='表单元素';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-21 11:32:04
