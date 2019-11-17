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
-- Table structure for table `table_summary`
--

DROP TABLE IF EXISTS `table_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `table_summary` (
  `bizId` int(11) NOT NULL COMMENT '业务编号',
  `tbId` int(11) NOT NULL COMMENT '表单编号',
  `tableName` varchar(45) NOT NULL COMMENT '业务表单名称',
  `wfId` int(11) DEFAULT NULL COMMENT '流程编号',
  `title` varchar(100) NOT NULL COMMENT '业务名称',
  `createdUserId` int(11) NOT NULL COMMENT '业务创建人编号',
  `createdUserName` varchar(45) NOT NULL COMMENT '业务创建人名称',
  `createdOrgId` int(11) NOT NULL COMMENT '业务创建人所在组织编号',
  `createdOrgName` varchar(45) NOT NULL COMMENT '业务创建人所在组织名称',
  `curUserId` varchar(45) DEFAULT NULL COMMENT '当前办理人编号',
  `curUserName` varchar(45) DEFAULT NULL COMMENT '当前办理人名称',
  `createdDate` datetime NOT NULL COMMENT '业务创建时间',
  `modifiedDate` datetime DEFAULT NULL COMMENT '业务修改时间',
  `finishedDate` datetime DEFAULT NULL COMMENT '业务完成时间',
  `status` varchar(10) NOT NULL COMMENT '业务办理状态',
  `action` varchar(20) DEFAULT NULL COMMENT '业务触发事件',
  `summaryId` int(11) NOT NULL AUTO_INCREMENT COMMENT '概述编号',
  `defId` int(11) DEFAULT NULL COMMENT '用户表单自定义',
  PRIMARY KEY (`summaryId`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务概述表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-24 16:11:04
