-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: bloomingshop
-- ------------------------------------------------------
-- Server version	8.4.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address_tbl`
--

DROP TABLE IF EXISTS `address_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address_tbl` (
  `address_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `postcode` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `detail_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`address_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `address_tbl_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address_tbl`
--

LOCK TABLES `address_tbl` WRITE;
/*!40000 ALTER TABLE `address_tbl` DISABLE KEYS */;
INSERT INTO `address_tbl` VALUES (2,28,'06101','서울 강남구 학동로42길 67-12','101호','2025-01-19 01:06:19',NULL),(3,29,'06101','너네집','','2025-01-19 01:21:17',NULL),(4,30,'06774','서울 서초구 강남대로 45-2','303호','2025-01-19 22:27:38',NULL),(5,32,'06035','서울 강남구 가로수길 5','206호','2025-01-20 01:27:24',NULL),(6,33,'06036','서울 강남구 신사동 541-7','','2025-01-20 02:12:49',NULL);
/*!40000 ALTER TABLE `address_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_tbl`
--

DROP TABLE IF EXISTS `cart_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_tbl` (
  `cart_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` int NOT NULL,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product_tbl` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_tbl`
--

LOCK TABLES `cart_tbl` WRITE;
/*!40000 ALTER TABLE `cart_tbl` DISABLE KEYS */;
INSERT INTO `cart_tbl` VALUES (147,28,11,1,'2025-02-09 13:35:26');
/*!40000 ALTER TABLE `cart_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'꽃다발'),(2,'꽃바구니'),(3,'동양란'),(4,'서양란'),(5,'근조화환'),(6,'축화화환'),(7,'관엽화분');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guest_cart`
--

DROP TABLE IF EXISTS `guest_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guest_cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `guest_id` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guest_cart`
--

LOCK TABLES `guest_cart` WRITE;
/*!40000 ALTER TABLE `guest_cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `guest_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `order_item_id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order_tbl` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product_tbl` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,41,8,3,49000.00,147000.00,NULL),(2,41,9,1,72000.00,72000.00,NULL),(3,42,7,5,82000.00,410000.00,NULL),(4,43,22,1,97000.00,97000.00,NULL),(5,44,16,4,96000.00,384000.00,NULL),(6,45,14,1,58000.00,58000.00,NULL),(7,46,14,1,58000.00,58000.00,NULL),(8,47,19,4,53500.00,214000.00,NULL),(9,48,22,1,97000.00,97000.00,NULL),(10,48,18,3,144000.00,432000.00,NULL),(11,48,21,2,67900.00,135800.00,NULL),(12,48,13,4,66000.00,264000.00,NULL),(13,49,14,1,58000.00,58000.00,NULL),(14,49,15,1,60000.00,60000.00,NULL),(15,50,1,3,70000.00,210000.00,NULL),(16,50,4,2,55000.00,110000.00,NULL),(20,55,15,1,60000.00,60000.00,NULL),(21,56,3,6,80000.00,480000.00,NULL),(22,57,18,1,144000.00,144000.00,NULL),(23,58,6,2,69000.00,138000.00,NULL),(24,59,20,1,86400.00,86400.00,NULL),(25,60,5,1,51000.00,51000.00,NULL),(26,61,8,17,49000.00,833000.00,NULL),(27,62,7,1,82000.00,82000.00,NULL),(28,63,13,2,66000.00,132000.00,NULL),(29,64,7,3,82000.00,246000.00,NULL),(30,65,19,4,53500.00,214000.00,NULL),(31,66,18,1,144000.00,144000.00,NULL),(32,66,1,3,70000.00,210000.00,NULL),(33,66,3,4,80000.00,320000.00,NULL),(34,67,8,1,49000.00,49000.00,NULL),(35,68,9,1,72000.00,72000.00,NULL),(36,68,5,3,51000.00,153000.00,NULL),(37,69,16,2,96000.00,192000.00,NULL),(38,70,14,1,58000.00,58000.00,NULL),(39,71,31,1,52800.00,52800.00,NULL),(40,72,30,1,53400.00,53400.00,NULL);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_tbl`
--

DROP TABLE IF EXISTS `order_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_tbl` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `guest_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `delivery_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `payment_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  `shipping_address_non_member` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `deleted_flag` tinyint(1) DEFAULT '0',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `delivery_status_updated` timestamp NULL DEFAULT NULL,
  `payment_status_updated` timestamp NULL DEFAULT NULL,
  `deleted_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  KEY `address_id` (`address_id`),
  CONSTRAINT `order_tbl_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_tbl_ibfk_2` FOREIGN KEY (`address_id`) REFERENCES `address_tbl` (`address_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_tbl`
--

LOCK TABLES `order_tbl` WRITE;
/*!40000 ALTER TABLE `order_tbl` DISABLE KEYS */;
INSERT INTO `order_tbl` VALUES (41,NULL,'guest-z1yz6q5m0',219000.00,'배송 준비 중','결제 진행 중',NULL,'06036 서울 강남구 가로수길 10 205호','CARD',1,'2025-01-31 08:50:11',NULL,'2025-01-31 09:44:17','2025-01-31 09:44:17'),(42,28,NULL,410000.00,'배송 준비 중','결제 진행 중',2,NULL,'BANK_TRANSFER',1,'2025-01-31 08:51:23',NULL,'2025-02-03 10:59:45','2025-02-03 10:59:45'),(43,NULL,'guest-z1yz6q5m0',97000.00,'배송 준비 중','결제 진행 중',NULL,'02880 서울 성북구 성북동1가 35-17','CARD',1,'2025-01-31 09:24:41',NULL,'2025-01-31 09:44:18','2025-01-31 09:44:18'),(44,NULL,'guest-z1yz6q5m0',384000.00,'배송 준비 중','결제 진행 중',NULL,'08775 서울 관악구 관천로 15-2','BANK_TRANSFER',1,'2025-01-31 09:25:28',NULL,'2025-01-31 09:44:19','2025-01-31 09:44:19'),(45,NULL,'guest-z1yz6q5m0',58000.00,'배송 완료','결제 완료',NULL,'63527 제주특별자치도 서귀포시 안덕면 덕수리 888-1','BANK_TRANSFER',0,'2025-01-31 09:44:47',NULL,'2025-02-04 16:22:11',NULL),(46,NULL,'guest-z1yz6q5m0',58000.00,'배송 준비 중','결제 완료',NULL,'27454 충북 충주시 신니면 수월1길 3','CARD',1,'2025-01-31 09:48:34',NULL,'2025-02-09 12:47:34','2025-02-09 12:47:34'),(47,28,NULL,214000.00,'배송 준비 중','결제 완료',2,NULL,'CARD',1,'2025-02-03 12:14:16',NULL,'2025-02-04 09:11:23','2025-02-04 09:11:23'),(48,28,NULL,928800.00,'배송 준비 중','결제 진행 중',2,NULL,'BANK_TRANSFER',1,'2025-02-03 12:14:41',NULL,'2025-02-09 12:47:29','2025-02-09 12:47:29'),(49,28,NULL,118000.00,'배송 준비 중','결제 완료',2,NULL,'CARD',0,'2025-02-03 12:14:54',NULL,'2025-02-03 12:14:58',NULL),(50,NULL,'guest-z1yz6q5m0',320000.00,'배송 준비 중','결제 완료',NULL,'24560 강원특별자치도 양구군 국토정중앙면 두무리 277','BANK_TRANSFER',0,'2025-02-03 12:15:51',NULL,'2025-02-07 11:48:01',NULL),(55,28,NULL,60000.00,'배송 완료','결제 완료',2,NULL,'CARD',0,'2025-02-03 14:29:42',NULL,'2025-02-04 09:00:16',NULL),(56,28,NULL,480000.00,'배송 준비 중','결제 진행 중',2,NULL,'BANK_TRANSFER',0,'2025-02-03 14:29:55',NULL,'2025-02-03 14:29:55',NULL),(57,28,NULL,144000.00,'배송 준비 중','결제 진행 중',2,NULL,'CARD',0,'2025-02-03 14:30:02',NULL,'2025-02-03 14:30:02',NULL),(58,28,NULL,138000.00,'배송 중','결제 완료',2,NULL,'CARD',0,'2025-02-03 14:30:13',NULL,'2025-02-04 09:00:19',NULL),(59,28,NULL,86400.00,'배송 준비 중','결제 완료',2,NULL,'BANK_TRANSFER',0,'2025-02-03 14:30:22',NULL,'2025-02-04 08:57:01',NULL),(60,28,NULL,51000.00,'배송 준비 중','결제 진행 중',2,NULL,'BANK_TRANSFER',1,'2025-02-03 14:30:39',NULL,'2025-02-07 12:55:08','2025-02-07 12:55:08'),(61,28,NULL,833000.00,'배송 준비 중','결제 완료',2,NULL,'CARD',1,'2025-02-04 16:25:15',NULL,'2025-02-04 16:25:24','2025-02-04 16:25:24'),(62,28,NULL,82000.00,'배송 완료','결제 완료',2,NULL,'BANK_TRANSFER',1,'2025-02-04 16:45:30',NULL,'2025-02-04 16:46:02','2025-02-04 16:46:02'),(63,28,NULL,132000.00,'배송 준비 중','결제 진행 중',2,NULL,'BANK_TRANSFER',0,'2025-02-07 13:01:46',NULL,'2025-02-07 13:01:46',NULL),(64,28,NULL,246000.00,'배송 준비 중','결제 진행 중',2,NULL,'CARD',0,'2025-02-09 11:16:48',NULL,'2025-02-09 11:16:48',NULL),(65,NULL,'guest-0o8tzshje',214000.00,'배송 준비 중','결제 진행 중',NULL,'48758 부산 동구 성남오길 5-1 207호','BANK_TRANSFER',1,'2025-02-09 12:42:19',NULL,'2025-02-09 13:00:29','2025-02-09 13:00:29'),(66,NULL,'guest-0o8tzshje',674000.00,'배송 준비 중','결제 완료',NULL,'08500 서울 금천구 가산동 535-128 2층','CARD',1,'2025-02-09 12:49:34',NULL,'2025-02-09 13:00:30','2025-02-09 13:00:30'),(67,NULL,'guest-0o8tzshje',49000.00,'배송 준비 중','결제 완료',NULL,'49358 부산 사하구 괴정동 1238-400','CARD',0,'2025-02-09 13:01:12',NULL,'2025-02-09 13:01:15',NULL),(68,NULL,'guest-0o8tzshje',225000.00,'배송 준비 중','결제 진행 중',NULL,'10922 경기 파주시 금촌동 122-10','CARD',0,'2025-02-09 13:48:09',NULL,'2025-02-09 13:48:09',NULL),(69,NULL,'guest-0o8tzshje',192000.00,'배송 준비 중','결제 완료',NULL,'12500 경기 양평군 서종면 상산현길 6','BANK_TRANSFER',0,'2025-02-09 13:48:49',NULL,'2025-02-09 13:48:56',NULL),(70,NULL,'guest-w6p8uxi70',58000.00,'배송 준비 중','결제 진행 중',NULL,'06035 서울 강남구 가로수길 5 202호','BANK_TRANSFER',0,'2025-02-26 17:07:54',NULL,'2025-02-26 17:07:54',NULL),(71,NULL,'guest-w6p8uxi70',52800.00,'배송 준비 중','결제 진행 중',NULL,'13355 경기 성남시 중원구 광명로 1','BANK_TRANSFER',0,'2025-03-04 07:09:42',NULL,'2025-03-04 07:09:42',NULL),(72,NULL,'guest-hnzu27ye2',53400.00,'배송 준비 중','결제 진행 중',NULL,'07614 서울 강서구 방화동 606-15','BANK_TRANSFER',0,'2025-03-04 07:45:48',NULL,'2025-03-04 07:45:48',NULL);
/*!40000 ALTER TABLE `order_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category` (
  `product_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`product_id`,`category_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `product_category_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_tbl` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `product_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,2),(17,2),(18,2),(19,2),(20,2),(21,2),(22,2),(23,2),(24,2),(25,2),(26,2),(27,2),(28,2),(29,2),(30,2),(31,3),(32,3),(33,3),(34,3),(37,4),(38,5),(35,6),(36,6);
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_tbl`
--

DROP TABLE IF EXISTS `product_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_tbl` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `size` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_tbl`
--

LOCK TABLES `product_tbl` WRITE;
/*!40000 ALTER TABLE `product_tbl` DISABLE KEYS */;
INSERT INTO `product_tbl` VALUES (1,'장미 꽃다발',70000.00,'일반','/images/488604f1-6d1c-4dff-a33b-f9addb0676a9.jpg','사랑과 열정을 상징하는 장미 꽃다발입니다.','2025-01-19 20:14:53','2025-01-22 20:13:43',0,NULL),(2,'장미 꽃다발[P}',89000.00,'프리미엄','/images/b624f2a7-86c8-424f-b485-2c9ec02ca97f.jpg','사랑과 열정을 상징하는 장미 꽃다발입니다.','2025-01-19 20:33:55','2025-01-22 18:12:00',0,NULL),(3,'백장미 혼합다발',80000.00,'일반','/images/9b15bdd3-f6b9-4f1d-84b9-ec1cf1ffd4a1.jpg','백장미혼합다발은 순수하고 고귀한 백장미와 다른 아름다운 꽃들이 어우러져, 사랑과 존경을 표현하는 완벽한 선물입니다. 고급스러운 디자인과 섬세한 꽃잎은 소중한 사람에게 특별한 감동을 선사할 것입니다. 이 꽃다발은 결혼식, 기념일, 또는 감사의 마음을 전하고 싶을 때 가장 적합한 선택입니다. 신뢰와 정직, 그리고 순수한 마음을 전하는 백장미로 특별한 순간을 더욱 빛내세요.','2025-01-19 22:46:19','2025-01-22 17:37:12',0,NULL),(4,'백합 꽃다발',55000.00,'일반','/images/2ef0ad72-ec75-446e-bdef-9d56e84897bd.jpg','백합은 순수하고 깨끗한 이미지로 사랑받는 꽃으로, 고요하고 세련된 아름다움을 상징합니다. 이 백합 꽃다발은 우아한 꽃잎과 부드러운 향기로 특별한 순간을 더욱 빛나게 만듭니다. 축하나 감사의 마음을 전할 때, 또는 사랑하는 사람에게 순수한 감정을 표현하고자 할 때 완벽한 선택입니다. 백합의 화사한 모습이 공간을 더욱 환하게 밝히며, 받는 이에게 마음의 평화와 행복을 선사합니다.','2025-01-19 23:14:16','2025-01-22 17:17:05',0,NULL),(5,'샤이니',51000.00,'일반','/images/50b7be40-4f8a-440e-b6a3-0b067cfcccd5.jpg','샤이니 꽃다발은 사랑과 감동을 전하는 아름다운 꽃다발입니다. 화려한 색감과 섬세한 꽃잎들이 조화를 이루어 특별한 날을 더욱 빛나게 만들어줍니다. 이 꽃다발은 기념일, 결혼식, 생일 등 중요한 순간에 가장 잘 어울립니다. 샤이니 꽃다발은 사랑하는 사람에게 감동을 선사하고, 오래도록 기억에 남을 순간을 만들어 줄 것입니다. 고급스러운 분위기와 함께 마음을 전할 수 있는 최고의 선택입니다.','2025-01-19 23:20:22','2025-01-22 17:02:25',0,NULL),(6,'샤이니[P]',69000.00,'프리미엄','/images/e6082f73-7fe7-4648-9491-43d6a2fee0d8.jpg','샤이니 꽃다발은 사랑과 감동을 전하는 아름다운 꽃다발입니다. 화려한 색감과 섬세한 꽃잎들이 조화를 이루어 특별한 날을 더욱 빛나게 만들어줍니다. 이 꽃다발은 기념일, 결혼식, 생일 등 중요한 순간에 가장 잘 어울립니다. 샤이니 꽃다발은 사랑하는 사람에게 감동을 선사하고, 오래도록 기억에 남을 순간을 만들어 줄 것입니다. 고급스러운 분위기와 함께 마음을 전할 수 있는 최고의 선택입니다.','2025-01-19 23:21:58','2025-01-22 18:12:17',0,NULL),(7,'카모마일',82000.00,'일반','/images/7a31c11d-eb74-45c0-b3e3-e950383c5956.jpg','카모마일은 순수하고 편안한 느낌을 주는 꽃으로, 사랑과 평화, 그리고 치유의 상징입니다. 그 특유의 순백색 꽃잎과 노란 중심이 어우러져 세련되면서도 따뜻한 분위기를 연출합니다. 카모마일 꽃다발은 스트레스를 완화하고 마음을 진정시키는 효과가 있어 선물용으로 많이 사용됩니다. 특히, 사랑하는 사람에게 편안한 마음을 전하고 싶은 순간에 완벽한 선택이 될 수 있습니다.\r\n\r\n카모마일 꽃다발은 심리적인 안정감을 주는 꽃으로, 다양한 감정의 변화를 치유하는 데 도움을 줄 수 있습니다. 또한, 자연 친화적인 느낌이 강해 자연을 사랑하는 사람에게도 적합한 선물입니다.','2025-01-19 23:27:25','2025-01-22 17:37:22',0,NULL),(8,'축하해',49000.00,'일반','/images/5142d3c3-3f51-4a2b-ba70-64792c8d627b.jpg','\"축하해 꽃다발\"은 특별한 날에 행복과 기쁨을 전하는 꽃다발입니다. 생동감 넘치는 색상의 꽃들이 어우러져, 소중한 순간을 더욱 빛나게 만들어 줍니다. 이 꽃다발은 축하의 의미를 담고 있어, 졸업식, 승진, 결혼, 출산 등 다양한 기념일에 완벽한 선물이 될 수 있습니다.\r\n\r\n각 꽃은 행복과 성공, 새로운 시작을 상징하며, 받은 사람에게 긍정적인 에너지를 전달합니다. \"축하해 꽃다발\"은 사랑과 존경의 마음을 함께 표현할 수 있는 아름다운 선물로, 어떤 축하의 순간에도 그 가치를 더할 것입니다.\r\n\r\n이 꽃다발을 통해, 중요한 순간을 함께 축하하고 특별한 사람에게 감동을 선사해보세요.','2025-01-19 23:31:39','2025-01-22 16:59:32',0,NULL),(9,'축하해[P]',72000.00,'프리미엄','/images/5549fd31-e8c1-42d5-97e1-800cb4cebe36.jpg','\"축하해 꽃다발\"은 특별한 날에 행복과 기쁨을 전하는 꽃다발입니다. 생동감 넘치는 색상의 꽃들이 어우러져, 소중한 순간을 더욱 빛나게 만들어 줍니다. 이 꽃다발은 축하의 의미를 담고 있어, 졸업식, 승진, 결혼, 출산 등 다양한 기념일에 완벽한 선물이 될 수 있습니다.\r\n\r\n각 꽃은 행복과 성공, 새로운 시작을 상징하며, 받은 사람에게 긍정적인 에너지를 전달합니다. \"축하해 꽃다발\"은 사랑과 존경의 마음을 함께 표현할 수 있는 아름다운 선물로, 어떤 축하의 순간에도 그 가치를 더할 것입니다.\r\n\r\n이 꽃다발을 통해, 중요한 순간을 함께 축하하고 특별한 사람에게 감동을 선사해보세요.','2025-01-19 23:32:01','2025-01-22 18:12:36',0,NULL),(10,'해바라기 꽃다발 ',67000.00,'일반','/images/5fa1b29e-2d95-47ef-9c9d-f0fe66db0cfd.jpg','\"해바라기 꽃다발\"은 밝고 활기찬 에너지를 선사하는 꽃다발입니다. 해바라기는 태양을 따라 자라는 특성 때문에 항상 밝고 긍정적인 느낌을 줍니다. 이 꽃다발은 사랑, 행복, 희망의 상징으로, 특별한 사람에게 힘과 용기를 불어넣어 줍니다.\r\n\r\n여름과 가을의 따뜻한 햇살을 닮은 해바라기는 누군가에게 응원의 메시지를 전달하기에 완벽한 선택입니다. 친구의 생일, 기념일, 새로운 시작을 축하하는 순간에 이 꽃다발을 선물하면 기쁨과 감동을 동시에 선사할 수 있습니다.\r\n\r\n\"해바라기 꽃다발\"은 그 자체로 밝은 에너지를 발산하며, 받은 사람에게 행복과 좋은 기운을 전달합니다. 이 아름다운 꽃다발로 사랑과 감사를 전해보세요.','2025-01-19 23:38:11','2025-01-22 17:36:36',0,NULL),(11,'해바라기 꽃다발[P]',89000.00,'프리미엄','/images/22568660-7ad1-463e-a760-b493eb723def.jpg','\"해바라기 꽃다발\"은 밝고 활기찬 에너지를 선사하는 꽃다발입니다. 해바라기는 태양을 따라 자라는 특성 때문에 항상 밝고 긍정적인 느낌을 줍니다. 이 꽃다발은 사랑, 행복, 희망의 상징으로, 특별한 사람에게 힘과 용기를 불어넣어 줍니다.\r\n\r\n여름과 가을의 따뜻한 햇살을 닮은 해바라기는 누군가에게 응원의 메시지를 전달하기에 완벽한 선택입니다. 친구의 생일, 기념일, 새로운 시작을 축하하는 순간에 이 꽃다발을 선물하면 기쁨과 감동을 동시에 선사할 수 있습니다.\r\n\r\n\"해바라기 꽃다발\"은 그 자체로 밝은 에너지를 발산하며, 받은 사람에게 행복과 좋은 기운을 전달합니다. 이 아름다운 꽃다발로 사랑과 감사를 전해보세요.','2025-01-19 23:38:32','2025-01-22 18:13:13',0,NULL),(12,'소국 꽃다발',46000.00,'일반','/images/a61ca16b-e56f-46e7-a767-2c5767bcfa79.jpg','소국 꽃다발은 아담하고 사랑스러운 소국꽃을 주제로 한 꽃다발로, 그 자체로 따뜻하고 포근한 느낌을 전달합니다. 소국꽃은 특히 다채로운 색상으로 사랑받으며, 각기 다른 색조의 꽃들이 어우러져 고유의 아름다움을 자아냅니다. 주로 작은 꽃잎들이 모여 하나의 꽃을 이루는 모습이 특징으로, 사랑과 감사의 마음을 전하기에 적합한 꽃입니다.\r\n\r\n소국 꽃다발은 여러 색상의 소국꽃들이 자연스럽게 어우러져 소박하면서도 세련된 느낌을 주며, 기념일이나 생일, 감사의 마음을 전하고 싶은 순간에 특별한 의미를 담아 선물할 수 있습니다. 또한 소국은 길게 지속되는 꽃의 아름다움으로, 선물 받는 사람에게 오랫동안 행복과 기쁨을 전합니다.','2025-01-19 23:47:17','2025-01-22 12:09:31',0,NULL),(13,'소국 꽃다발[P]',66000.00,'프리미엄','/images/1a9bb9f6-a550-4105-a717-d9b991a97b26.jpg','소국 꽃다발은 아담하고 사랑스러운 소국꽃을 주제로 한 꽃다발로, 그 자체로 따뜻하고 포근한 느낌을 전달합니다. 소국꽃은 특히 다채로운 색상으로 사랑받으며, 각기 다른 색조의 꽃들이 어우러져 고유의 아름다움을 자아냅니다. 주로 작은 꽃잎들이 모여 하나의 꽃을 이루는 모습이 특징으로, 사랑과 감사의 마음을 전하기에 적합한 꽃입니다.\r\n\r\n소국 꽃다발은 여러 색상의 소국꽃들이 자연스럽게 어우러져 소박하면서도 세련된 느낌을 주며, 기념일이나 생일, 감사의 마음을 전하고 싶은 순간에 특별한 의미를 담아 선물할 수 있습니다. 또한 소국은 길게 지속되는 꽃의 아름다움으로, 선물 받는 사람에게 오랫동안 행복과 기쁨을 전합니다.','2025-01-19 23:51:35','2025-01-22 18:13:26',0,NULL),(14,'엑시덴탈 러브',58000.00,'일반','/images/4fbf95b2-467b-4c3b-850f-a74d7b855d56.jpg','엑시덴탈 러브 꽃다발은 우연히 발견한 사랑의 아름다움을 담은 꽃다발입니다. 이 꽃다발은 여러 가지 꽃들이 예기치 않게 어우러져 그 자체로 감동적인 이야기와 같은 느낌을 전합니다. 우연히 만나게 된 아름다움을 표현한 이름처럼, 엑시덴탈 러브는 사랑이 예상치 못한 순간에 찾아오고, 그 사랑의 감동이 시간이 지나도 오래 남을 수 있음을 상징합니다.\r\n\r\n꽃다발 안에는 다채로운 색상의 꽃들이 혼합되어 있으며, 각 꽃은 고유한 의미를 지니고 있습니다. 사랑의 시작이 우연인 것처럼, 이 꽃다발은 사랑이 어떤 형태로든 예기치 않게 찾아올 수 있다는 메시지를 전합니다. 특별한 날이나 중요한 사람에게 전할 수 있는 이 꽃다발은, 사랑과 감동을 담아 마음을 전하는 선물이 될 것입니다.','2025-01-19 23:53:44','2025-01-22 17:35:48',0,NULL),(15,'옴니아',60000.00,'일반','/images/2dbf01c8-481d-4222-b51d-d12d1f0c7905.jpg','옴니아 꽃다발은 \"모든 것\"을 의미하는 라틴어에서 유래된 이름으로, 다양한 아름다움과 감동을 모두 담고 있는 꽃다발입니다. 이 꽃다발은 여러 종류의 꽃들이 조화롭게 어우러져 각기 다른 색상과 형태가 하나의 아름다움을 만들어냅니다. \'옴니아\'는 모든 감정, 모든 순간을 대표하는 꽃다발로, 행복, 사랑, 감사, 기쁨 등 모든 감정을 표현하는 데 적합한 선물입니다.\r\n\r\n꽃다발에는 각기 다른 색감과 형태를 가진 꽃들이 조화롭게 배치되어 있어 보는 이에게 시각적인 즐거움뿐만 아니라 마음의 여유도 선사합니다. 옴니아는 결혼식, 기념일, 감사의 마음을 전하고 싶은 순간에 어울리는 완벽한 선택으로, 특별한 사람에게 감동을 전하고자 할 때 탁월한 선택이 될 것입니다.\r\n\r\n이 꽃다발은 모든 종류의 사랑과 감사의 마음을 담아 전할 수 있는 의미 깊은 선물로, 받는 이에게 큰 감동을 선사할 것입니다.','2025-01-19 23:57:13','2025-01-22 17:35:59',0,NULL),(16,'하트장미 바구니',96000.00,'일반','/images/a3e900c7-557f-4feb-8ba3-66790a7a72d5.jpg','사랑과 애정을 표현하는 하트 모양의 바구니에 담긴 아름다운 장미꽃이 특징인 꽃다발입니다. 이 꽃다발은 진심을 전하는 완벽한 선물로, 특별한 날이나 기념일에 사랑하는 사람에게 전하기 좋습니다. 우아하고 섬세한 디자인이 돋보이며, 연애 기념일, 발렌타인데이 등 특별한 순간에 사랑과 감사를 전달하는 의미 있는 선택이 될 것입니다.','2025-01-20 00:07:26','2025-01-22 17:37:51',0,NULL),(17,'하트장미 바구니[P]',128000.00,'프리미엄','/images/28b184b8-7316-4731-821e-3cde494f1ae9.jpg','사랑과 애정을 표현하는 하트 모양의 바구니에 담긴 아름다운 장미꽃이 특징인 꽃다발입니다. 이 꽃다발은 진심을 전하는 완벽한 선물로, 특별한 날이나 기념일에 사랑하는 사람에게 전하기 좋습니다. 우아하고 섬세한 디자인이 돋보이며, 연애 기념일, 발렌타인데이 등 특별한 순간에 사랑과 감사를 전달하는 의미 있는 선택이 될 것입니다.','2025-01-20 00:08:04','2025-01-22 17:38:29',0,NULL),(18,'그린 로즈',144000.00,'일반','/images/2a1ef15e-5963-42a2-a08f-117af1a767e3.jpg','주로 신선한 녹색 식물과 장미를 주제로 한 꽃바구니입니다. 여기에 사용되는 주요 꽃들은 고급스러운 장미와 그린 색조를 강조하는 다양한 잎사귀, 초록색 식물들입니다. 이 꽃바구니는 자연의 신선함과 평화로운 느낌을 주어, 주로 축하 행사나 감사의 의미를 전달하는데 적합합니다.','2025-01-21 16:53:13','2025-01-22 17:38:40',0,NULL),(19,'델몬트 사랑',53500.00,'일반','/images/ad64faff-1dbf-4270-8958-a25535cc8b92.jpg','소중한 사람에게 당신의 사랑과 따뜻한 마음을 선물하세요! \'델몬트 사랑 꽃바구니\'가 특별한 날을 더욱 빛나게 만들어 드립니다','2025-01-21 17:01:44','2025-01-22 17:04:10',0,NULL),(20,'화려한 외출',86400.00,'일반','/images/69c8e8f7-130e-4be3-a323-9adfae072322.jpg','화려한 외출 꽃바구니는 특별한 날이나 중요한 순간을 위한 고급스럽고 아름다운 꽃바구니입니다. 이 꽃바구니는 중요한 행사나 기념일에 어울리는 고급스러운 디자인으로, 받는 사람에게 특별함과 감동을 전달합니다.','2025-01-21 17:13:58',NULL,0,NULL),(21,'순수한 사랑',67900.00,'일반','/images/e95780cf-6fa0-40d3-8cd5-e200674bc18a.jpg','\"순수한 사랑 꽃바구니\"는 일반적으로 사랑과 감사를 표현하는 꽃바구니로, 결혼식, 기념일, 생일, 또는 특별한 날에 적합한 선물입니다.','2025-01-22 18:19:06',NULL,0,NULL),(22,'순수한 사랑[P]',97000.00,'프리미엄','/images/ee171ada-f2e4-404c-8a60-2f2fdfe499a1.jpg','\"순수한 사랑 꽃바구니\"는 일반적으로 사랑과 감사를 표현하는 꽃바구니로, 결혼식, 기념일, 생일, 또는 특별한 날에 적합한 선물입니다.','2025-01-22 18:22:36',NULL,0,NULL),(23,'안개비 사랑',54400.00,'일반','/images/cedd571f-bc5f-446f-b29d-70603e1ec124.jpg','\"안개비 사랑 꽃바구니\"는 감성적이고 사랑스러운 분위기를 연출할 수 있는 꽃바구니입니다. 이름에서 유추할 수 있듯이, 이 꽃바구니는 안개비처럼 부드럽고 섬세한 느낌을 주는 디자인으로, 사랑하는 사람에게 감동을 전하기에 적합한 선물입니다.','2025-01-22 18:44:28',NULL,0,NULL),(24,'아이리스 혼합바구니',53000.00,'일반','/images/210540f3-e083-44a8-903e-be8f6dc7b4a5.jpg','아이리스 혼합바구니는 아이리스 꽃을 중심으로 다양한 꽃들이 조화를 이루는 꽃바구니입니다. 아이리스는 그 자체로 우아하고 아름다운 꽃으로, 보통 파란색, 보라색, 흰색 등 다양한 색상으로 많이 보입니다. 이 꽃은 그리스 신화에서 유래한 꽃으로, 특히 희망과 아이리스의 신성함을 상징합니다. 혼합바구니는 이 아이리스 꽃과 함께 다른 꽃들을 배치하여 다채로운 색감과 풍성한 느낌을 줍니다.','2025-02-09 21:14:11',NULL,0,NULL),(25,'고귀한 사랑',75000.00,'일반','/images/f944e5bb-0486-4cd1-85ba-c8bfbdf7771b.jpg','\"고귀한 사랑 꽃바구니\"는 사랑과 존경을 담아 고귀한 의미를 지닌 꽃들을 조화롭게 배열한 꽃바구니입니다. 이 꽃바구니는 주로 특별한 기념일이나 행사에서 사용되며, 사랑과 감사를 전하고자 할 때 선택되는 아이템입니다.','2025-02-09 21:22:01',NULL,0,NULL),(26,'해바라기 꽃바구니',77000.00,'일반','/images/16c2105d-6dd3-4532-9893-1ac9b4ae27f5.jpg','해바라기 꽃바구니는 생동감 넘치는 노란 해바라기를 중심으로 구성되며, 유칼립투스나 루스커스 같은 그린 소재를 더해 싱그러움을 강조합니다. 필요에 따라 장미, 거베라, 리시안셔스 등을 함께 배치해 더욱 풍성한 느낌을 줄 수 있습니다. 개업이나 승진, 졸업, 입학 같은 특별한 순간을 축하하는 선물로 좋으며, 밝고 따뜻한 분위기를 원하는 공간을 장식하는 데도 잘 어울립니다. 또한 응원의 메시지를 담아 긍정적인 에너지를 전하고 싶을 때 적합한 꽃바구니입니다.','2025-02-26 17:20:25','2025-02-26 17:21:42',0,NULL),(27,'귀여운여인',85400.00,'프리미엄','/images/ed3d0ab9-433f-49dd-8e4e-de4070f278cd.jpg','‘귀여운 여인’ 꽃바구니는 부드럽고 사랑스러운 분위기를 담은 꽃바구니로, 화사한 색감과 우아한 조화가 돋보이는 상품입니다. 은은한 파스텔톤의 장미와 리시안셔스를 중심으로, 풍성한 거베라와 싱그러운 그린 소재를 더해 따뜻하고 로맨틱한 느낌을 연출합니다. 사랑과 감사를 표현하는 선물로 잘 어울리며, 생일이나 기념일, 특별한 날에 감동을 전하기 좋은 꽃바구니입니다.','2025-02-26 17:27:35','2025-02-26 17:28:40',0,NULL),(28,'내 마음이야',53500.00,'일반','/images/63a8d4f1-642c-49af-97e8-653d7f565c3e.jpg','‘내 마음이야’ 꽃바구니는 따뜻한 감성을 담아 사랑과 진심을 전하는 꽃바구니입니다. 은은한 색감의 장미와 리시안셔스를 중심으로, 거베라와 다양한 그린 소재를 조화롭게 어레인지하여 부드럽고 세련된 분위기를 연출합니다. 소중한 사람에게 마음을 표현하고 싶을 때, 특별한 날 감동을 전하고 싶을 때 어울리는 로맨틱한 꽃바구니입니다.','2025-02-26 17:32:35',NULL,0,NULL),(29,'아침같은 사랑',192000.00,'프리미엄','/images/7e115fe1-f943-48fc-9893-75bf8daade00.jpg','‘아침 같은 사랑’ 꽃바구니는 상쾌하고 따뜻한 느낌을 주는 꽃바구니로, 사랑과 희망을 전하는 의미가 담겨 있습니다. 밝고 화사한 색감의 장미와 거베라, 리시안셔스 등을 중심으로 싱그러운 그린 소재가 조화를 이루어 부드럽고 활기찬 분위기를 연출합니다. 하루를 시작하는 기분처럼 상쾌하고 따뜻한 마음을 전하고 싶은 특별한 날에 어울리는 꽃바구니입니다.','2025-02-26 17:35:02',NULL,0,NULL),(30,'핑크 리시안셔스 바구니',53400.00,'일반','/images/612e62bd-4153-4d35-9b91-028e01e2085b.jpg','‘핑크 리시안셔스 바구니’는 사랑스럽고 우아한 느낌을 주는 꽃바구니로, 핑크색 리시안셔스를 중심으로 다양한 꽃들이 조화를 이루어 부드럽고 따뜻한 분위기를 전달합니다. 상큼한 그린 소재와 함께 생기 넘치는 느낌을 더해, 특별한 날이나 중요한 사람에게 감동을 전할 수 있는 완벽한 선택입니다. 사랑과 감사의 마음을 담아 전달할 수 있는 이 꽃바구니는 어떤 상황에도 어울리며, 기념일이나 생일, 감사의 마음을 전하고자 할 때 더욱 빛을 발합니다.','2025-02-26 17:50:55','2025-02-26 17:53:53',0,NULL),(31,'산천보세 1호',52800.00,'일반','/images/16391570-0d57-40cc-9e4c-8c22d19c7044.jpg','‘동량란 산천보세 1호’ 는 고급스러운 동양란으로, 우아한 자태와 품격 있는 분위기를 자아내는 식물입니다. 산천보세 1호는 그 자체로 아름다움과 고요한 매력을 발산하며, 차분한 분위기와 함께 자연스러운 아름다움을 더해줍니다. 이 동양란은 꾸준한 관리만으로도 오랫동안 꽃을 유지할 수 있어 집이나 사무실의 장식용으로 이상적입니다. 특별한 날이나 축하의 의미를 담은 선물로도 적합하며, 세련된 인테리어 소품으로도 큰 역할을 할 수 있습니다.','2025-02-26 17:56:52',NULL,0,NULL),(32,'산천보세 2호',97000.00,'일반','/images/baddd018-2acb-4b11-b30d-439fe4cc52ec.jpg','‘동량란 산천보세 2호’는 고급스러움과 우아함이 강조된 동양란으로, 산천보세 2호는 그 자체로 차분하면서도 고귀한 아름다움을 지닌 식물입니다. 이 동양란은 뚜렷한 특징을 가진 꽃잎과 깊이 있는 색감으로 인테리어에 세련된 분위기를 더해주며, 오랜 시간 동안 꽃을 감상할 수 있어 공간을 더욱 품격 있게 만듭니다. 관리가 용이하여 선물용으로도 훌륭하며, 다양한 기념일이나 중요한 순간을 축하하는 데 적합한 상품입니다. 산천보세 2호는 고급스러운 환경을 원하는 사람들에게 이상적인 선택입니다.','2025-02-26 17:59:36','2025-02-26 18:03:54',0,NULL),(33,'태양금',146000.00,'일반','/images/f1722486-d567-4079-b10b-9ae3f155d28d.jpg','\"태양금 동량란\" 은 고급스러운 동양란으로, 화려하고 따뜻한 느낌을 주는 식물입니다. 밝고 강렬한 금빛 색감을 가진 태양금 동량란은 그 자체로 공간에 고급스러움과 생동감을 불어넣습니다. 이 식물은 특유의 아름다움과 우아함으로 인테리어에 품격을 더해주며, 관리가 용이하여 선물용으로도 적합합니다. 태양금 동량란은 특별한 날이나 축하의 의미를 담아 선물하기 좋으며, 공간에 밝고 긍정적인 에너지를 전달할 수 있는 제품입니다.','2025-02-26 18:11:59','2025-02-26 18:14:29',0,NULL),(34,'태양금 2호',189000.00,'일반','/images/389bbc1f-4491-4d5a-8aee-5d584b26f821.jpg','\"태양금 2호 동량란\" 은 더욱 세련되고 고급스러운 매력을 발산하는 동양란입니다. 태양금 2호는 더욱 풍성하고 깊이 있는 금빛 색감을 자랑하며, 공간에 따뜻한 에너지를 더합니다. 이 동량란은 그 자체로 존재감이 뚜렷하여 인테리어 장식으로 활용하기에 이상적입니다. 관리가 용이하고, 특별한 기념일이나 축하의 의미를 담은 선물로 적합한 제품입니다. 태양금 2호는 고급스러움을 더하며, 공간에 고요하면서도 화려한 아름다움을 더하는 역할을 합니다.','2025-02-26 18:14:50',NULL,0,NULL),(35,'축하 3단 화환',94100.00,'일반','/images/71dd7881-aecb-445a-ae54-9c592e9ebc19.jpg','\"축하 3단 화환\" 은 중요한 축하의 순간을 더욱 특별하게 만들어주는 화환입니다. 세련되고 품격 있는 디자인으로, 특별한 사람이나 이벤트를 축하하는 데 적합합니다. 3단으로 이루어진 구조는 화려함과 고급스러움을 강조하며, 결혼식, 개업, 승진, 졸업 등 다양한 기념일과 행사에 맞춰 사용하기 좋습니다. 신경 써서 준비된 세심한 장식이 축하의 메시지를 잘 전달하며, 행사 공간에 밝고 활기찬 분위기를 불어넣어 줍니다.\r\n\r\n','2025-02-26 18:19:19',NULL,0,NULL),(36,'축하 3단 화환[P]',117400.00,'프리미엄','/images/05564097-6f7a-441e-a87f-e76981964928.jpg','\"축하 3단 화환\" 은 중요한 축하의 순간을 더욱 특별하게 만들어주는 화환입니다. 세련되고 품격 있는 디자인으로, 특별한 사람이나 이벤트를 축하하는 데 적합합니다. 3단으로 이루어진 구조는 화려함과 고급스러움을 강조하며, 결혼식, 개업, 승진, 졸업 등 다양한 기념일과 행사에 맞춰 사용하기 좋습니다. 신경 써서 준비된 세심한 장식이 축하의 메시지를 잘 전달하며, 행사 공간에 밝고 활기찬 분위기를 불어넣어 줍니다.\r\n\r\n','2025-02-26 18:20:04',NULL,0,NULL),(37,'플로센셋',88700.00,'일반','/images/cc758a76-7877-4db2-8fd4-02b1cc8e48db.jpg','\"플로센셋\" 은 우아함과 고급스러움을 지닌 서양란으로, 그 자체로 품격을 더하는 아름다운 식물입니다. 플로센셋은 섬세한 꽃잎과 균형 잡힌 구조로 인테리어에 세련된 분위기를 자아냅니다. 서양란 특유의 우아한 매력을 지닌 이 식물은, 집이나 사무실에서 장식용으로 이상적이며, 그 아름다움으로 공간을 환하게 밝혀줍니다. 관리가 용이하여 선물용으로도 훌륭하며, 특별한 날이나 중요한 순간을 축하하는 데 적합한 품격 있는 선택입니다. 플로센셋은 고급스러운 느낌을 주며, 공간에 차분하고 고귀한 분위기를 더할 수 있습니다.','2025-02-26 18:22:36',NULL,0,NULL),(38,'삼가 명복을 빕니다 ',126000.00,'일반','/images/ded67df2-d69b-408b-a935-2fa38442b866.jpg','\"삼가 명복을 빕니다\" 근조화환은 고인의 명복을 기리고 애도의 마음을 전하는 품격 있는 화환입니다. 고급스러운 꽃들로 구성되어 있으며, 고인의 삶을 기리기 위한 엄숙한 분위기를 자아냅니다. 근조화환은 고인의 명복을 빌며 유족에게 위로의 마음을 전하고, 장례식이나 추모식에 적합한 품격 있는 장식입니다. 꽃의 색감과 배열은 고인의 마지막 길을 아름답게 장식하며, 고인을 향한 깊은 존경과 애도를 표현하는 의미 있는 선물이 됩니다.','2025-02-26 18:24:49',NULL,0,NULL);
/*!40000 ALTER TABLE `product_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_theme`
--

DROP TABLE IF EXISTS `product_theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_theme` (
  `product_id` bigint NOT NULL,
  `theme_id` bigint NOT NULL,
  PRIMARY KEY (`product_id`,`theme_id`),
  KEY `theme_id` (`theme_id`),
  CONSTRAINT `product_theme_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_tbl` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `product_theme_ibfk_2` FOREIGN KEY (`theme_id`) REFERENCES `theme` (`theme_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_theme`
--

LOCK TABLES `product_theme` WRITE;
/*!40000 ALTER TABLE `product_theme` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_theme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review_tbl`
--

DROP TABLE IF EXISTS `review_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_tbl` (
  `review_id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `rating` int DEFAULT NULL,
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_id`),
  KEY `product_id` (`product_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `review_tbl_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_tbl` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `review_tbl_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`id`) ON DELETE CASCADE,
  CONSTRAINT `review_tbl_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_tbl`
--

LOCK TABLES `review_tbl` WRITE;
/*!40000 ALTER TABLE `review_tbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `review_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_tbl`
--

DROP TABLE IF EXISTS `role_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_tbl` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_tbl`
--

LOCK TABLES `role_tbl` WRITE;
/*!40000 ALTER TABLE `role_tbl` DISABLE KEYS */;
INSERT INTO `role_tbl` VALUES (1,'ADMIN'),(2,'USER');
/*!40000 ALTER TABLE `role_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `support_board_tbl`
--

DROP TABLE IF EXISTS `support_board_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `support_board_tbl` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `content` text COLLATE utf8mb4_general_ci NOT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `view_qty` int DEFAULT '0',
  `author_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_secret` tinyint(1) DEFAULT '0',
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_answer` tinyint(1) DEFAULT '0',
  `answer_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_flag` tinyint(1) DEFAULT '0',
  `deleted_date` timestamp NULL DEFAULT NULL,
  `answer_created_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `support_board_tbl`
--

LOCK TABLES `support_board_tbl` WRITE;
/*!40000 ALTER TABLE `support_board_tbl` DISABLE KEYS */;
INSERT INTO `support_board_tbl` VALUES (1,'aaa1','bcvbcbfdgdf',NULL,2,'fdvdf',0,NULL,1,'gd','2025-02-09 15:15:03','2025-02-12 15:44:59',0,NULL,'2025-02-12 14:18:28'),(2,'bbb','sdfdsacsad',NULL,4,'sadfdws',0,NULL,0,NULL,'2025-02-09 19:13:21','2025-02-12 15:46:27',0,NULL,NULL),(3,'ccc1','dasfdsxc',NULL,1,'sdxzc',0,NULL,0,NULL,'2025-02-09 19:29:24','2025-02-12 15:35:07',0,NULL,NULL),(4,'ddddddddddd','wefafdsafasdf',NULL,0,'pach',0,NULL,0,NULL,'2025-02-09 21:38:00','2025-02-09 21:38:00',0,NULL,NULL),(5,'ddddddddddd','wefafdsafasdf',NULL,0,'pach',0,NULL,0,NULL,'2025-02-09 21:38:03','2025-02-09 21:38:03',0,NULL,NULL),(6,'eeeeeeee','wefafdsafasdf',NULL,0,'asdxa',0,NULL,0,NULL,'2025-02-09 21:39:09','2025-02-09 21:39:09',0,NULL,NULL),(7,'fffffffff','dsafsaxasd',NULL,0,'1234',0,NULL,0,NULL,'2025-02-09 21:40:17','2025-02-09 21:40:17',0,NULL,NULL),(9,'비밀글 테스트1','ㅇㄴㅂㅈㄷㅂㅈㄷㅂㅈㄷㅂㅈㄴ',NULL,0,'psw01',0,NULL,0,NULL,'2025-02-09 21:41:18','2025-02-12 15:34:58',0,NULL,NULL),(10,'비밀글 테스트2','ㅁㄴㅇㅁㄹㅇㄴㅁㅇㅁㄴ','/images/4f67be20-52dd-4e1f-8a96-815ab74914d2.jpg',0,'psw02',0,'1q2w3e4r!',0,NULL,'2025-02-09 22:23:59','2025-02-09 22:23:59',0,NULL,NULL),(11,'sadasd','asdasfas','/images/1fcef551-7485-4d14-baf0-f727ffac1085.jpg&S=700_',0,'123123',0,'asdqwer',0,NULL,'2025-02-09 22:25:25','2025-02-09 22:25:25',0,NULL,NULL),(12,'테스틑3','ㄴㅇㅁㄻㅇㄴㄹ',NULL,0,'ㅇㄻ',0,'',0,NULL,'2025-02-09 22:26:22','2025-02-09 22:26:22',0,NULL,NULL),(13,'테스트4','ㄴㅁㅇㅁㄴㅇ',NULL,0,'ㅇㄴㅁㅇㅁㄴㅇ',0,'213qwed',0,NULL,'2025-02-09 22:27:01','2025-02-09 22:27:01',0,NULL,NULL),(14,'테스트5','ㄴㅇㅁㄻㄹ',NULL,0,'ㄴㅇㅁㄹ',0,'dsfdsf',0,NULL,'2025-02-09 22:27:33','2025-02-09 22:27:33',0,NULL,NULL),(15,'테스트6','1231423',NULL,7,'213ㅈㅂㄷ',0,'qwe213',1,'엥?','2025-02-09 22:32:54','2025-02-26 19:07:46',0,NULL,'2025-02-12 13:01:07'),(16,'테스트7','ㅈㅂㄷㄱㅈㅂㄷ',NULL,0,'ㅈㅂㄷㅂㅈㄴㅌ',1,'qwe213',0,NULL,'2025-02-09 22:35:17','2025-02-09 22:35:17',0,NULL,NULL),(17,'테스트 마무리','무슨 사진일까요?','/images/48616627-a7ff-44f6-889f-da7e06b4dca7.gif',0,'test01',1,'qwer1234',1,'짱구 입니다 ㅎ.ㅎ','2025-02-09 22:36:08','2025-02-26 18:30:54',0,NULL,'2025-02-26 18:30:54'),(18,'new 1','asdfdas','/images/3876231d-17f1-44d8-a359-658f489c183c.jpg',0,'zxc',1,'asdwqe23',0,NULL,'2025-02-09 22:39:59','2025-02-12 15:31:12',0,'2025-02-12 15:31:12',NULL),(19,'new 1','asdfdas','/images/99f965fd-27b4-4c34-a5dd-d57ccc6dc7ee.jpg',0,'zxc',1,'asdwqe23',0,NULL,'2025-02-09 22:40:06','2025-02-12 15:44:50',0,NULL,NULL),(20,'ㄴㅇㅁㅇㅁㄴ','ㅇㅁㄴㅇㅁㄴㄹ',NULL,0,'ㄴㅁㅇ',1,'2312',0,NULL,'2025-02-09 22:43:44','2025-02-09 22:43:44',0,NULL,NULL),(21,'ㅁㄴㅇ','ㄹㅇㄹㅇ',NULL,0,'1234',1,'wqer',0,NULL,'2025-02-09 22:45:41','2025-02-09 22:45:41',0,NULL,NULL),(22,'첨부파일 테스트','에몽가','/images/d7059bab-53dc-4008-a3e0-1bc7793b15fa.jpg',0,'에몽가장인',1,'dpahdrk',0,NULL,'2025-02-09 23:00:02','2025-02-09 23:00:02',0,NULL,NULL),(23,'view 테스트','에몽가 ㅋ','/images/776eca87-9756-43d5-8abb-f9c7de80c00f.jpg',0,'psw001',1,'1q2w3e4r',0,NULL,'2025-02-09 23:41:35','2025-02-09 23:41:35',0,NULL,NULL),(25,'gddgdgd','dsfasdf','/images/fd1d929f-2942-4fae-ac49-3745b5cbc2a2.jpg',4,'qqqqqqq',1,'1q2w3e4r!',1,'그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@\n그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@\n그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@\n그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@\n그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@\n그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@그긴거@@@@@@@@@@@@@@@@@@@@@@@@@@','2025-02-12 08:33:52','2025-03-04 07:48:50',0,NULL,'2025-02-12 13:11:53'),(28,'test01','test011','/images/1c0a5752-209b-4af5-b470-fdeb906aa16c.png',0,'ppap+psw',1,'1234',0,NULL,'2025-02-12 15:33:39','2025-02-12 15:46:46',1,'2025-02-12 15:46:46',NULL);
/*!40000 ALTER TABLE `support_board_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `theme`
--

DROP TABLE IF EXISTS `theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `theme` (
  `theme_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`theme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `theme`
--

LOCK TABLES `theme` WRITE;
/*!40000 ALTER TABLE `theme` DISABLE KEYS */;
/*!40000 ALTER TABLE `theme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_tbl`
--

DROP TABLE IF EXISTS `user_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_tbl` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `login_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` timestamp NULL DEFAULT NULL,
  `disabled_date` timestamp NULL DEFAULT NULL,
  `deleted_date` timestamp NULL DEFAULT NULL,
  `provider_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `provider` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_id` (`login_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone_number` (`phone_number`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `user_tbl_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role_tbl` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_tbl`
--

LOCK TABLES `user_tbl` WRITE;
/*!40000 ALTER TABLE `user_tbl` DISABLE KEYS */;
INSERT INTO `user_tbl` VALUES (13,'testidsuper','$2a$10$p87Fjk7My5gdBllB4RMDyephV.NJCAU.FZLm8p2fMFMAJKybhYdvy','administrator','남성','1998-10-08','admin@example.com','010-1234-5678',1,'ACTIVE','2025-01-17 08:59:40',NULL,NULL,NULL,NULL,NULL),(19,'testid01','$2a$10$.byZ4rRiOMl66BxHiW12g.m8wRzK3b5ZW28eoh7a9ojke3fTAgvsO','psw01','여성','2025-01-02','testid01@naver.com','010-1234-1234',2,'DISABLED','2025-01-17 09:08:51',NULL,'2025-02-09 11:33:08',NULL,NULL,NULL),(20,'testid02','$2a$10$wTP48WBPLaWWXNWMOLU5aur/TJjh4wkVxSVxA8I9.S2iluuyC7Hd6','박선우','여성','2025-01-02','testid02@naver.com','010-1234-2334',2,'ACTIVE','2025-01-17 09:09:43',NULL,NULL,NULL,NULL,NULL),(21,'testid03','$2a$10$lRym4Q/ofLgA.hfdVTrhkenpRT8Rd4l048TKjbJO1BS.hb5lq.XVK','박선우','남성','2025-01-07','psw01@naver.com','',2,'ACTIVE','2025-01-18 12:21:15',NULL,NULL,NULL,NULL,NULL),(28,'tjsdn8963','$2a$10$H0vCzHp1mAJH42eUuHUdROb4T8YVeCywaEMhtmisZ6odbroI/vr1y','박선우','남성','1998-10-08','tjsdn89632@naver.com','010-8550-8905',2,'ACTIVE','2025-01-18 16:06:19',NULL,NULL,NULL,NULL,NULL),(29,'testid011','$2a$10$fS7lGkepEtNCuWHcdPXISOlkeP8x.0xuqBIZyegminldgPu39vF3y','박선우','남성','2015-01-31','tjsdn89263@naver.com','010-1234-2676',2,'ACTIVE','2025-01-18 16:21:17',NULL,NULL,NULL,NULL,NULL),(30,'qwer1234','$2a$10$HZcVWxOgXMUABGy0N0Zuk.yVOHWruBmOQznGbVwLGFsB6lwANTBku','박선우','여성','2025-01-09','tjsd1n8963@naver.com','010-1234-4458',2,'ACTIVE','2025-01-19 13:27:38',NULL,NULL,NULL,NULL,NULL),(31,'638450958690-mna2ki3tc21b9m5bbb13gi4p7cni2hu1.apps.googleusercontent.com_115914878289530424418','N/A','하프츄',NULL,NULL,'rkwhr8963@gmail.com',NULL,2,'DISABLED','2025-01-19 15:10:49',NULL,'2025-02-04 16:06:02',NULL,'115914878289530424418','638450958690-mna2ki3tc21b9m5bbb13gi4p7cni2hu1.apps.googleusercontent.com'),(32,'harping1','$2a$10$Ek70SpOnCP6kaBNoSqH.VeHZ8jCLJHEJJnAG5YYnld/ADfhSjyfv2','선우팤','남성','2025-01-01','tjsdn81963@naver.com','010-8555-8905',2,'DELETED','2025-01-19 16:27:24',NULL,NULL,'2025-02-12 15:35:30',NULL,NULL),(33,'qqqqq','$2a$10$PAPcIjnkZpDiTsom99hXJuFXXctOz7scTX.9fAtV9O82vAicqBGmy','박선우','남성','2025-01-02','tjsdn8963@naver.com','010-1234-2333',2,'ACTIVE','2025-01-19 17:12:49',NULL,NULL,NULL,NULL,NULL),(34,'asdffwe','adasdsa','박선우','여성','2025-01-04','tn81963@naver.com','010-1234-2173',2,'ACTIVE','2025-01-23 17:12:49',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'bloomingshop'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-04 16:51:21
