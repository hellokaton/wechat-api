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
    <version>1.0.3</version>
</dependency>
```

构建自己的小机器人

```java
public class MyBot extends WeChatBot {

    public MyBot(Config config) {
        super(config);
    }

    /**
     * 绑定群聊信息
     *
     * @param message
     */
    @Bind(msgType = MsgType.ALL, accountType = AccountType.TYPE_GROUP)
    public void groupMessage(WeChatMessage message) {
        log.info("接收到群 [{}] 的消息: {}", message.getName(), message.getText());
        this.api().sendText(message.getFromUserName(), "发送给群: " + new Date().toLocaleString());
    }

    /**
     * 绑定私聊消息
     *
     * @param message
     */
    @Bind(msgType = MsgType.TEXT, accountType = AccountType.TYPE_FRIEND)
    public void friendMessage(WeChatMessage message) {
        log.info("接收到好友 [{}] 的消息: {}", message.getName(), message.getText());
        this.api().sendText(message.getFromUserName(), "自动回复: " + message.getText());
    }
    
    /**
     * 好友验证消息
     *
     * @param message
     */
    @Bind(msgType = MsgType.ADD_FRIEND)
    public void addFriend(WeChatMessage message) {
        log.info("收到好友验证消息: {}", message.getText());
        if (message.getText().contains("java")) {
            this.api().verify(message.getRaw().getRecommend());
        }
    }
    
    public static void main(String[] args) {
        new MyBot(Config.me().autoLogin(true).showTerminal(true)).start();
    }
    
}
```

## Bot API

```java
/**
 * 发送文本消息
 *
 * @param toUser
 * @param msg
 */
void sendText(String toUser, String msg);

/**
 * 根据备注或者昵称发送消息
 *
 * @param name
 * @param msg
 */
void sendTextByName(String name, String msg);

/**
 * 发送图片
 *
 * @param toUser
 * @param filePath
 */
void sendImg(String toUser, String filePath);

/**
 * 根据备注或者昵称发送图片
 *
 * @param name
 * @param filePath
 */
void sendImgByName(String name, String filePath);

/**
 * 发送文件
 *
 * @param toUser
 * @param filePath
 */
void sendFile(String toUser, String filePath);

/**
 * 根据备注或者昵称发送消息
 *
 * @param name
 * @param filePath
 */
void sendFileByName(String name, String filePath);

/**
 * 上传附件
 *
 * @param toUser
 * @param filePath
 * @return
 */
MediaResponse uploadMedia(String toUser, String filePath);

/**
 * 根据UserName获取账号信息
 *
 * @param id
 * @return
 */
Account getAccountById(String id);

/**
 * 根据备注或昵称查找账户
 *
 * @param name
 * @return
 */
Account getAccountByName(String name);

/**
 * 添加好友验证
 *
 * @param recommend 好友信息
 */
void verify(Recommend recommend);
```

## TODO

1. 接收位置
2. 撤回消息查看
3. 创建群聊
4. 发送文件消息
5. 消息撤回

## 开源协议

[MIT](https://github.com/biezhi/wechat-api/blob/master/LICENSE)