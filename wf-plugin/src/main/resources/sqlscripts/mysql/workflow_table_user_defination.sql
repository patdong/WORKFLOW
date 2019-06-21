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
-- Table structure for table `table_user_defination`
--

DROP TABLE IF EXISTS `table_user_defination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `table_user_defination` (
  `defId` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `tbId` int(11) NOT NULL COMMENT '表单编号',
  `wfId` int(11) DEFAULT NULL COMMENT '流程编号',
  `userId` int(11) NOT NULL COMMENT '自定义用户编号',
  `createdDate` datetime NOT NULL COMMENT '创建时间',
  `tableName` varchar(50) NOT NULL COMMENT '表单名称',
  `notification1` varchar(45) DEFAULT NULL COMMENT '提醒方式1',
  `notification2` varchar(45) DEFAULT NULL COMMENT '提醒方式2',
  `notification3` varchar(45) DEFAULT NULL COMMENT '提醒方式3',
  `action1` varchar(45) DEFAULT NULL COMMENT '协同操作1',
  `action2` varchar(45) DEFAULT NULL COMMENT '协同操作2',
  `action3` varchar(45) DEFAULT NULL COMMENT '协同操作3',
  `type` varchar(20) NOT NULL COMMENT '业务类型：使用默认流程、设置自定义流程、不使用流程',
  PRIMARY KEY (`defId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户自定义表单相关属性';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-21 11:32:03
