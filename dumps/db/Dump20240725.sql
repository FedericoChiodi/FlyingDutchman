CREATE DATABASE  IF NOT EXISTS `flyingdutchmandb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `flyingdutchmandb`;
-- MySQL dump 10.13  Distrib 8.0.34, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: flyingdutchmandb
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `AUCTION`
--

DROP TABLE IF EXISTS `AUCTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUCTION` (
  `auctionID` bigint NOT NULL AUTO_INCREMENT,
  `opening_timestamp` timestamp NOT NULL,
  `closing_timestamp` timestamp NULL DEFAULT NULL,
  `is_product_sold` varchar(1) NOT NULL,
  `deleted` varchar(1) NOT NULL DEFAULT 'N',
  `productID` bigint NOT NULL,
  PRIMARY KEY (`auctionID`),
  KEY `productID` (`productID`),
  KEY `is_product_sold` (`is_product_sold`),
  CONSTRAINT `AUCTION_ibfk1` FOREIGN KEY (`productID`) REFERENCES `PRODUCT` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUCTION`
--

LOCK TABLES `AUCTION` WRITE;
/*!40000 ALTER TABLE `AUCTION` DISABLE KEYS */;
INSERT INTO `AUCTION` VALUES (1,'2023-07-05 12:00:00','2023-07-05 12:00:00','Y','Y',1),(2,'2023-07-05 12:05:00','2023-07-05 12:28:40','Y','N',2),(3,'2023-06-04 06:34:12','2023-06-05 06:30:17','Y','N',3),(4,'2023-05-05 08:45:33','2023-05-05 17:02:45','Y','N',4),(5,'2023-05-06 10:00:00','2023-05-06 10:50:00','N','N',5),(6,'2023-05-07 11:00:40','2023-05-07 15:52:07','N','N',5),(7,'2023-05-27 10:00:00','0000-00-00 00:00:00','Y','N',6),(8,'2023-05-06 12:09:21',NULL,'N','N',5);
/*!40000 ALTER TABLE `AUCTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATEGORY`
--

DROP TABLE IF EXISTS `CATEGORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CATEGORY` (
  `categoryID` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  PRIMARY KEY (`categoryID`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATEGORY`
--

LOCK TABLES `CATEGORY` WRITE;
/*!40000 ALTER TABLE `CATEGORY` DISABLE KEYS */;
INSERT INTO `CATEGORY` VALUES (4,'Hobby e tempo libero'),(3,'Mobili'),(2,'Pesca sportiva'),(1,'Premium');
/*!40000 ALTER TABLE `CATEGORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ORDER`
--

DROP TABLE IF EXISTS `ORDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ORDER` (
  `orderID` bigint NOT NULL AUTO_INCREMENT,
  `order_time` timestamp NOT NULL,
  `selling_price` decimal(24,2) NOT NULL,
  `bought_from_threshold` varchar(1) NOT NULL DEFAULT 'N',
  `userID` bigint NOT NULL,
  `productID` bigint NOT NULL,
  PRIMARY KEY (`orderID`),
  UNIQUE KEY `productID` (`productID`),
  KEY `ORDER_ibfk1` (`userID`),
  CONSTRAINT `ORDER_ibfk1` FOREIGN KEY (`userID`) REFERENCES `USER` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ORDER_ibfk2` FOREIGN KEY (`productID`) REFERENCES `PRODUCT` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ORDER`
--

LOCK TABLES `ORDER` WRITE;
/*!40000 ALTER TABLE `ORDER` DISABLE KEYS */;
INSERT INTO `ORDER` VALUES (1,'2023-07-05 12:30:00',55.00,'N',1,2),(2,'2023-06-05 06:34:12',100.00,'N',2,3),(3,'2023-05-05 17:04:17',150.00,'N',2,4),(4,'2023-05-26 16:00:00',20.00,'Y',1,6);
/*!40000 ALTER TABLE `ORDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRODUCT`
--

DROP TABLE IF EXISTS `PRODUCT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRODUCT` (
  `productID` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(200) NOT NULL,
  `min_price` decimal(24,2) NOT NULL,
  `starting_price` decimal(24,2) NOT NULL,
  `current_price` decimal(24,2) NOT NULL,
  `image` varchar(128) NOT NULL DEFAULT '/home/sanpc/tomcat/webapps/Uploads/default.png',
  `deleted` varchar(1) NOT NULL DEFAULT 'N',
  `categoryID` bigint NOT NULL,
  `ownerID` bigint NOT NULL,
  PRIMARY KEY (`productID`),
  KEY `categoryID` (`categoryID`),
  KEY `ownerID` (`ownerID`),
  CONSTRAINT `PRODUCT_ibfk1` FOREIGN KEY (`categoryID`) REFERENCES `CATEGORY` (`categoryID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PRODUCT_ibfk2` FOREIGN KEY (`ownerID`) REFERENCES `USER` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRODUCT`
--

LOCK TABLES `PRODUCT` WRITE;
/*!40000 ALTER TABLE `PRODUCT` DISABLE KEYS */;
INSERT INTO `PRODUCT` VALUES (1,'Premium Membership',50.00,50.00,50.00,'/home/sanpc/tomcat/webapps/Uploads/Mario12/Premium Membership.png','Y',1,1),(2,'Canna da pesca in ottime condizioni',50.00,250.00,55.00,'/home/sanpc/tomcat/webapps/Uploads/JohnDoe/Canna da pesca in ottime condizioni.png','N',2,3),(3,'Tavolo in legno massello fatto a mano',100.00,500.00,100.00,'/home/sanpc/tomcat/webapps/Uploads/Mario12/Tavolo in legno massello fatto a mano.png','N',3,1),(4,'Scacchiera in marmo',75.00,200.00,150.00,'/home/sanpc/tomcat/webapps/Uploads/Giulia88/Scacchiera in marmo.png','N',3,3),(5,'Racchetta da ping pong',5.00,25.00,24.00,'/home/sanpc/tomcat/webapps/Uploads/JohnDoe/Racchetta da ping pong.png','N',4,3),(6,'Sedia da Giardino',15.00,50.00,20.00,'/home/sanpc/tomcat/webapps/Uploads/Giulia88/Sedia da Giardino.png','N',3,3);
/*!40000 ALTER TABLE `PRODUCT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `THRESHOLD`
--

DROP TABLE IF EXISTS `THRESHOLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `THRESHOLD` (
  `thresholdID` bigint NOT NULL AUTO_INCREMENT,
  `price` decimal(24,2) NOT NULL,
  `reservation_date` timestamp NOT NULL,
  `userID` bigint NOT NULL,
  `auctionID` bigint NOT NULL,
  PRIMARY KEY (`thresholdID`),
  KEY `userID` (`userID`),
  KEY `auctionID` (`auctionID`),
  CONSTRAINT `THRESHOLD_ibfk1` FOREIGN KEY (`userID`) REFERENCES `USER` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `THRESHOLD_ibfk2` FOREIGN KEY (`auctionID`) REFERENCES `AUCTION` (`auctionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `THRESHOLD`
--

LOCK TABLES `THRESHOLD` WRITE;
/*!40000 ALTER TABLE `THRESHOLD` DISABLE KEYS */;
INSERT INTO `THRESHOLD` VALUES (1,15.00,'2023-07-05 11:20:00',4,1),(2,20.00,'0000-00-00 00:00:00',1,7);
/*!40000 ALTER TABLE `THRESHOLD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER` (
  `userID` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `firstname` varchar(40) NOT NULL,
  `surname` varchar(40) NOT NULL,
  `birthdate` date NOT NULL,
  `address` varchar(40) NOT NULL,
  `civic_number` smallint NOT NULL,
  `cap` smallint NOT NULL,
  `city` varchar(40) NOT NULL,
  `state` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `cel_number` varchar(40) NOT NULL,
  `role` varchar(40) NOT NULL,
  `deleted` varchar(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`userID`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
INSERT INTO `USER` VALUES (1,'Mario12','password','Mario','Rossi','1980-10-10','Via Mazzini',12,32767,'Venezia','Italia','mario12@gmail.com','+393458874500','SuperAdmin','N'),(2,'Giulia88','password','Giulia','Bianchi','1995-05-15','Via Roma',25,32767,'Firenze','Italia','giulia88@gmail.com','+393331234567','Default','N'),(3,'JohnDoe','password','John','Doe','1988-07-20','123 Main St',10,12345,'New York','USA','johndoe@example.com','+12015551234','Default','N'),(4,'Laura87','password','Laura','Verdi','2000-12-01','Via Garibaldi',8,20121,'Venezia','Italia','laura87@example.com','+393299876543','Default','N');
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-25 16:23:00
