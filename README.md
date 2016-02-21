# wechat-robot

wechat-robot是基于微信网页版协议开发的普通微信号机器人程序，使用Java语言。

[微信协议分析](doc/protocol.md)

## 使用

这是一个Maven工程，如果你想在普通项目中运行该程序，需要下载 [blade-kit.jar](http://search.maven.org/remotecontent?filepath=com/bladejava/blade-kit/1.2.9-alpha/blade-kit-1.2.9-alpha.jar)
运行 `me.biezhi.weixin.App` 类

使用手机扫描二维码

![](http://i13.tietuku.com/76a8af09f08243a7.png)

### 控制台日志

```sh
2016-02-21 18:49:02,636 INFO [main] me.biezhi.weixin.App | [*] GET https://login.weixin.qq.com/jslogin?appid=wx782c26e4c19acffb&fun=new&lang=zh_CN&_=1456051742
2016-02-21 18:49:03,100 INFO [main] me.biezhi.weixin.App | [*] 获取到uuid为 [QZP9RCOmVA==]
2016-02-21 18:49:03,328 INFO [main] me.biezhi.weixin.App | [*] GET https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?tip=1&uuid=QZP9RCOmVA==&_=1456051743
2016-02-21 18:49:06,413 INFO [main] me.biezhi.weixin.App | [*] 成功扫描,请在手机上点击确认以登录
2016-02-21 18:49:08,413 INFO [main] me.biezhi.weixin.App | [*] GET https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?tip=1&uuid=QZP9RCOmVA==&_=1456051748
2016-02-21 18:49:08,414 INFO [main] me.biezhi.weixin.App | [*] 正在登录...
2016-02-21 18:49:08,414 INFO [main] me.biezhi.weixin.App | [*] redirect_uri=https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket=A4BjR97r2-55oYG23dXSucQK@qrticket_0&uuid=QZP9RCOmVA==&lang=zh_CN&scan=1456051814&fun=new
2016-02-21 18:49:08,414 INFO [main] me.biezhi.weixin.App | [*] base_uri=https://wx2.qq.com/cgi-bin/mmwebwx-bin
2016-02-21 18:49:08,419 INFO [main] me.biezhi.weixin.App | [*] GET https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket=A4BjR97r2-55oYG23dXSucQK@qrticket_0&uuid=QZP9RCOmVA==&lang=zh_CN&scan=1456051814&fun=new
2016-02-21 18:49:08,861 INFO [main] me.biezhi.weixin.App | [*] skey[@crypt_a742d75a_9c1d3752638ef9112f0b48895e8ae69d]
2016-02-21 18:49:08,861 INFO [main] me.biezhi.weixin.App | [*] wxsid[3DfEeKrrBD2r4Sn6]
2016-02-21 18:49:08,861 INFO [main] me.biezhi.weixin.App | [*] wxuin[3155248292]
2016-02-21 18:49:08,861 INFO [main] me.biezhi.weixin.App | [*] pass_ticket[iYBKk3bszHw1ZIUZgow4tqSZx%2BfY3ACnGlgqQI7rAJwkP%2B4iVQHdHlxT0ipK3V4N]
2016-02-21 18:49:08,866 INFO [main] me.biezhi.weixin.App | [*] 微信登录成功
2016-02-21 18:49:09,031 INFO [main] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r=1456051748&pass_ticket=iYBKk3bszHw1ZIUZgow4tqSZx%2BfY3ACnGlgqQI7rAJwkP%2B4iVQHdHlxT0ipK3V4N&skey=@crypt_a742d75a_9c1d3752638ef9112f0b48895e8ae69d
2016-02-21 18:49:09,214 INFO [main] me.biezhi.weixin.App | [*] 微信初始化成功
2016-02-21 18:49:09,215 INFO [main] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxstatusnotify?lang=zh_CN&pass_ticket=iYBKk3bszHw1ZIUZgow4tqSZx%2BfY3ACnGlgqQI7rAJwkP%2B4iVQHdHlxT0ipK3V4N
2016-02-21 18:49:09,306 INFO [main] me.biezhi.weixin.App | [*] 开启状态通知成功
2016-02-21 18:49:09,307 INFO [main] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?pass_ticket=iYBKk3bszHw1ZIUZgow4tqSZx%2BfY3ACnGlgqQI7rAJwkP%2B4iVQHdHlxT0ipK3V4N&skey=@crypt_a742d75a_9c1d3752638ef9112f0b48895e8ae69d&r=1456051749
2016-02-21 18:49:09,540 INFO [main] me.biezhi.weixin.App | [*] 获取联系人成功
2016-02-21 18:49:09,540 INFO [main] me.biezhi.weixin.App | [*] 共有 4 位联系人
2016-02-21 18:49:09,542 INFO [listenMsgMode] me.biezhi.weixin.App | [*] 进入消息监听模式 ...
2016-02-21 18:49:09,543 INFO [listenMsgMode] me.biezhi.weixin.App | [*] GET https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=145605174961757&skey=@crypt_a742d75a_9c1d3752638ef9112f0b48895e8ae69d&uin=3155248292&sid=3DfEeKrrBD2r4Sn6&deviceid=e1456051742&synckey=1_610390336%7C2_610390399%7C3_610390360%7C1000_1456046795&_=1456051749542
2016-02-21 18:49:09,779 INFO [listenMsgMode] me.biezhi.weixin.App | [*] retcode=0,selector=2
2016-02-21 18:49:09,782 INFO [listenMsgMode] me.biezhi.weixin.App | [*] POST https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?lang=zh_CN&pass_ticket=iYBKk3bszHw1ZIUZgow4tqSZx%2BfY3ACnGlgqQI7rAJwkP%2B4iVQHdHlxT0ipK3V4N&skey=@crypt_a742d75a_9c1d3752638ef9112f0b48895e8ae69d&sid=3DfEeKrrBD2r4Sn6&r=1456051749
2016-02-21 18:49:09,978 INFO [listenMsgMode] me.biezhi.weixin.App | [*] 你有新的消息，请注意查收
2016-02-21 18:49:09,978 INFO [listenMsgMode] me.biezhi.weixin.App | [*] 成功截获微信初始化消息
2016-02-21 18:49:09,978 INFO [listenMsgMode] me.biezhi.weixin.App | [*] 你有新的消息，请注意查收
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
