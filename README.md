# precious

使用blade框架开发的电影资源发布站点，未开发完成。

## 使用方法

> 您必须具备mysql+jdk+maven环境

	1. 下载项目
	
	```sh
	git clone https://github.com/biezhi/precious.git
	```
	
	2. 安装数据库
	
	新建数据库 `precious` ，将 `doc/precious.sql` 导入到您的数据库中
	
	3. 运行项目
	
	```sh
	mvn clean install jetty:run
	```
