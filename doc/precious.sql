# Host: localhost  (Version: 5.5.40)
# Date: 2015-12-26 16:43:43
# Generator: MySQL-Front 5.3  (Build 4.120)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "t_menu"
#

DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '菜单名',
  `slug` varchar(100) NOT NULL COMMENT '菜单缩略名',
  `display_order` tinyint(4) NOT NULL COMMENT '菜单排序',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

#
# Data for table "t_menu"
#

/*!40000 ALTER TABLE `t_menu` DISABLE KEYS */;
INSERT INTO `t_menu` VALUES (2,'高清电影','gaoqingdianying',1),(3,'1080P','1080P',2),(4,'720P','720P',3),(5,'电视剧','tv',4);
/*!40000 ALTER TABLE `t_menu` ENABLE KEYS */;

#
# Structure for table "t_option"
#

DROP TABLE IF EXISTS `t_option`;
CREATE TABLE `t_option` (
  `opt_key` varchar(255) NOT NULL,
  `opt_value` text NOT NULL,
  PRIMARY KEY (`opt_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Data for table "t_option"
#

/*!40000 ALTER TABLE `t_option` DISABLE KEYS */;
INSERT INTO `t_option` VALUES ('site_description','钻石电影网是Blade框架开发的一款电影磁力链接网站'),('site_keywords','blade框架,电影,磁力链接,Java开源'),('site_name','钻石电影'),('site_title','钻石电影网');
/*!40000 ALTER TABLE `t_option` ENABLE KEYS */;

#
# Structure for table "t_post"
#

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

#
# Data for table "t_post"
#

/*!40000 ALTER TABLE `t_post` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_post` ENABLE KEYS */;

#
# Structure for table "t_post_tag"
#

DROP TABLE IF EXISTS `t_post_tag`;
CREATE TABLE `t_post_tag` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pid` int(10) NOT NULL,
  `tid` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Data for table "t_post_tag"
#

/*!40000 ALTER TABLE `t_post_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_post_tag` ENABLE KEYS */;

#
# Structure for table "t_tag"
#

DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '标签',
  `count` int(10) NOT NULL DEFAULT '0' COMMENT '标签下的文章个数',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

#
# Data for table "t_tag"
#

/*!40000 ALTER TABLE `t_tag` DISABLE KEYS */;
INSERT INTO `t_tag` VALUES (1,'2015',0),(2,'悬疑',0),(3,'惊悚',0),(4,'喜剧',0),(5,'爱情',0),(6,'华语',0),(7,'欧美',0),(8,'搞笑',0),(9,'科幻',0),(10,'动画',0);
/*!40000 ALTER TABLE `t_tag` ENABLE KEYS */;

#
# Structure for table "t_user"
#

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `uid` int(10) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `pass_word` varchar(255) NOT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `is_admin` tinyint(1) NOT NULL COMMENT '是否是管理员',
  `is_del` tinyint(1) NOT NULL COMMENT '是否删除',
  `dateline` int(11) NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Data for table "t_user"
#

/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'admin','BQlRvE7PKAX4b1waywkqD8Vom3yjeYGn',NULL,NULL,1,0,0);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
