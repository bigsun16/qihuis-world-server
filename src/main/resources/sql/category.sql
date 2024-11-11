/*
 Navicat Premium Dump SQL

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80401 (8.4.1)
 Source Host           : localhost:3306
 Source Schema         : wish_tree

 Target Server Type    : MySQL
 Target Server Version : 80401 (8.4.1)
 File Encoding         : 65001

 Date: 11/11/2024 19:33:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `thumb_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 'thoughtPage', '思想印记', 'src\\assets\\thought\\removebg-preview\\thinker1.png');
INSERT INTO `category` VALUES (2, 'sportPage', '运动', 'src\\assets\\sport\\removebg-preview\\sport1.png');
INSERT INTO `category` VALUES (3, 'notebookPage', '随手记', 'src\\assets\\life-note\\removebg-preview\\notebook1.png');
INSERT INTO `category` VALUES (4, 'viewPage', '风景', 'src\\assets\\views\\removebg-preview\\view2.png');
INSERT INTO `category` VALUES (5, 'treePage', '种植', 'src\\assets\\dream-tree\\removebg-preview\\tree1.png');
INSERT INTO `category` VALUES (6, 'moviePage', '影视', 'src\\assets\\movie\\removebg-preview\\movie1.png');
INSERT INTO `category` VALUES (7, 'MusicPage', '陶冶情操', 'src\\assets\\music\\removebg-preview\\note1.png');
INSERT INTO `category` VALUES (8, 'aquariumPage', '水族箱', 'src\\assets\\aquarium\\removebg-preview\\aquarium1.png');

SET FOREIGN_KEY_CHECKS = 1;
