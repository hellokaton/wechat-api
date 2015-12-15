/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.40 : Database - jewel
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jewel` /*!40100 DEFAULT CHARACTER SET utf8 */;

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '菜单名',
  `slug` varchar(100) NOT NULL COMMENT '菜单缩略名',
  `display_order` tinyint(4) NOT NULL COMMENT '菜单排序',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `t_menu` */

insert  into `t_menu`(`id`,`name`,`slug`,`display_order`) values (1,'首页','/',0),(2,'高清电影','/gaoqingdianying',1),(3,'1080P','/1080P',2),(4,'720P','/720P',3),(5,'电视剧','/tv',4);

/*Table structure for table `t_option` */

DROP TABLE IF EXISTS `t_option`;

CREATE TABLE `t_option` (
  `opt_key` varchar(255) NOT NULL,
  `opt_value` text NOT NULL,
  PRIMARY KEY (`opt_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `t_option` */

/*Table structure for table `t_post` */

DROP TABLE IF EXISTS `t_post`;

CREATE TABLE `t_post` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL COMMENT '标题',
  `slug` varchar(255) DEFAULT NULL COMMENT '缩略名',
  `cover` varchar(255) NOT NULL COMMENT '封面',
  `menu_id` int(11) DEFAULT NULL COMMENT '所属菜单',
  `content` text NOT NULL COMMENT '内容',
  `links` text,
  `views` int(10) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已经删除',
  `dateline` int(11) NOT NULL COMMENT '发布日期',
  PRIMARY KEY (`pid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `t_post` */

/*Table structure for table `t_post_tag` */

DROP TABLE IF EXISTS `t_post_tag`;

CREATE TABLE `t_post_tag` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pid` int(10) NOT NULL,
  `tid` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `t_post_tag` */

/*Table structure for table `t_tag` */

DROP TABLE IF EXISTS `t_tag`;

CREATE TABLE `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '标签',
  `count` int(10) NOT NULL DEFAULT '0' COMMENT '标签下的文章个数',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `t_tag` */

insert  into `t_tag`(`id`,`name`,`count`) values (1,'2015',0),(2,'悬疑',0),(3,'惊悚',0),(4,'喜剧',0),(5,'爱情',0),(6,'华语',0),(7,'欧美',0),(8,'搞笑',0),(9,'科幻',0),(10,'动画',0);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `uid` int(10) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `pass_word` varchar(255) NOT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `is_admin` tinyint(1) NOT NULL COMMENT '是否是管理员',
  `is_del` tinyint(1) NOT NULL COMMENT '是否删除',
  `dateline` int(11) NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`uid`,`login_name`,`pass_word`,`avatar`,`is_admin`,`is_del`,`dateline`) values (1,'admin','BQlRvE7PKAX4b1waywkqD8Vom3yjeYGn',NULL,1,0,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
