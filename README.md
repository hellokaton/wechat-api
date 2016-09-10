# wechat-robot

wechat-robot是基于微信网页版协议开发的普通微信号机器人程序，使用Java语言。

[微信协议分析](doc/protocol.md)

## 使用

直接运行 `me.biezhi.wechat.Application` 中的main函数

## 机器人接口申请地址 (申请后在config.properties文件中配置)

[http://www.itpk.cn/robot.php](http://www.itpk.cn/robot.php)

使用手机扫描二维码

![](http://i13.tietuku.com/76a8af09f08243a7.png)

### 控制台日志

```sh
[wechat-robot] 2016-09-11 01:01:45,734 INFO [main] com.blade.kit.base.Config => Load config [classpath:config.properties]
[wechat-robot] 2016-09-11 01:01:47,763 INFO [main] me.biezhi.wechat.WechatRobot => 获取到uuid为 [IdNRA183Nw==]
[wechat-robot] 2016-09-11 01:01:47,991 INFO [main] me.biezhi.wechat.WechatRobot => 等待登录...
[wechat-robot] 2016-09-11 01:01:57,180 INFO [main] me.biezhi.wechat.WechatRobot => 成功扫描,请在手机上点击确认以登录
[wechat-robot] 2016-09-11 01:01:59,184 INFO [main] me.biezhi.wechat.WechatRobot => 等待登录...
[wechat-robot] 2016-09-11 01:01:59,204 INFO [main] me.biezhi.wechat.WechatRobot => 正在登录...
[wechat-robot] 2016-09-11 01:01:59,959 INFO [main] me.biezhi.wechat.WechatRobot => 微信登录成功
[wechat-robot] 2016-09-11 01:01:59,959 INFO [main] me.biezhi.wechat.WechatRobot => 微信初始化...
[wechat-robot] 2016-09-11 01:02:00,153 INFO [main] me.biezhi.wechat.WechatRobot => 微信初始化成功
[wechat-robot] 2016-09-11 01:02:00,153 INFO [main] me.biezhi.wechat.WechatRobot => 开启状态通知...
[wechat-robot] 2016-09-11 01:02:00,259 INFO [main] me.biezhi.wechat.WechatRobot => 开启状态通知成功
[wechat-robot] 2016-09-11 01:02:00,259 INFO [main] me.biezhi.wechat.WechatRobot => 获取联系人...
[wechat-robot] 2016-09-11 01:02:00,538 INFO [main] me.biezhi.wechat.WechatRobot => 获取联系人成功
[wechat-robot] 2016-09-11 01:02:00,538 INFO [main] me.biezhi.wechat.WechatRobot => 共有 4 位联系人
[wechat-robot] 2016-09-11 01:02:00,539 INFO [wechat-listener-thread] me.biezhi.wechat.listener.WechatListener => 进入消息监听模式 ...
[wechat-robot] 2016-09-11 01:02:00,782 INFO [wechat-listener-thread] me.biezhi.wechat.service.WechatService => 选择线路：[webpush2.weixin.qq.com]
[wechat-robot] 2016-09-11 01:02:00,835 INFO [wechat-listener-thread] me.biezhi.wechat.listener.WechatListener => retcode=0, selector=2
[wechat-robot] 2016-09-11 01:02:01,095 INFO [wechat-listener-thread] me.biezhi.wechat.service.WechatService => 你有新的消息，请注意查收
[wechat-robot] 2016-09-11 01:02:01,095 INFO [wechat-listener-thread] me.biezhi.wechat.service.WechatService => 成功截获微信初始化消息
[wechat-robot] 2016-09-11 01:02:01,095 INFO [wechat-listener-thread] me.biezhi.wechat.listener.WechatListener => 等待2000ms...
```

### 测试通信

![](http://i.imgur.com/PKuEtH4.png)

```sh
2016-02-21 18:53:00,500 INFO [listenMsgMode] me.biezhi.weixin.App | [*] retcode=0,selector=6
2016-02-21 18:53:00,665 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&sid=WdbalZCr+GV33OUD&r=1456051980
2016-02-21 18:53:00,775 INFO [listenMsgMode] me.biezhi.weixin.App | [*] 你有新的消息，请注意查收
2016-02-21 18:53:00,775 INFO [listenMsgMode] me.biezhi.weixin.App | kiki: 你叫什么？
2016-02-21 18:53:00,890 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC
2016-02-21 18:53:01,166 INFO [listenMsgMode] me.biezhi.weixin.App | 自动回复 我叫二蛋，你叫什么啊！
2016-02-21 18:53:01,167 INFO [listenMsgMode] me.biezhi.weixin.App | [*] GET https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=145605198122996&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&uin=3155248292&sid=WdbalZCr%2BGV33OUD&deviceid=e1456051957&synckey=1_610390336%7C2_610390405%7C3_610390360%7C11_610390233%7C13_610390001%7C201_1456052048%7C1000_1456048420&_=1456051981167
2016-02-21 18:53:01,219 INFO [listenMsgMode] me.biezhi.weixin.App | [*] retcode=0,selector=2
2016-02-21 18:53:01,220 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&sid=WdbalZCr+GV33OUD&r=1456051981
2016-02-21 18:53:01,323 INFO [listenMsgMode] me.biezhi.weixin.App | [*] GET https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=145605198116582&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&uin=3155248292&sid=WdbalZCr%2BGV33OUD&deviceid=e1456051957&synckey=1_610390336%7C2_610390406%7C3_610390360%7C11_610390233%7C13_610390001%7C201_1456052048%7C1000_1456048420&_=1456051981323
2016-02-21 18:53:18,565 INFO [listenMsgMode] me.biezhi.weixin.App | [*] retcode=0,selector=6
2016-02-21 18:53:18,727 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&sid=WdbalZCr+GV33OUD&r=1456051998
2016-02-21 18:53:18,822 INFO [listenMsgMode] me.biezhi.weixin.App | [*] 你有新的消息，请注意查收
2016-02-21 18:53:18,822 INFO [listenMsgMode] me.biezhi.weixin.App | kiki: 我叫王二小
2016-02-21 18:53:19,136 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC
2016-02-21 18:53:19,412 INFO [listenMsgMode] me.biezhi.weixin.App | 自动回复 傻了眼
2016-02-21 18:53:19,412 INFO [listenMsgMode] me.biezhi.weixin.App | [*] GET https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=145605199972325&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&uin=3155248292&sid=WdbalZCr%2BGV33OUD&deviceid=e1456051957&synckey=1_610390336%7C2_610390408%7C3_610390360%7C11_610390233%7C13_610390001%7C201_1456052066%7C1000_1456048420&_=1456051999412
2016-02-21 18:53:19,466 INFO [listenMsgMode] me.biezhi.weixin.App | [*] retcode=0,selector=2
2016-02-21 18:53:19,466 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&sid=WdbalZCr+GV33OUD&r=1456051999
2016-02-21 18:53:19,561 INFO [listenMsgMode] me.biezhi.weixin.App | [*] GET https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=145605199935595&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&uin=3155248292&sid=WdbalZCr%2BGV33OUD&deviceid=e1456051957&synckey=1_610390336%7C2_610390409%7C3_610390360%7C11_610390233%7C13_610390001%7C201_1456052066%7C1000_1456048420&_=1456051999561
2016-02-21 18:53:28,820 INFO [listenMsgMode] me.biezhi.weixin.App | [*] retcode=0,selector=6
2016-02-21 18:53:28,984 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&sid=WdbalZCr+GV33OUD&r=1456052008
2016-02-21 18:53:29,094 INFO [listenMsgMode] me.biezhi.weixin.App | [*] 你有新的消息，请注意查收
2016-02-21 18:53:29,094 INFO [listenMsgMode] me.biezhi.weixin.App | kiki: 上海明天天气怎么样
2016-02-21 18:53:29,347 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC
2016-02-21 18:53:29,612 INFO [listenMsgMode] me.biezhi.weixin.App | 自动回复 上海天气预报：
明天7℃ 中雨 东南风微风，晚上5℃ 阴 无持续风向微风，日出和日落时间06:29|17:46
城市信息：经度121.445，纬度31.213，区号021，邮编200000，海拔19米。
2016-02-21 18:53:29,612 INFO [listenMsgMode] me.biezhi.weixin.App | [*] GET https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=145605200913443&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&uin=3155248292&sid=WdbalZCr%2BGV33OUD&deviceid=e1456051957&synckey=1_610390336%7C2_610390410%7C3_610390360%7C11_610390233%7C13_610390001%7C201_1456052076%7C1000_1456048420&_=1456052009612
2016-02-21 18:53:29,664 INFO [listenMsgMode] me.biezhi.weixin.App | [*] retcode=0,selector=2
2016-02-21 18:53:29,664 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?lang=zh_CN&pass_ticket=CSFPx%2BpUZ1zKum%2BcUK5n%2BM39BoQdvcAzgYBDEzCRtzry3ogKVO2wJChbiSgPimQC&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&sid=WdbalZCr+GV33OUD&r=1456052009
2016-02-21 18:53:29,765 INFO [listenMsgMode] me.biezhi.weixin.App | [*] GET https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=145605200916678&skey=@crypt_a742d75a_acfe5f44c68520a5190a939e0f74ed37&uin=3155248292&sid=WdbalZCr%2BGV33OUD&deviceid=e1456051957&synckey=1_610390336%7C2_610390411%7C3_610390360%7C11_610390233%7C13_610390001%7C201_1456052076%7C1000_1456048420&_=1456052009765
```

更多有趣的东西你可以自己研究，比如发送图片，音乐，推送文章等。。
