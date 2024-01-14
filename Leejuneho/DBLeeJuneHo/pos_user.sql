-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: pos
-- ------------------------------------------------------
-- Server version	8.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userCode` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(20) NOT NULL,
  `userRegDate` date NOT NULL,
  `userGender` char(2) NOT NULL,
  `phoneHeader` varchar(3) NOT NULL,
  `phoneMiddle` varchar(4) NOT NULL,
  `phoneTail` varchar(4) NOT NULL,
  `userStatus` tinyint NOT NULL,
  `membership` varchar(100) NOT NULL,
  `userStartDate` date NOT NULL,
  `userEndDate` date NOT NULL,
  PRIMARY KEY (`userCode`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'송상엽','2024-01-01','남자','010','1234','5678',1,'프리미엄 통합 회원권 [3개월] + 운동복 + 개인락커                             * 가격  :  500,000원','2023-10-10','2023-11-10'),(2,'이준호','2024-01-02','남자','010','2345','6789',1,'프리미엄 회원권 [1개월] + 운동복 + 개인락커                                 * 가격  :  200,000원','2023-10-10','2023-11-10'),(3,'김규민','2024-01-03','남자','010','3456','7890',0,'프리미엄 통합 회원권 [3개월] + 운동복 + 개인락커                             * 가격  :  500,000원','2023-10-10','2023-11-10'),(4,'최기근','2024-01-04','여자','010','1233','5678',0,'프리미엄 회원권 [1개월] + 운동복 + 개인락커                                 * 가격  :  200,000원','2023-10-10','2023-11-10'),(15,'박진규','2024-01-11','남자','010','1234','1234',1,'프리미엄 통합 회원권 [3개월] + 운동복 + 개인락커                             * 가격  :  500,000원','2024-01-11','2024-04-11'),(16,'손흥민','2024-01-11','남자','010','5478','1578',1,'프리미엄 통합 회원권 [3개월] + 운동복 + 개인락커                             * 가격  :  500,000원','2024-01-01','2024-04-01'),(17,'이강인','2024-01-11','남자','010','1245','8568',1,'프리미엄 회원권 [1개월] + 운동복 + 개인락커                                 * 가격  :  200,000원','2023-12-26','2024-01-26');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-12 15:21:25
