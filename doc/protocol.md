# 微信web协议分析和实现微信机器人（微信网页版 wx2.qq.com）

1.打开首页，分配一个随机uuid，
2.根据该uuid获取二维码图片。
3.微信客户端扫描该图片，在客户端确认登录。
4.浏览器不停的调用一个接口，如果返回登录成功，则调用登录接口
5.此时可以获取联系人列表，可以发送消息。然后不断调用同步接口。
6.如果同步接口有返回，则可以获取新消息，然后继续调用同步接口。

- Java版实现源码：https://github.com/biezhi/wechat-robot
- Python实现：https://github.com/Urinx/WeixinBot
- C#实现：https://github.com/sherlockchou86/WeChat.NET
- QT实现：https://github.com/xiangzhai/qwx
- Perl实现：https://github.com/sjdy521/Mojo-Weixin

## 执行流程

```sh
       +--------------+     +---------------+   +---------------+
       |              |     |               |   |               |
       |   Get UUID   |     |  Get Contact  |   | Status Notify |
       |              |     |               |   |               |
       +-------+------+     +-------^-------+   +-------^-------+
               |                    |                   |
               |                    +-------+  +--------+
               |                            |  |
       +-------v------+               +-----+--+------+      +--------------+
       |              |               |               |      |              |
       |  Get QRCode  |               |  Weixin Init  +------>  Sync Check  <----+
       |              |               |               |      |              |    |
       +-------+------+               +-------^-------+      +-------+------+    |
               |                              |                      |           |
               |                              |                      +-----------+
               |                              |                      |
       +-------v------+               +-------+--------+     +-------v-------+
       |              | Confirm Login |                |     |               |
+------>    Login     +---------------> New Login Page |     |  Weixin Sync  |
|      |              |               |                |     |               |
|      +------+-------+               +----------------+     +---------------+
|             |
|QRCode Scaned|
+-------------+
```

## WebWechat API

### 1. 获取UUID（参考方法 getUUID）

| API | 获取 UUID |
| --- | --------- |
| url | https://login.weixin.qq.com/jslogin |
| method | GET |
| data | URL Encode |
| params | **appid** : wx782c26e4c19acffb <br> **fun** : new <br> **lang**: zh\_CN <br> **_** :  时间戳 |

返回数据(String):

```
window.QRLogin.code = 200; window.QRLogin.uuid = "xxx"
```

### 2. 显示二维码（参考方法 showQrCode）

| API | 显示二维码 |
| --- | --------- |
| url | https://login.weixin.qq.com/qrcode/{uuid} |
| method | POST |
| params | **t** : webwx <br/> **_** : 时间戳|
<br>

### 3. 等待登录（参考方法 waitForLogin）这里是微信确认登录

| API | 二维码扫描登录 |
| --- | --------- |
| url | https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login |
| method | GET |
| params | **tip** : 1:未扫描  0:已扫描 <br> **uuid** : 获取到的uuid <br> **_** : 时间戳 |

返回数据(String):
```
window.code=xxx;

xxx:
	408 登陆超时
	201 扫描成功
	200 确认登录

当返回200时，还会有
window.redirect_uri="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket=xxx&uuid=xxx&lang=xxx&scan=xxx";
```

### 4. 登录获取Cookie（参考方法 login）

| API | webwxnewloginpage |
| --- | --------- |
| url | https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage |
| method | GET |
| params | **ticket** : xxx <br> **uuid** : xxx <br> **lang** : zh_CN <br> **scan** : xxx <br> **fun** : new |

返回数据(XML):
```
<error>
	<ret>0</ret>
	<message>OK</message>
	<skey>xxx</skey>
	<wxsid>xxx</wxsid>
	<wxuin>xxx</wxuin>
	<pass_ticket>xxx</pass_ticket>
	<isgrayscale>1</isgrayscale>
</error>
```
在这一步获取xml中的 `skey`, `wxsid`, `wxuin`, `pass_ticket`

### 5. 微信初始化（参考方法 wxInit）

| API | webwxinit |
| --- | --------- |
| url | https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxinit |
| method | POST |
| data | JSON |
| header | Content-Type: application/json; charset=UTF-8 |
| params | { <br> &nbsp;&nbsp;&nbsp;&nbsp; BaseRequest: { <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	Uin: xxx, <br>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Sid: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	Skey: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; DeviceID: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp; } <br> } |

返回数据(JSON):
```
{
	"BaseResponse": {
		"Ret": 0,
		"ErrMsg": ""
	},
	"Count": 11,
	"ContactList": [...],
	"SyncKey": {
		"Count": 4,
		"List": [
			{
				"Key": 1,
				"Val": 635705559
			},
			...
		]
	},
	"User": {
		"Uin": xxx,
		"UserName": xxx,
		"NickName": xxx,
		"HeadImgUrl": xxx,
		"RemarkName": "",
		"PYInitial": "",
		"PYQuanPin": "",
		"RemarkPYInitial": "",
		"RemarkPYQuanPin": "",
		"HideInputBarFlag": 0,
		"StarFriend": 0,
		"Sex": 1,
		"Signature": "Apt-get install B",
		"AppAccountFlag": 0,
		"VerifyFlag": 0,
		"ContactFlag": 0,
		"WebWxPluginSwitch": 0,
		"HeadImgFlag": 1,
		"SnsFlag": 17
	},
	"ChatSet": xxx,
	"SKey": xxx,
	"ClientVersion": 369297683,
	"SystemTime": 1453124908,
	"GrayScale": 1,
	"InviteStartCount": 40,
	"MPSubscribeMsgCount": 2,
	"MPSubscribeMsgList": [...],
	"ClickReportInterval": 600000
}
```

这一步中获取 `SyncKey`, `User` 后面的消息监听用。

### 6. 开启微信状态通知（参考方法 wxStatusNotify）

| API | webwxstatusnotify |
| --- | --------- |
| url | https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxstatusnotify |
| method | POST |
| data | JSON |
| header | Content-Type: application/json; charset=UTF-8 |
| params | { <br> &nbsp;&nbsp;&nbsp;&nbsp; BaseRequest: { Uin: xxx, Sid: xxx, Skey: xxx, DeviceID: xxx }, <br> &nbsp;&nbsp;&nbsp;&nbsp; Code: 3, <br> &nbsp;&nbsp;&nbsp;&nbsp; FromUserName: 自己的ID, <br> &nbsp;&nbsp;&nbsp;&nbsp; ToUserName: 自己的ID, <br> &nbsp;&nbsp;&nbsp;&nbsp; ClientMsgId: 时间戳 <br> } |

返回数据(JSON):
```
{
	"BaseResponse": {
		"Ret": 0,
		"ErrMsg": ""
	},
	...
}
```

### 7. 获取联系人列表（参考方法 getContact）

| API | webwxgetcontact |
| --- | --------- |
| url | https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact |
| method | POST |
| data | JSON |
| header | ContentType: application/json; charset=UTF-8 |
| params | { <br> &nbsp;&nbsp;&nbsp;&nbsp; BaseRequest: { <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	Uin: xxx, <br>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Sid: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	Skey: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; DeviceID: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp; } <br> } |

返回数据(JSON):
```
{
	"BaseResponse": {
		"Ret": 0,
		"ErrMsg": ""
	},
	"MemberCount": 334,
	"MemberList": [
		{
			"Uin": 0,
			"UserName": xxx,
			"NickName": "Urinx",
			"HeadImgUrl": xxx,
			"ContactFlag": 3,
			"MemberCount": 0,
			"MemberList": [],
			"RemarkName": "",
			"HideInputBarFlag": 0,
			"Sex": 0,
			"Signature": "我是二蛋",
			"VerifyFlag": 8,
			"OwnerUin": 0,
			"PYInitial": "URINX",
			"PYQuanPin": "Urinx",
			"RemarkPYInitial": "",
			"RemarkPYQuanPin": "",
			"StarFriend": 0,
			"AppAccountFlag": 0,
			"Statues": 0,
			"AttrStatus": 0,
			"Province": "",
			"City": "",
			"Alias": "Urinxs",
			"SnsFlag": 0,
			"UniFriend": 0,
			"DisplayName": "",
			"ChatRoomId": 0,
			"KeyWord": "gh_",
			"EncryChatRoomId": ""
		},
		...
	],
	"Seq": 0
}
```

### 8.消息检查（参考方法 syncCheck）

| API | synccheck |
| --- | --------- |
| url | https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck |
| method | GET |
| data | JSON |
| header | ContentType: application/json; charset=UTF-8 |
| params | { <br> &nbsp;&nbsp;&nbsp;&nbsp; BaseRequest: { <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	Uin: xxx, <br>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Sid: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	Skey: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; DeviceID: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp; } <br> } |

返回数据(String):
```
window.synccheck={retcode:"xxx",selector:"xxx"}

retcode:
	0 正常
	1100 失败/登出微信
selector:
	0 正常
	2 新的消息
	7 进入/离开聊天界面
```

### 9. 获取最新消息（参考方法 webwxsync）

| API | webwxsync |
| --- | --------- |
| url | https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid=xxx&skey=xxx&pass_ticket=xxx |
| method | POST |
| data | JSON |
| header | ContentType: application/json; charset=UTF-8 |
| params | { <br> &nbsp;&nbsp;&nbsp;&nbsp; BaseRequest: { Uin: xxx, Sid: xxx, Skey: xxx, DeviceID: xxx }, <br> &nbsp;&nbsp;&nbsp;&nbsp; SyncKey: xxx, <br> &nbsp;&nbsp;&nbsp;&nbsp; rr: `时间戳取反` <br> } |

返回数据(JSON):
```
{
	'BaseResponse': {'ErrMsg': '', 'Ret': 0},
	'SyncKey': {
		'Count': 7,
		'List': [
			{'Val': 636214192, 'Key': 1},
			...
		]
	},
	'ContinueFlag': 0,
	'AddMsgCount': 1,
	'AddMsgList': [
		{
			'FromUserName': '',
			'PlayLength': 0,
			'RecommendInfo': {...},
			'Content': "", 
			'StatusNotifyUserName': '',
			'StatusNotifyCode': 5,
			'Status': 3,
			'VoiceLength': 0,
			'ToUserName': '',
			'ForwardFlag': 0,
			'AppMsgType': 0,
			'AppInfo': {'Type': 0, 'AppID': ''},
			'Url': '',
			'ImgStatus': 1,
			'MsgType': 51,
			'ImgHeight': 0,
			'MediaId': '', 
			'FileName': '',
			'FileSize': '',
			...
		},
		...
	],
	'ModChatRoomMemberCount': 0,
	'ModContactList': [],
	'DelContactList': [],
	'ModChatRoomMemberList': [],
	'DelContactCount': 0,
	...
}
```

### 10. 发送消息（参考方法 webwxsendmsg）

| API | webwxsendmsg |
| --- | ------------ |
| url | https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?pass_ticket=xxx |
| method | POST |
| data | JSON |
| header | ContentType: application/json; charset=UTF-8 |
| params | { <br> &nbsp;&nbsp;&nbsp;&nbsp; BaseRequest: { Uin: xxx, Sid: xxx, Skey: xxx, DeviceID: xxx }, <br> &nbsp;&nbsp;&nbsp;&nbsp; Msg: { <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Type: 1 文字消息, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Content: 要发送的消息, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; FromUserName: 自己的ID, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ToUserName: 好友的ID, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; LocalID: 与clientMsgId相同, <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ClientMsgId: 时间戳左移4位随后补上4位随机数 <br> &nbsp;&nbsp;&nbsp;&nbsp; } <br> } |

返回数据(JSON):
```
{
	"BaseResponse": {
		"Ret": 0,
		"ErrMsg": ""
	},
	...
}
```

更多资料：
https://github.com/xiangzhai/qwx
https://github.com/Urinx/WeixinBot
http://www.07net01.com/2016/01/1201188.html
http://www.cnblogs.com/xiaozhi_5638/p/4923811.html
