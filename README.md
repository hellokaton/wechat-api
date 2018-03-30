# wechat-api

wechat-api 是微信个人号的Java版本API，让个人号具备更多能力，提供方便的接口调用。

[在线文档](https://biezhi.github.io/wechat-api/)

[![](https://img.shields.io/travis/biezhi/wechat-api.svg)](https://travis-ci.org/biezhi/wechat-api)
[![](https://img.shields.io/maven-central/v/io.github.biezhi/wechat-api.svg)](https://mvnrepository.com/artifact/io.github.biezhi/wechat-api)
[![](https://img.shields.io/badge/license-MIT-FF0080.svg)](https://github.com/biezhi/wechat-api/blob/master/LICENSE)
[![@biezhi on zhihu](https://img.shields.io/badge/zhihu-%40biezhi-red.svg)](https://www.zhihu.com/people/biezhi)
[![](https://img.shields.io/github/followers/biezhi.svg?style=social&label=Follow%20Me)](https://github.com/biezhi)

## 特性

- 使用简单，引入依赖即可
- 支持本地图片和终端输出二维码
- 本地自动登录
- 支持文本、图片、视频、撤回消息等
- 支持发送文本、图片、附件
- 注解绑定消息监听
- 群聊、单聊支持
- 添加好友验证
- 撤回消息获取
- JDK7+

## 使用

本地开发的同学请先安装 [lombok](https://projectlombok.org/) 插件并确保你的JDK环境是1.7+

引入 `maven` 依赖

```xml
<dependency>
    <groupId>io.github.biezhi</groupId>
    <artifactId>wechat-api</artifactId>
    <version>1.0.6</version>
</dependency>
```

构建自己的小机器人

```java
public class HelloBot extends WeChatBot {
    
    public HelloBot(Config config) {
        super(config);
    }
    
    @Bind(msgType = MsgType.TEXT)
    public void handleText(WeChatMessage message) {
        if (StringUtils.isNotEmpty(message.getName())) {
            log.info("接收到 [{}] 的消息: {}", message.getName(), message.getText());
            this.sendMsg(message.getFromUserName(), "自动回复: " + message.getText());
        }
    }
    
    public static void main(String[] args) {
        new HelloBot(Config.me().autoLogin(true).showTerminal(true)).start();
    }
    
}
```

## Bot API

```java
/**
 * 给文件助手发送消息
 *
 * @param msg 消息内容
 * @return 发送是否成功
 */
boolean sendMsgToFileHelper(String msg);

/**
 * 给某个用户发送消息
 *
 * @param name 用户UserName
 * @param msg  消息内容
 * @return 发送是否成功
 */
boolean sendMsg(String name, String msg);

/**
 * 根据名称发送消息
 *
 * @param name 备注或昵称，精确匹配
 * @param msg  消息内容
 * @return 发送是否成功
 */
boolean sendMsgByName(String name, String msg);

/**
 * 给某个用户发送图片消息
 *
 * @param name    用户UserName
 * @param imgPath 图片路径
 * @return 发送是否成功
 */
boolean sendImg(String name, String imgPath);

/**
 * 根据名称发送图片消息
 *
 * @param name    备注或昵称，精确匹配
 * @param imgPath 图片路径
 * @return 发送是否成功
 */
boolean sendImgName(String name, String imgPath);

/**
 * 给用户发送文件
 *
 * @param name     用户UserName
 * @param filePath 文件路径
 * @return 发送是否成功
 */
boolean sendFile(String name, String filePath);

/**
 * 根据名称发送文件消息
 *
 * @param name     备注或昵称，精确匹配
 * @param filePath 文件路径
 * @return 发送是否成功
 */
boolean sendFileName(String name, String filePath);
```

[更多API见文档](https://biezhi.github.io/wechat-api/#/?id=api%e5%88%97%e8%a1%a8)

## TODO

1. 接收位置
2. 撤回消息查看
3. 发送文件消息
4. 多线程处理消息

## 开源协议

[MIT](https://github.com/biezhi/wechat-api/blob/master/LICENSE)