package me.biezhi.wechat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;

import me.biezhi.wechat.Constant;
import me.biezhi.wechat.exception.WechatException;
import me.biezhi.wechat.model.WechatContact;
import me.biezhi.wechat.model.WechatMeta;
import me.biezhi.wechat.model.WechatRequest;
import me.biezhi.wechat.robot.MoLiRobot;
import me.biezhi.wechat.robot.Robot;
import me.biezhi.wechat.util.Matchers;

public class WechatServiceImpl implements WechatService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatService.class);
	
	private Robot robot = new MoLiRobot();
	
	@Override
	public WechatContact getContact(WechatMeta wechatMeta, WechatRequest wechatRequest) {
		
		String url = Constant.BASE_URL + "/webwxgetcontact?pass_ticket=" + wechatMeta.getPass_ticket() 
			+ "&skey=" + wechatMeta.getSkey() + "&r=" + DateKit.getCurrentUnixTime();
		
		JSONObject body = new JSONObject();
		body.put("BaseRequest", wechatMeta.getBaseRequest());
		
		HttpRequest request = HttpRequest.post(url)
				.header("Content-Type", "application/json;charset=utf-8")
				.header("Cookie", wechatMeta.getCookie())
				.send(body.toString());
		
		LOGGER.info("[*] " + request);
		String res = request.body();
		request.disconnect();

		if(StringKit.isBlank(res)){
			return null;
		}
		
		WechatContact wechatContact = new WechatContact();
		 
		try {
			JSONObject jsonObject = JSONKit.parseObject(res);
			JSONObject BaseResponse = jsonObject.get("BaseResponse").asJSONObject();
			if(null != BaseResponse){
				int ret = BaseResponse.getInt("Ret", -1);
				if(ret == 0){
					JSONArray MemberList = jsonObject.get("MemberList").asArray();
					JSONArray ContactList = new JSONArray();
					if(null != MemberList){
						for(int i=0, len=MemberList.size(); i<len; i++){
							JSONObject contact = MemberList.get(i).asJSONObject();
							//公众号/服务号
							if(contact.getInt("VerifyFlag", 0) == 8){
								continue;
							}
							//特殊联系人
							if(Constant.SpecialUsers.contains(contact.getString("UserName"))){
								continue;
							}
							//群聊
							if(contact.getString("UserName").indexOf("@@") != -1){
								continue;
							}
							//自己
							if(contact.getString("UserName").equals(wechatRequest.getUser().getString("UserName"))){
								continue;
							}
							ContactList.add(contact);
						}
						
						wechatContact.setContactList(ContactList);
						wechatContact.setMemberList(MemberList);
						
						return wechatContact;
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
	

	@Override
	public String getUUID() throws WechatException{
		HttpRequest request = HttpRequest.get(Constant.JS_LOGIN_URL, true, 
				"appid", "wx782c26e4c19acffb", 
				"fun", "new",
				"lang", "zh_CN",
				"_" , DateKit.getCurrentUnixTime());
		
		LOGGER.info("[*] " + request);
		
		String res = request.body();
		request.disconnect();

		if(StringKit.isNotBlank(res)){
			String code = Matchers.match("window.QRLogin.code = (\\d+);", res);
			if(null != code){
				if(code.equals("200")){
					return Matchers.match("window.QRLogin.uuid = \"(.*)\";", res);
				} else {
					throw new WechatException("错误的状态码: " + code);
				}
			}
		}
		throw new WechatException("获取UUID失败");
	}

	@Override
	public boolean openStatusNotify(WechatMeta wechatMeta, WechatRequest wechatRequest) {
		
		String url = Constant.BASE_URL + "/webwxstatusnotify?lang=zh_CN&pass_ticket=" + wechatMeta.getPass_ticket();
		
		JSONObject body = new JSONObject();
		body.put("BaseRequest", wechatMeta.getBaseRequest());
		body.put("Code", 3);
		body.put("FromUserName", wechatRequest.getUser().getString("UserName"));
		body.put("ToUserName", wechatRequest.getUser().getString("UserName"));
		body.put("ClientMsgId", DateKit.getCurrentUnixTime());
		
		HttpRequest request = HttpRequest.post(url)
				.header("Content-Type", "application/json;charset=utf-8")
				.header("Cookie", wechatMeta.getCookie())
				.send(body.toString());
		
		LOGGER.info("[*] " + request);
		String res = request.body();
		request.disconnect();

		if(StringKit.isBlank(res)){
			return false;
		}
		
		try {
			JSONObject jsonObject = JSONKit.parseObject(res);
			JSONObject BaseResponse = jsonObject.get("BaseResponse").asJSONObject();
			if(null != BaseResponse){
				int ret = BaseResponse.getInt("Ret", -1);
				return ret == 0;
			}
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public WechatRequest wxInit(WechatMeta wechatMeta) throws WechatException {
		String url = Constant.BASE_URL + "/webwxinit?r=" + DateKit.getCurrentUnixTime() + "&pass_ticket=" + wechatMeta.getPass_ticket() +
				"&skey=" + wechatMeta.getSkey();
		
		JSONObject body = new JSONObject();
		body.put("BaseRequest", wechatMeta.getBaseRequest());
		
		WechatRequest wechatRequest = new WechatRequest();
		
		HttpRequest request = HttpRequest.post(url)
				.header("Content-Type", "application/json;charset=utf-8")
				.header("Cookie", wechatMeta.getCookie())
				.send(body.toString());
		
		LOGGER.info("[*] " + request);
		String res = request.body();
		request.disconnect();
		
		if(StringKit.isBlank(res)){
			return wechatRequest;
		}
		
		try {
			JSONObject jsonObject = JSONKit.parseObject(res);
			if(null != jsonObject){
				JSONObject BaseResponse = jsonObject.get("BaseResponse").asJSONObject();
				if(null != BaseResponse){
					int ret = BaseResponse.getInt("Ret", -1);
					if(ret == 0){
						wechatRequest.setSyncKey(jsonObject.get("SyncKey").asJSONObject());
						wechatRequest.setUser(jsonObject.get("User").asJSONObject());
						
						StringBuffer synckey = new StringBuffer();
						JSONArray list = wechatRequest.getSyncKey().get("List").asArray();
						for(int i=0, len=list.size(); i<len; i++){
							JSONObject item = list.get(i).asJSONObject();
							synckey.append("|" + item.getInt("Key", 0) + "_" + item.getInt("Val", 0));
						}
						wechatMeta.setSynckey(synckey.substring(1));
					}
				}
			}
		} catch (Exception e) {
		}
		return wechatRequest;
	}


	@Override
	public int[] syncCheck(String webpush_url, WechatMeta wechatMeta) {
		
		String url = webpush_url + "/synccheck";
		JSONObject body = new JSONObject();
		body.put("BaseRequest", wechatMeta.getBaseRequest());
		HttpRequest request = HttpRequest.get(url, true,
				"r", DateKit.getCurrentUnixTime() + StringKit.getRandomNumber(5),
				"skey", wechatMeta.getSkey(),
				"uin", wechatMeta.getWxuin(),
				"sid", wechatMeta.getWxsid(),
				"deviceid", wechatMeta.getDeviceId(),
				"synckey", wechatMeta.getSynckey(),
				"_", System.currentTimeMillis())
				.header("Cookie", wechatMeta.getCookie());
		
		LOGGER.info("[*] " + request);
		String res = request.body();
		request.disconnect();

		int[] arr = new int[2];
		if(StringKit.isBlank(res)){
			return arr;
		}
		
		String retcode = Matchers.match("retcode:\"(\\d+)\",", res);
		String selector = Matchers.match("selector:\"(\\d+)\"}", res);
		if(null != retcode && null != selector){
			arr[0] = Integer.parseInt(retcode);
			arr[1] = Integer.parseInt(selector);
			return arr;
		}
		return arr;
	}


	@Override
	public void handleMsg(WechatMeta wechatMeta, WechatRequest wechatRequest, WechatContact contact, JSONObject data) {
		if(null == data){
			return;
		}
		
		JSONArray AddMsgList = data.get("AddMsgList").asArray();
		
		for(int i=0,len=AddMsgList.size(); i<len; i++){
			LOGGER.info("[*] 你有新的消息，请注意查收");
			JSONObject msg = AddMsgList.get(i).asJSONObject();
			int msgType = msg.getInt("MsgType", 0);
			String name = getUserRemarkName(contact, msg.getString("FromUserName"));
			String content = msg.getString("Content");
			
			if(msgType == 51){
				LOGGER.info("[*] 成功截获微信初始化消息");
			} else if(msgType == 1){
				if(Constant.SpecialUsers.contains(msg.getString("ToUserName"))){
					continue;
				} else if(msg.getString("FromUserName").equals(wechatRequest.getUser().getString("UserName"))){
					continue;
				} else if (msg.getString("ToUserName").indexOf("@@") != -1) {
					String[] peopleContent = content.split(":<br/>");
					LOGGER.info("|" + name + "| " + peopleContent[0] + ":\n" + peopleContent[1].replace("<br/>", "\n"));
				} else {
					LOGGER.info(name + ": " + content);
					String ans = robot.talk(content);
					webwxsendmsg(wechatMeta, wechatRequest, ans, msg.getString("FromUserName"));
					LOGGER.info("自动回复 " + ans);
				}
			} else if(msgType == 3){
				webwxsendmsg(wechatMeta, wechatRequest, "二蛋还不支持图片呢", msg.getString("FromUserName"));
			} else if(msgType == 34){
				webwxsendmsg(wechatMeta, wechatRequest, "二蛋还不支持语音呢", msg.getString("FromUserName"));
			} else if(msgType == 42){
				LOGGER.info(name + " 给你发送了一张名片:");
				LOGGER.info("=========================");
			}
		}
	}
	
	private void webwxsendmsg(WechatMeta meta, WechatRequest wechatRequest,  String content, String to) {
		
		String url = Constant.BASE_URL + "/webwxsendmsg?lang=zh_CN&pass_ticket=" + meta.getPass_ticket();
		
		JSONObject body = new JSONObject();
		
		String clientMsgId = DateKit.getCurrentUnixTime() + StringKit.getRandomNumber(5);
		JSONObject Msg = new JSONObject();
		Msg.put("Type", 1);
		Msg.put("Content", content);
		Msg.put("FromUserName", wechatRequest.getUser().getString("UserName"));
		Msg.put("ToUserName", to);
		Msg.put("LocalID", clientMsgId);
		Msg.put("ClientMsgId", clientMsgId);
		
		body.put("BaseRequest", meta.getBaseRequest());
		body.put("Msg", Msg);
		
		HttpRequest request = HttpRequest.post(url)
				.header("Content-Type", "application/json;charset=utf-8")
				.header("Cookie", meta.getCookie())
				.send(body.toString());
		
		LOGGER.info("[*] " + request);
		request.body();
		request.disconnect();
	}
	
	
	private String getUserRemarkName(WechatContact contact, String id) {
		String name = "这个人物名字未知";
		for(int i=0, len=contact.getMemberList().size(); i<len; i++){
			JSONObject member = contact.getMemberList().get(i).asJSONObject();
			if(member.getString("UserName").equals(id)){
				if(StringKit.isNotBlank(member.getString("RemarkName"))){
					name = member.getString("RemarkName");
				} else {
					name = member.getString("NickName");
				}
				return name;
			}
		}
		return name;
	}


	@Override
	public JSONObject webwxsync(WechatMeta meta, WechatRequest wechatRequest) {
		String url = Constant.BASE_URL + "/webwxsync?lang=zh_CN&pass_ticket=" + meta.getPass_ticket()
				 + "&skey=" + meta.getSkey() + "&sid=" + meta.getWxsid() + "&r=" + DateKit.getCurrentUnixTime();
		
		JSONObject body = new JSONObject();
		body.put("BaseRequest", meta.getBaseRequest());
		body.put("SyncKey", meta.getSynckey());
		body.put("rr", DateKit.getCurrentUnixTime());
		
		HttpRequest request = HttpRequest.post(url)
				.header("Content-Type", "application/json;charset=utf-8")
				.header("Cookie", meta.getCookie())
				.send(body.toString());
		
		LOGGER.info("[*] " + request);
		String res = request.body();
		request.disconnect();
		
		if(StringKit.isBlank(res)){
			return null;
		}
		
		JSONObject jsonObject = JSONKit.parseObject(res);
		JSONObject BaseResponse = jsonObject.get("BaseResponse").asJSONObject();
		if(null != BaseResponse){
			int ret = BaseResponse.getInt("Ret", -1);
			if(ret == 0){
				
				wechatRequest.setSyncKey(jsonObject.get("SyncKey").asJSONObject());
				
				StringBuffer synckey = new StringBuffer();
				JSONArray list = wechatRequest.getSyncKey().get("List").asArray();
				for(int i=0, len=list.size(); i<len; i++){
					JSONObject item = list.get(i).asJSONObject();
					synckey.append("|" + item.getInt("Key", 0) + "_" + item.getInt("Val", 0));
				}
				meta.setSynckey(synckey.substring(1));
			}
		}
		return jsonObject;
	}

}
