package io.github.biezhi.wechat.service;

import com.github.kevinsawicki.http.HttpRequest;
import io.github.biezhi.wechat.Constant;
import io.github.biezhi.wechat.exception.WechatException;
import io.github.biezhi.wechat.model.entity.SyncKeyEntity;
import io.github.biezhi.wechat.model.entity.WechatContact;
import io.github.biezhi.wechat.model.entity.WechatMeta;
import io.github.biezhi.wechat.model.entity.WechatUser;
import io.github.biezhi.wechat.model.request.*;
import io.github.biezhi.wechat.model.response.BaseResponse;
import io.github.biezhi.wechat.model.response.WebwxsyncResponse;
import io.github.biezhi.wechat.model.response.WechatResponse;
import io.github.biezhi.wechat.robot.MoLiRobot;
import io.github.biezhi.wechat.robot.Robot;
import io.github.biezhi.wechat.util.JsonUtil;
import io.github.biezhi.wechat.util.Matchers;
import io.github.biezhi.wechat.util.PingUtil;
import io.github.biezhi.wechat.util.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WechatServiceImpl implements WechatService {

    private static final Logger log = LoggerFactory.getLogger(WechatService.class);

    // 茉莉机器人
    private Robot robot = new MoLiRobot();

    /**
     * 获取联系人
     */
    @Override
    public WechatContact getContact(WechatMeta wechatMeta) throws WechatException {
        String url = wechatMeta.getBase_uri() + "/webwxgetcontact?pass_ticket=" + wechatMeta.getPass_ticket() + "&skey="
                + wechatMeta.getSkey() + "&r=" + System.currentTimeMillis();

        WechatRequest wechatRequest = new WechatRequest();
        wechatRequest.setBaseRequest(wechatMeta.getBaseRequest());

        String body = JsonUtil.toJson(wechatRequest);

        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", wechatMeta.getCookie()).send(body);

        log.debug(request.toString());
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            throw new WechatException("获取联系人失败");
        }

        log.debug(res);

        WechatContact wechatContact = new WechatContact();
        try {

            WechatResponse wechatResponse = JsonUtil.fromJson(res, WechatResponse.class);
            if (wechatResponse.getBaseResponse().getRet() == 0) {
                List<WechatUser> memberList = wechatResponse.getMemberList();
                List<WechatUser> contactList = wechatResponse.getContactList();

                if (null != memberList) {
                    for (int i = 0, len = memberList.size(); i < len; i++) {
                        WechatUser contact = memberList.get(i);
                        // 公众号/服务号
                        if (contact.getVerifyFlag() == 8) {
                            continue;
                        }
                        // 特殊联系人
                        if (Constant.FILTER_USERS.contains(contact.getUserName())) {
                            continue;
                        }
                        // 群聊
                        if (contact.getUserName().indexOf("@@") != -1) {
                            continue;
                        }
                        // 自己
                        if (contact.getUserName().equals(wechatMeta.getUser().getUserName())) {
                            continue;
                        }
                        contactList.add(contact);
                    }
                    wechatContact.setContactList(contactList);
                    wechatContact.setMemberList(memberList);
                    this.getGroup(wechatMeta, wechatContact);
                    return wechatContact;
                }
            }
        } catch (Exception e) {
            throw new WechatException(e);
        }
        return null;
    }

    private void getGroup(WechatMeta wechatMeta, WechatContact wechatContact) throws WechatException {
        String url = wechatMeta.getBase_uri() + "/webwxbatchgetcontact?type=ex&pass_ticket=" + wechatMeta.getPass_ticket() + "&skey="
                + wechatMeta.getSkey() + "&r=" + System.currentTimeMillis();

        WechatRequest wechatRequest = new WechatRequest();
        wechatRequest.setBaseRequest(wechatMeta.getBaseRequest());

        String body = JsonUtil.toJson(wechatContact);

        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", wechatMeta.getCookie()).send(body);

        log.debug(request.toString());
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            throw new WechatException("获取群信息失败");
        }

        log.debug(res);
        try {

            WechatResponse wechatResponse = JsonUtil.fromJson(res, WechatResponse.class);

            BaseResponse baseResponse = wechatResponse.getBaseResponse();
            if (baseResponse.getRet() == 0) {
                List<WechatUser> memberList = baseResponse.getMemberList();
                List<WechatUser> contactList = new ArrayList<WechatUser>();
                if (null != memberList) {
                    for (WechatUser user : memberList) {
                        // 公众号/服务号
                        if (user.getVerifyFlag() == 8) {
                            continue;
                        }
                        // 特殊联系人
                        if (Constant.FILTER_USERS.contains(user.getUserName())) {
                            continue;
                        }
                        // 群聊
                        if (user.getUserName().indexOf("@@") != -1) {
                            continue;
                        }
                        // 自己
                        if (user.getUserName().equals(wechatMeta.getUser().getUserName())) {
                            continue;
                        }
                        contactList.add(user);
                    }
                    wechatContact.setContactList(contactList);
                    wechatContact.setMemberList(memberList);
                }
            }
        } catch (Exception e) {
            throw new WechatException(e);
        }
    }

    /**
     * 获取UUID
     */
    @Override
    public String getUUID() throws WechatException {

        HttpRequest request = HttpRequest.get(Constant.JS_LOGIN_URL, true, "appid", "wx782c26e4c19acffb", "fun", "new",
                "lang", "zh_CN", "_", System.currentTimeMillis());

        log.debug(request.toString());

        String res = request.body();
        request.disconnect();

        if (StringKit.isNotBlank(res)) {
            String code = Matchers.match("window.QRLogin.code = (\\d+);", res);
            if (null != code) {
                if (code.equals("200")) {
                    return Matchers.match("window.QRLogin.uuid = \"(.*)\";", res);
                } else {
                    throw new WechatException("错误的状态码: " + code);
                }
            }
        }
        throw new WechatException("获取UUID失败");
    }

    /**
     * 打开状态提醒
     */
    @Override
    public void openStatusNotify(WechatMeta wechatMeta) throws WechatException {

        String url = wechatMeta.getBase_uri() + "/webwxstatusnotify?lang=zh_CN&pass_ticket=" + wechatMeta.getPass_ticket();

        StatusNotifyRequest statusNotifyRequest = new StatusNotifyRequest();
        statusNotifyRequest.setBaseRequest(wechatMeta.getBaseRequest());
        statusNotifyRequest.setCode(3);
        statusNotifyRequest.setFromUserName(wechatMeta.getUser().getUserName());
        statusNotifyRequest.setToUserName(wechatMeta.getUser().getUserName());
        statusNotifyRequest.setClientMsgId(System.currentTimeMillis());

        String body = JsonUtil.toJson(statusNotifyRequest);

        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", wechatMeta.getCookie()).send(body.toString());

        log.debug("" + request);
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            throw new WechatException("状态通知开启失败");
        }
        try {
            WechatResponse wechatResponse = JsonUtil.fromJson(res, WechatResponse.class);
            BaseResponse baseResponse = wechatResponse.getBaseResponse();
            if (baseResponse.getRet() != 0) {
                throw new WechatException("状态通知开启失败，ret：" + baseResponse.getRet());
            }
        } catch (Exception e) {
            throw new WechatException(e);
        }
    }

    /**
     * 微信初始化
     */
    @Override
    public void wxInit(WechatMeta wechatMeta) throws WechatException {
        String url = wechatMeta.getBase_uri() + "/webwxinit?r=" + System.currentTimeMillis() + "&pass_ticket="
                + wechatMeta.getPass_ticket() + "&skey=" + wechatMeta.getSkey();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("BaseRequest", wechatMeta.getBaseRequest());
        String body = JsonUtil.toJson(map);

        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", wechatMeta.getCookie()).send(body);

        log.debug("" + request);
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            throw new WechatException("微信初始化失败");
        }

        WechatResponse wechatResponse = JsonUtil.fromJson(res, WechatResponse.class);
        BaseResponse baseResponse = wechatResponse.getBaseResponse();
        if (baseResponse.getRet() == 0) {
            wechatMeta.setSyncKey(baseResponse.getSyncKey());
            wechatMeta.setUser(baseResponse.getUser());

            StringBuffer synckey = new StringBuffer();
            List<SyncKeyEntity> list = wechatMeta.getSyncKey().getList();
            for (SyncKeyEntity item : list) {
                synckey.append("|" + item.getKey() + "_" + item.getVal());
            }
            wechatMeta.setSynckey(synckey.substring(1));
        }
    }

    /**
     * 选择同步线路
     */
    @Override
    public void choiceSyncLine(WechatMeta wechatMeta) throws WechatException {
        boolean enabled = false;
        for (String syncUrl : Constant.SYNC_HOST) {
            int[] res = this.syncCheck(syncUrl, wechatMeta);
            if (res[0] == 0) {
                String url = "https://" + syncUrl + "/cgi-bin/mmwebwx-bin";
                wechatMeta.setWebpush_url(url);
                log.info("选择线路：[{}]", syncUrl);
                enabled = true;
                break;
            }
        }
        if (!enabled) {
            throw new WechatException("同步线路不通畅");
        }
    }

    /**
     * 检测心跳
     */
    @Override
    public int[] syncCheck(WechatMeta wechatMeta) {
        return this.syncCheck(null, wechatMeta);
    }

    /**
     * 检测心跳
     */
    private int[] syncCheck(String url, WechatMeta meta) {

        // 如果网络中断，休息10秒
        if (PingUtil.netIsOver()) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                log.error("", e);
            }
        }

        if (null == url) {
            url = meta.getWebpush_url() + "/synccheck";
        } else {
            url = "https://" + url + "/cgi-bin/mmwebwx-bin/synccheck";
        }

        WechatRequest wechatRequest = new WechatRequest();
        wechatRequest.setBaseRequest(meta.getBaseRequest());

        String body = JsonUtil.toJson(wechatRequest);

        HttpRequest request = HttpRequest
                .get(url, true, "r", System.currentTimeMillis() + StringKit.getRandomNumber(5), "skey",
                        meta.getSkey(), "uin", meta.getWxuin(), "sid", meta.getWxsid(), "deviceid",
                        meta.getDeviceId(), "synckey", meta.getSynckey(), "_", System.currentTimeMillis())
                .header("Cookie", meta.getCookie());

        log.debug(request.toString());

        String res = request.body();
        request.disconnect();

        int[] arr = new int[]{-1, -1};
        if (StringKit.isBlank(res)) {
            return arr;
        }

        String retcode = Matchers.match("retcode:\"(\\d+)\",", res);
        String selector = Matchers.match("selector:\"(\\d+)\"}", res);
        if (null != retcode && null != selector) {
            arr[0] = Integer.parseInt(retcode);
            arr[1] = Integer.parseInt(selector);
            return arr;
        }
        return arr;
    }

    /**
     * 处理消息
     */
    @Override
    public void handleMsg(WechatMeta wechatMeta, MessageRequest messageRequest) {
        if (null == messageRequest) {
            return;
        }

        List<AddMessage> addMsgList = messageRequest.getAddMsgList();

        for (int i = 0, len = addMsgList.size(); i < len; i++) {
            log.info("你有新的消息，请注意查收");

            AddMessage addMessage = addMsgList.get(i);
            int msgType = addMessage.getMsgType();

            String name = getUserRemarkName(addMessage.getFromUserName());
            String content = addMessage.getContent();

            if (msgType == 51) {
                log.info("成功截获微信初始化消息");
            } else if (msgType == 1) {
                if (Constant.FILTER_USERS.contains(addMessage.getToUserName())) {
                    continue;
                } else if (addMessage.getFromUserName().equals(wechatMeta.getUser().getUserName())) {
                    continue;
                } else if (addMessage.getToUserName().indexOf("@@") != -1) {
                    String[] peopleContent = content.split(":<br/>");
                    log.info("|" + name + "| " + peopleContent[0] + ":\n" + peopleContent[1].replace("<br/>", "\n"));
                } else {
                    log.info(name + ": " + content);
                    String ans = robot.talk(content);
                    webwxsendmsg(wechatMeta, ans, addMessage.getFromUserName());
                    log.info("自动回复 " + ans);
                }
            } else if (msgType == 3) {
                String imgDir = Constant.environment.get("app.img_path");
                String msgId = addMessage.getMsgId();
                new File(imgDir).mkdir();
                String imgUrl = wechatMeta.getBase_uri() + "/webwxgetmsgimg?MsgID=" + msgId + "&skey=" + wechatMeta.getSkey() + "&type=slave";
                HttpRequest.get(imgUrl).header("Cookie", wechatMeta.getCookie()).receive(new File(imgDir + "/" + msgId + ".jpg"));
                webwxsendmsg(wechatMeta, "二蛋还不支持图片呢", addMessage.getFromUserName());
            } else if (msgType == 34) {
                webwxsendmsg(wechatMeta, "二蛋还不支持语音呢", addMessage.getFromUserName());
            } else if (msgType == 42) {
                log.info(name + " 给你发送了一张名片:");
                log.info("=========================");
            }
        }
    }

    /**
     * 发送消息
     */
    private void webwxsendmsg(WechatMeta meta, String content, String to) {
        String url = meta.getBase_uri() + "/webwxsendmsg?lang=zh_CN&pass_ticket=" + meta.getPass_ticket();
        Map<String, Object> map = new HashMap<String, Object>();

        String clientMsgId = System.currentTimeMillis() / 1000 + "_" + StringKit.getRandomNumber(5);

        WebSendMsgRequest webSendMsgRequest = new WebSendMsgRequest();
        webSendMsgRequest.setType(1);
        webSendMsgRequest.setContent(content);
        webSendMsgRequest.setFromUserName(meta.getUser().getUserName());
        webSendMsgRequest.setToUserName(to);
        webSendMsgRequest.setLocalID(clientMsgId);
        webSendMsgRequest.setClientMsgId(clientMsgId);

        map.put("BaseRequest", meta.getBaseRequest());
        map.put("Msg", webSendMsgRequest);

        String body = JsonUtil.toJson(map);

        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", meta.getCookie()).send(body);

        log.info("发送消息...");
        log.debug("" + request);
        request.body();
        request.disconnect();
    }

    private String getUserRemarkName(String id) {
        String name = "这个人物名字未知";
        for (int i = 0, len = Constant.CONTACT.getMemberList().size(); i < len; i++) {
            WechatUser member = Constant.CONTACT.getMemberList().get(i);
            if (member.getUserName().equals(id)) {
                if (StringKit.isNotBlank(member.getRemarkName())) {
                    name = member.getRemarkName();
                } else {
                    name = member.getNickName();
                }
                return name;
            }
        }
        return name;
    }

    @Override
    public BaseResponse webwxsync(WechatMeta meta) throws WechatException {

        String url = meta.getBase_uri() + "/webwxsync?skey=" + meta.getSkey() + "&sid=" + meta.getWxsid();

        WebwxsyncRequest webwxsyncRequest = new WebwxsyncRequest();
        webwxsyncRequest.setBaseRequest(meta.getBaseRequest());
        webwxsyncRequest.setSyncKey(meta.getSyncKey());

        String body = JsonUtil.toJson(webwxsyncRequest);

        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", meta.getCookie()).send(body);

        log.debug(request.toString());
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            throw new WechatException("同步syncKey失败");
        }

        WebwxsyncResponse webwxsyncResponse = JsonUtil.fromJson(res, WebwxsyncResponse.class);
        int ret = webwxsyncResponse.getRet();
        if (ret == 0) {
            return webwxsyncResponse.getBaseResponse();
        }
        return null;
    }

}
