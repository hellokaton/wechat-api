# wechat-api

wechat-api 是微信个人号的Java版本API，让你更方便的操作个人微信号。

[![](https://img.shields.io/travis/biezhi/wechat-api.svg)](https://travis-ci.org/biezhi/wechat-api)
[![](https://img.shields.io/maven-central/v/io.github.biezhi/wechat-api.svg)](https://mvnrepository.com/artifact/io.github.biezhi/wechat-api)
[![](https://img.shields.io/badge/license-MIT-FF0080.svg)](https://github.com/biezhi/wechat-api/blob/master/LICENSE)
[![@biezhi on zhihu](https://img.shields.io/badge/zhihu-%40biezhi-red.svg)](https://www.zhihu.com/people/biezhi)
[![](https://img.shields.io/github/followers/biezhi.svg?style=social&label=Follow%20Me)](https://github.com/biezhi)

## 特性

- 使用简单，引入以来即可
- 支持本地图片和终端输出二维码
- 支持文本、图片、视频、撤回消息等
- 注解绑定消息监听
- 群聊、单聊支持
- 添加好友验证
- 撤回消息获取
- JDK7+

## 使用

引入 `maven` 依赖

```xml
<dependency>
    <groupId>io.github.biezhi</groupId>
    <artifactId>wechat-api</artifactId>
    <version>1.0.1</version>
</dependency>
```

构建自己的小机器人

```java
public class MyBot extends WeChatBot {

    public MyBot(Config config) {
        super(config);
    }
    
    @Bind(msgType = MsgType.TEXT)
    public void handleText(WeChatMessage message) {
        log.info("接收到 [{}] 的消息: {}", message.getName(), message.getText());
        this.sendText(message.getFromUserName(), message.getText() + " : 嘻嘻嘻 [坏笑]");
    }
    
    public static void main(String[] args) {
        new MyBot(Config.me().showTerminal(true)).start();
    }
    
}
```

## 开源协议

[MIT](https://github.com/biezhi/wechat-api/blob/master/LICENSE)