# 快速开始

`wechat-api` 是一个基于微信Web协议的Java版本封装，提供了发送消息、接受消息、心跳检查等功能。
你需要做的是引入依赖，然后创建属于自己的微信机器人应用。

该依赖支持 JDK7+，请确保你的Java环境是该版本或更高， 加载最新版 `wechat-api` 依赖，
可以在 Github 的 `maven-central` 图标查看或者在 [这里](http://search.maven.org/#search%7Cga%7C1%7Cwechat-api ":target=_blank") 查看。

**添加依赖**

```xml
<dependency>
    <groupId>io.github.biezhi</groupId>
    <artifactId>wechat-api</artifactId>
    <version>1.0.3</version>
</dependency>
``` 

该依赖中包含了日志组件，默认是 `logback`，如果你的系统中需要其他的日志组件，请先排除 `logback`

```xml
<dependency>
    <groupId>io.github.biezhi</groupId>
    <artifactId>wechat-api</artifactId>
    <version>1.0.3</version>
    <exclusions>
        <exclusion>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

# 创建机器人

创建一个 Java 类作为我们的机器人，比如 `HelloBot`

```java
public class HelloBot extends WeChatBot {
    
    public HelloBot(Config config) {
        super(config);
    }
    
    @Bind(msgType = MsgType.TEXT)
    public void handleText(WeChatMessage message) {
        if (StringUtils.isNotEmpty(message.getName())) {
            log.info("接收到 [{}] 的消息: {}", message.getName(), message.getText());
            this.api().sendText(message.getFromUserName(), "自动回复: " + message.getText());
        }
    }
    
    public static void main(String[] args) {
        new HelloBot(Config.me().autoLogin(true).showTerminal(true)).start();
    }
    
}
```

不到 20 行的代码我们已经完成了一个简单的机器人实现，我们来看一下这个机器人的代码流程，
然后分析他们的意思。

1. 首先继承 `WeChatBot` 这个父类被标识为一个机器人
2. 创建了一个以 `Config` 为入参的构造函数
3. 设置允许自动登录（在常见问题中描述了什么是自动登录）
4. 设置在终端显示微信二维码
5. 绑定了一个函数 `handleText` 来接受 **文本** 类型的消息
6. 启动机器人

## 消息监听

`wechat-api` 提供了一个父类 `WeChatBot` 给所有的微信机器人作为扩展使用，
通过注解绑定函数来完成消息灵活监听。

1. 使用 `@Bind` 注解在方法标识
2. 方法参数为 `WeChatMessage`
3. `@Bind` 注解参数: `msgType` 为消息类型，建议不要使用 `ALL`，这里可以配置多个
4. `@Bind` 注解参数: `accountType` 监听群聊或单聊

# 运行源码

很多同学想获得最新的代码在本地运行，需要再三强调一点。

!> 在本地开发环境安装 [lombok](https://projectlombok.org/ ":target=_blank") 插件并确保你的 Java 环境是 1.7+

# API列表

所有的 API 都在 [WeChatApi](https://github.com/biezhi/wechat-api/blob/master/src/main/java/io/github/biezhi/wechat/api/WeChatApi.java ":target=_blank") 这个接口中定义。

# 常见问题

## 什么是自动登录?

> 使用 `wechat-api` 扫码登录后会获得微信返回的 **登录会话** 缓存在本地，同事也会获得一个 `Cookie` 在通讯时用到，
> 自动登录的原理是每次心跳检查后将登录信息缓存到本地，存储为 `login.json`，重新启动时加载该文件恢复即可。

## 为什么我发送信息的时候部分信息没有成功发出来?

> 有些账号是天生无法给自己的账号发送信息的，建议使用 `filehelper` 代替。

# 更新日志

## v1.0.4

1. 校验函数绑定错误跑出异常
2. 添加通过 `ClassPath` 加载 `Config` 配置
3. 消息只可以被处理一次
4. 添加处理位置类型消息
5. 添加创建群聊接口
6. 添加消息撤回接口