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
-- Table structure for table `element_library`
--

DROP TABLE IF EXISTS `element_library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `element_library` (
  `emId` int(11) NOT NULL AUTO_INCREMENT COMMENT '元素编号',
  `labelName` varchar(20) NOT NULL COMMENT '元素标签名称',
  `fieldName` varchar(45) NOT NULL COMMENT '元素字段名称',
  `hiddenFieldName` varchar(45) DEFAULT NULL COMMENT '元素隐藏字段名',
  `functionName` varchar(45) DEFAULT NULL COMMENT '元素方法名称',
  `status` varchar(10) NOT NULL COMMENT '状态',
  `createdDate` datetime NOT NULL COMMENT '创建时间',
  `level` varchar(10) NOT NULL COMMENT '级别（系统级、自定义）',
  `fieldType` varchar(10) NOT NULL DEFAULT '输入框' COMMENT '字段类型',
  `fieldDataType` varchar(10) NOT NULL DEFAULT '字符串' COMMENT '字段数据类型',
  `dataContent` varchar(100) DEFAULT NULL COMMENT '值关联表值（适用于下拉框、多选框）',
  `length` int(11) DEFAULT NULL COMMENT '字段长度，仅对字符串类型有效',
  `functionBelongTo` varchar(10) DEFAULT NULL COMMENT '方法隶属于标签或元素',
  PRIMARY KEY (`emId`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='元素库';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-18  8:53:42
