package io.github.biezhi.wechat.api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.Utils;
import io.github.biezhi.wechat.model.Const;
import io.github.biezhi.wechat.model.Environment;
import io.github.biezhi.wechat.model.Session;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 微信API实现
 *
 * @author biezhi
 * 16/06/2017
 */
public class WechatApi {

    private static final Logger log = LoggerFactory.getLogger(WechatApi.class);

    // 配置文件环境参数
    protected Environment environment;

    protected String appid = "wx782c26e4c19acffb";
    protected String wxHost;

    // 微信配置信息
    protected Map<String, String> conf = new HashMap<String, String>();

    protected String wxFileHost;
    protected String redirectUri;

    // 登录会话
    protected Session session;

    protected Map<String, Object> baseRequest;

    protected JsonObject synckeyDic;
    protected String     synckey;

    // device_id: 登录手机设备
    // web wechat 的格式为: e123456789012345 (e+15位随机数)
    // mobile wechat 的格式为: A1234567890abcde (A+15位随机数字或字母)
    protected String deviceId = "e" + System.currentTimeMillis();

    protected String userAgent = Const.API_USER_AGENT[new Random().nextInt(2)];
    protected String cookie;

    // 登陆账号信息
    protected Map<String, Object> user;

    // 好友+群聊+公众号+特殊账号
    protected JsonArray memberList;

    protected int memberCount;

    // 好友
    protected JsonArray contactList;

    // 群
    protected JsonArray groupList;

    // 群聊成员字典 {group_id:[]}
    protected Map<String, JsonArray> groupMemeberList = new HashMap<String, JsonArray>();

    // 公众号／服务号
    protected JsonArray publicUsersList;

    // 特殊账号
    protected JsonArray specialUsersList;

    // 读取、连接、发送超时时长，单位/秒
    private int readTimeout, connTimeout, writeTimeout;

    public WechatApi(Environment environment) {
        System.setProperty("https.protocols", "TLSv1");
        System.setProperty("jsse.enableSNIExtension", "false");
        this.wxHost = environment.get("wxHost", "wx.qq.com");
        this.connTimeout = environment.getInt("http.conn-time-out", 10);
        this.readTimeout = environment.getInt("http.read-time-out", 10);
        this.writeTimeout = environment.getInt("http.write-time-out", 10);
        this.conf_factory();
    }

    private void conf_factory() {
        // wx.qq.com
        String e = this.wxHost;
        String t = "login.weixin.qq.com";
        String o = "file.wx.qq.com";
        String n = "webpush.weixin.qq.com";

        if (e.indexOf("wx2.qq.com") > -1) {
            t = "login.wx2.qq.com";
            o = "file.wx2.qq.com";
            n = "webpush.wx2.qq.com";
        } else if (e.indexOf("wx8.qq.com") > -1) {
            t = "login.wx8.qq.com";
            o = "file.wx8.qq.com";
            n = "webpush.wx8.qq.com";
        } else if (e.indexOf("qq.com") > -1) {
            t = "login.wx.qq.com";
            o = "file.wx.qq.com";
            n = "webpush.wx.qq.com";
        } else if (e.indexOf("web2.wechat.com") > -1) {
            t = "login.web2.wechat.com";
            o = "file.web2.wechat.com";
            n = "webpush.web2.wechat.com";
        } else if (e.indexOf("wechat.com") > -1) {
            t = "login.web.wechat.com";
            o = "file.web.wechat.com";
            n = "webpush.web.wechat.com";
        }
        conf.put("LANG", "zh_CN");
        conf.put("API_jsLogin", "https://login.weixin.qq.com/jslogin");
        conf.put("API_qrcode", "https://login.weixin.qq.com/l/");
        conf.put("API_qrcode_img", "https://login.weixin.qq.com/qrcode/");

        conf.put("API_login", "https://" + e + "/cgi-bin/mmwebwx-bin/login");
        conf.put("API_synccheck", "https://" + n + "/cgi-bin/mmwebwx-bin/synccheck");
        conf.put("API_webwxdownloadmedia", "https://" + o + "/cgi-bin/mmwebwx-bin/webwxgetmedia");
        conf.put("API_webwxuploadmedia", "https://" + o + "/cgi-bin/mmwebwx-bin/webwxuploadmedia");
        conf.put("API_webwxpreview", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxpreview");
        conf.put("API_webwxinit", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxinit");
        conf.put("API_webwxgetcontact", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxgetcontact");
        conf.put("API_webwxsync", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxsync");
        conf.put("API_webwxbatchgetcontact", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxbatchgetcontact");
        conf.put("API_webwxgeticon", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxgeticon");
        conf.put("API_webwxsendmsg", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxsendmsg");
        conf.put("API_webwxsendmsgimg", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxsendmsgimg");
        conf.put("API_webwxsendmsgvedio", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxsendvideomsg");
        conf.put("API_webwxsendemoticon", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxsendemoticon");
        conf.put("API_webwxsendappmsg", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxsendappmsg");
        conf.put("API_webwxgetheadimg", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxgetheadimg");
        conf.put("API_webwxgetmsgimg", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxgetmsgimg");
        conf.put("API_webwxgetmedia", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxgetmedia");
        conf.put("API_webwxgetvideo", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxgetvideo");
        conf.put("API_webwxlogout", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxlogout");
        conf.put("API_webwxgetvoice", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxgetvoice");
        conf.put("API_webwxupdatechatroom", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxupdatechatroom");
        conf.put("API_webwxcreatechatroom", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxcreatechatroom");
        conf.put("API_webwxstatusnotify", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxstatusnotify");
        conf.put("API_webwxcheckurl", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxcheckurl");
        conf.put("API_webwxverifyuser", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxverifyuser");
        conf.put("API_webwxfeedback", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxsendfeedback");
        conf.put("API_webwxreport", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxstatreport");
        conf.put("API_webwxsearch", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxsearchcontact");
        conf.put("API_webwxoplog", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxoplog");
        conf.put("API_checkupload", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxcheckupload");
        conf.put("API_webwxrevokemsg", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxrevokemsg");
        conf.put("API_webwxpushloginurl", "https://" + e + "/cgi-bin/mmwebwx-bin/webwxpushloginurl");

        conf.put("CONTACTFLAG_CONTACT", "1");
        conf.put("CONTACTFLAG_CHATCONTACT", "2");
        conf.put("CONTACTFLAG_CHATROOMCONTACT", "4");
        conf.put("CONTACTFLAG_BLACKLISTCONTACT", "8");
        conf.put("CONTACTFLAG_DOMAINCONTACT", "16");
        conf.put("CONTACTFLAG_HIDECONTACT", "32");
        conf.put("CONTACTFLAG_FAVOURCONTACT", "64");
        conf.put("CONTACTFLAG_3RDAPPCONTACT", "128");
        conf.put("CONTACTFLAG_SNSBLACKLISTCONTACT", "256");
        conf.put("CONTACTFLAG_NOTIFYCLOSECONTACT", "512");
        conf.put("CONTACTFLAG_TOPCONTACT", "2048");
        conf.put("MSGTYPE_TEXT", "1");
        conf.put("MSGTYPE_IMAGE", "3");
        conf.put("MSGTYPE_VOICE", "34");
        conf.put("MSGTYPE_VIDEO", "43");
        conf.put("MSGTYPE_MICROVIDEO", "62");
        conf.put("MSGTYPE_EMOTICON", "47");
        conf.put("MSGTYPE_APP", "49");
        conf.put("MSGTYPE_VOIPMSG", "50");
        conf.put("MSGTYPE_VOIPNOTIFY", "52");
        conf.put("MSGTYPE_VOIPINVITE", "53");
        conf.put("MSGTYPE_LOCATION", "48");
        conf.put("MSGTYPE_STATUSNOTIFY", "51");
        conf.put("MSGTYPE_SYSNOTICE", "9999");
        conf.put("MSGTYPE_POSSIBLEFRIEND_MSG", "40");
        conf.put("MSGTYPE_VERIFYMSG", "37");
        conf.put("MSGTYPE_SHARECARD", "42");
        conf.put("MSGTYPE_SYS", "10000");
        conf.put("MSGTYPE_RECALLED", "10002");
        conf.put("APPMSGTYPE_TEXT", "1");
        conf.put("APPMSGTYPE_IMG", "2");
        conf.put("APPMSGTYPE_AUDIO", "3");
        conf.put("APPMSGTYPE_VIDEO", "4");
        conf.put("APPMSGTYPE_URL", "5");
        conf.put("APPMSGTYPE_ATTACH", "6");
        conf.put("APPMSGTYPE_OPEN", "7");
        conf.put("APPMSGTYPE_EMOJI", "8");
        conf.put("APPMSGTYPE_VOICE_REMIND", "9");
        conf.put("APPMSGTYPE_SCAN_GOOD", "10");
        conf.put("APPMSGTYPE_GOOD", "13");
        conf.put("APPMSGTYPE_EMOTION", "15");
        conf.put("APPMSGTYPE_CARD_TICKET", "16");
        conf.put("APPMSGTYPE_REALTIME_SHARE_LOCATION", "17");
        conf.put("APPMSGTYPE_TRANSFERS", "2e3");
        conf.put("APPMSGTYPE_RED_ENVELOPES", "2001");
        conf.put("APPMSGTYPE_READER_TYPE", "100001");
        conf.put("UPLOAD_MEDIA_TYPE_IMAGE", "1");
        conf.put("UPLOAD_MEDIA_TYPE_VIDEO", "2");
        conf.put("UPLOAD_MEDIA_TYPE_AUDIO", "3");
        conf.put("UPLOAD_MEDIA_TYPE_ATTACHMENT", "4");
    }

    /**
     * 获取uuid
     *
     * @return
     */
    public boolean getUUID() {
        String              url    = conf.get("API_jsLogin");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", appid);
        params.put("fun", "new");
        params.put("lang", conf.get("LANG"));
        params.put("_", System.currentTimeMillis() + "");

        String response = doGet(url, params);
        if (Utils.isBlank(response)) {
            log.warn("获取UUID失败");
            return false;
        }

        String code = Utils.match("window.QRLogin.code = (\\d+);", response);
        if (Utils.isBlank(code)) {
            log.warn("获取UUID失败");
            return false;
        }

        if (!code.equals("200")) {
            log.warn("错误的状态码: {}", code);
            return false;
        }
        session = new Session();
        session.setUuid(Utils.match("window.QRLogin.uuid = \"(.*)\";", response));
        return true;
    }

    /**
     * 生成二维码
     *
     * @return 返回二维码的图片路径
     */
    public String genqrcode() {
        String     url    = conf.get("API_qrcode_img") + session.getUuid();
        final File output = new File("qrcode.jpg");

        RequestBody body = new FormBody.Builder()
                .add("t", "webwx")
                .add("_", System.currentTimeMillis() + "").build();

        System.out.println(url);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response         response = client.newCall(request).execute();
            byte[]           bytes    = response.body().bytes();
            FileOutputStream fos      = new FileOutputStream(output);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            log.error("[*] 生成二维码失败", e);
        }
        return output.getPath();
    }

    /**
     * 等待登录
     *
     * @param tip 1:等待扫描二维码 0:等待微信客户端确认
     * @return
     */
    public boolean waitforlogin(int tip) {
        Utils.sleep(tip);
        String url = conf.get("API_login") + "?tip=%d&uuid=%s&_%s";
        url = String.format(url, tip, session.getUuid(), System.currentTimeMillis());

        String response = doGet(url);

        if (Utils.isBlank(response)) {
            log.warn("扫描二维码验证失败");
            return false;
        }

        String code = Utils.match("window.code=(\\d+);", response);
        if (Utils.isBlank(code)) {
            log.warn("扫描二维码验证失败");
            return false;
        }

        if (code.equals("201")) {
            return true;
        }
        if (code.equals("200")) {
            String pm    = Utils.match("window.redirect_uri=\"(\\S+?)\";", response);
            String r_uri = pm + "&fun=new";
            this.redirectUri = r_uri;
            this.wxHost = r_uri.split("://")[1].split("/")[0];
            this.conf_factory();
            return true;
        }
        if (code.equals("408")) {
            log.warn(Const.LOG_MSG_WAIT_LOGIN_ERR1);
        } else {
            log.warn(Const.LOG_MSG_WAIT_LOGIN_ERR2);
        }
        return false;
    }

    /**
     * 登录微信
     *
     * @return
     */
    public boolean login() {

        Request.Builder requestBuilder = new Request.Builder().url(this.redirectUri);
        Request         request        = requestBuilder.build();

        log.debug("[*] 请求 => {}\n", request);
        try {
            Response     response = client.newCall(request).execute();
            Headers      headers  = response.headers();
            List<String> cookies  = headers.values("Set-Cookie");
            this.cookie = Utils.getCookie(cookies);
            log.info("[*] 设置cookie [{}]", this.cookie);
            String body = response.body().string();
            if (Utils.isBlank(body)) {
                return false;
            }
            session.setSkey(Utils.match("<skey>(\\S+)</skey>", body));
            session.setSid(Utils.match("<wxsid>(\\S+)</wxsid>", body));
            session.setUin(Utils.match("<wxuin>(\\S+)</wxuin>", body));
            session.setPassTicket(Utils.match("<pass_ticket>(\\S+)</pass_ticket>", body));

            this.baseRequest = Utils.createMap("Uin", Long.valueOf(session.getUin()),
                    "Sid", session.getSid(), "Skey", session.getSkey(), "DeviceID", this.deviceId);

            File output = new File("temp.jpg");
            if (output.exists()) {
                output.delete();
            }
            return true;
        } catch (Exception e) {
            log.error("[*] 登录失败", e);
            return false;
        }
    }

    /**
     * 微信初始化
     *
     * @return
     * @throws WechatException
     */
    public boolean webwxinit() {
        if (null == session) {
            return false;
        }

        String url = conf.get("API_webwxinit") + "?pass_ticket=%s&skey=%s&r=%s";
        url = String.format(url, session.getPassTicket(), session.getSkey(), System.currentTimeMillis());

        Map<String, Object> param = Utils.createMap("BaseRequest", this.baseRequest);

        JsonObject response = doPost(url, param).getAsJsonObject();
        if (null == response) {
            return false;
        }

        this.user = new Gson().fromJson(response.get("User"), new TypeToken<Map<String, Object>>() {
        }.getType());
        this.makeSynckey(response);

        JsonObject baseResponse = response.getAsJsonObject("BaseResponse");
        return baseResponse.get("Ret").getAsInt() == 0;
    }

    private void makeSynckey(JsonObject dic) {
        this.synckeyDic = dic.getAsJsonObject("SyncKey");
        StringBuffer synckey = new StringBuffer();
        JsonArray    list    = this.synckeyDic.getAsJsonArray("List");
        for (JsonElement element : list) {
            JsonObject item = element.getAsJsonObject();
            synckey.append("|" + item.get("Key").getAsInt() + "_" + item.get("Val").getAsInt());
        }
        if (synckey.length() > 0) {
            this.synckey = synckey.substring(1);
        }
    }

    /**
     * 开启状态通知
     *
     * @return
     */
    public boolean openStatusNotify() {
        String url = conf.get("API_webwxstatusnotify") + "?lang=%s&pass_ticket=%s";
        url = String.format(url, conf.get("LANG"), session.getPassTicket());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BaseRequest", this.baseRequest);
        params.put("Code", 3);
        params.put("FromUserName", this.user.get("UserName"));
        params.put("ToUserName", this.user.get("UserName"));
        params.put("ClientMsgId", System.currentTimeMillis());

        JsonObject response = doPost(url, params).getAsJsonObject();
        if (null == response) {
            return false;
        }
        JsonObject baseResponse = response.getAsJsonObject("BaseResponse");
        return baseResponse.get("Ret").getAsInt() == 0;
    }

    /**
     * 获取联系人信息
     *
     * @return
     */
    public boolean getContact() {
        String url = conf.get("API_webwxgetcontact") + "?pass_ticket=%s&skey=%s&r=%s";
        url = String.format(url, session.getPassTicket(), session.getSkey(), System.currentTimeMillis());

        Set<String> specialUsers = Const.API_SPECIAL_USER;

        JsonObject response = doPost(url, null).getAsJsonObject();
        if (null == response) {
            return false;
        }

        this.memberCount = response.get("MemberCount").getAsInt();
        this.memberList = response.getAsJsonArray("MemberList");

        JsonArray ContactList = new JsonArray();
        ContactList.addAll(this.memberList);

        this.publicUsersList = new JsonArray();
        this.groupList = new JsonArray();
        this.specialUsersList = new JsonArray();

        for (JsonElement element : memberList) {
            JsonObject contact = element.getAsJsonObject();
            if (contact.get("VerifyFlag").getAsInt() != 0) { //公众号/服务号
                ContactList.remove(contact);
                this.publicUsersList.add(contact);
            } else if (specialUsers.contains(contact.get("UserName").getAsString())) { //特殊账号
                ContactList.remove(contact);
                this.specialUsersList.add(contact);
            } else if (contact.get("UserName").getAsString().contains("@@")) {// 群聊
                ContactList.remove(contact);
                this.groupList.add(contact);
            } else if (contact.get("UserName").getAsString().equals(this.user.get("UserName"))) {// 自己
                ContactList.remove(contact);
            }
        }
        this.contactList = ContactList;
        return true;
    }

    /**
     * 批量获取群成员
     *
     * @param groupIds
     * @return
     */
    public JsonArray batchGetContact(List<String> groupIds) {
        String url = conf.get("API_webwxbatchgetcontact") + "?type=ex&r=%s&pass_ticket=%s";
        url = String.format(url, System.currentTimeMillis(), session.getPassTicket());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BaseRequest", this.baseRequest);
        params.put("Count", groupIds.size());

        List<Map> list = new ArrayList<Map>();
        for (String groupId : groupIds) {
            list.add(Utils.createMap("UserName", groupId, "EncryChatRoomId", ""));
        }
        params.put("List", list);

        JsonElement response = doPost(url, params);
        if (null == response) {
            return null;
        }
        JsonObject dic = response.getAsJsonObject();
        return dic.get("ContactList").getAsJsonArray();
    }

    /**
     * 和微信保持同步
     *
     * @return
     */
    public JsonObject wxSync() {
        String url = conf.get("API_webwxsync") + "?sid=%s&skey=%s&pass_ticket=%s";
        url = String.format(url, session.getSid(), session.getSkey(), session.getPassTicket());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BaseRequest", this.baseRequest);
        params.put("SyncKey", this.synckeyDic);
        params.put("rr", System.currentTimeMillis());

        JsonElement response = doPost(url, params);
        if (null == response) {
            return null;
        }

        JsonObject dic = response.getAsJsonObject();
        if (null != dic) {
            JsonObject baseResponse = dic.getAsJsonObject("BaseResponse");
            if (null != baseResponse && baseResponse.get("Ret").getAsInt() == 0) {
                this.makeSynckey(dic);
            }
        }
        return dic;
    }

    /**
     * 拉取群成员
     *
     * @return
     */
    public void fetchGroupContacts() {
        log.debug("fetchGroupContacts");

        List<String> groupIds = new ArrayList<String>();

        Map<String, JsonObject> g_dict = new HashMap<String, JsonObject>();

        for (JsonElement element : groupList) {
            JsonObject group = element.getAsJsonObject();
            groupIds.add(group.get("UserName").getAsString());
            g_dict.put(group.get("UserName").getAsString(), group);
        }

        JsonArray groupMembers = batchGetContact(groupIds);
        for (JsonElement element : groupMembers) {
            JsonObject member_list = element.getAsJsonObject();
            String     g_id        = member_list.get("UserName").getAsString();
            JsonObject group       = g_dict.get(g_id);
            group.addProperty("MemberCount", member_list.get("MemberCount").getAsInt());
            group.addProperty("OwnerUin", member_list.get("OwnerUin").getAsInt());
            this.groupMemeberList.put(g_id, member_list.get("MemberList").getAsJsonArray());
        }
    }

    // TODO
    public boolean snapshot() {
        return false;
    }

    /**
     * 微信同步检查
     *
     * @return
     */
    public int[] synccheck() {
        String              url    = conf.get("API_synccheck");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("r", System.currentTimeMillis() + Utils.getRandomNumber(5));
        params.put("sid", session.getSid());
        params.put("uin", session.getUin());
        params.put("skey", session.getSkey());
        params.put("deviceid", this.deviceId);
        params.put("synckey", this.synckey);
        params.put("_", System.currentTimeMillis());

        String response = doGet(url, this.cookie, params);

        int[] arr = new int[]{-1, -1};
        if (Utils.isBlank(response)) {
            return arr;
        }
        String retcode  = Utils.match("retcode:\"(\\d+)\",", response);
        String selector = Utils.match("selector:\"(\\d+)\"}", response);
        if (null != retcode && null != selector) {
            arr[0] = Integer.parseInt(retcode);
            arr[1] = Integer.parseInt(selector);
        }
        return arr;
    }

    /**
     * 根据用户id和群id查询用户信息
     *
     * @param userId
     * @param groupId
     * @return
     */
    public Map<String, String> getGroupUserById(String userId, String groupId) {
        String              unknownPeople = Const.LOG_MSG_UNKNOWN_NAME + userId;
        Map<String, String> user          = new HashMap<String, String>();
        // 微信动态ID
        user.put("UserName", userId);
        // 微信昵称
        user.put("NickName", "");
        // 群聊显示名称
        user.put("DisplayName", "");
        // Log显示用的
        user.put("ShowName", unknownPeople);
        // 群用户id
        user.put("AttrStatus", unknownPeople);

        // 群友
        if (!groupMemeberList.containsKey(groupId)) {
            return user;
        }

        JsonArray memebers = groupMemeberList.get(groupId);
        for (JsonElement element : memebers) {
            JsonObject member = element.getAsJsonObject();
            if (member.get("UserName").getAsString().equals(userId)) {
                String n = Utils.emptyOr(member.get("NickName").getAsString(), "");
                String d = Utils.emptyOr(member.get("DisplayName").getAsString(), "");
                user.put("NickName", n);
                user.put("DisplayName", d);
                user.put("AttrStatus", Utils.emptyOr(member.get("AttrStatus").getAsString(), ""));
                user.put("ShowName", Utils.emptyOr(d, n));
                break;
            }
        }
        return user;
    }

    /**
     * 根据群id查询群信息
     *
     * @param groupId
     * @return
     */
    public Map<String, String> getGroupById(String groupId) {
        String              unknownGroup = Const.LOG_MSG_UNKNOWN_GROUP_NAME + groupId;
        Map<String, String> group        = new HashMap<String, String>();
        group.put("UserName", groupId);
        group.put("NickName", "");
        group.put("DisplayName", "");
        group.put("ShowName", "");
        group.put("OwnerUin", "");
        group.put("MemberCount", "");

        for (JsonElement element : groupList) {
            JsonObject member = element.getAsJsonObject();
            if (member.get("UserName").getAsString().equals(groupId)) {
                group.put("NickName", Utils.emptyOr(member.get("NickName").getAsString(), ""));
                group.put("DisplayName", Utils.emptyOr(member.get("DisplayName").getAsString(), ""));
                group.put("ShowName", Utils.emptyOr(member.get("NickName").getAsString(), ""));
                group.put("OwnerUin", Utils.emptyOr(member.get("OwnerUin").getAsString(), ""));
                group.put("MemberCount", Utils.emptyOr(member.get("MemberCount").getAsString(), "0"));
                break;
            }
        }

        return group;
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    public Map<String, String> getUserById(String userId) {
        String              unknownPeople = Const.LOG_MSG_UNKNOWN_NAME + userId;
        Map<String, String> user          = new HashMap<String, String>();
        user.put("UserName", userId);
        user.put("RemarkName", "");
        user.put("NickName", "");
        user.put("ShowName", unknownPeople);

        if (userId.equals(this.user.get("UserName"))) {
            user.put("RemarkName", this.user.get("RemarkName").toString());
            user.put("NickName", this.user.get("NickName").toString());
            user.put("ShowName", this.user.get("NickName").toString());
        } else {
            // 联系人
            for (JsonElement element : memberList) {
                JsonObject item = element.getAsJsonObject();
                if (item.get("UserName").getAsString().equals(userId)) {
                    user.put("RemarkName", Utils.emptyOr(item.get("RemarkName").getAsString(), ""));
                    user.put("NickName", Utils.emptyOr(item.get("NickName").getAsString(), ""));
                    if (Utils.isNotBlank(user.get("RemarkName"))) {
                        user.put("ShowName", user.get("RemarkName"));
                    } else {
                        user.put("ShowName", user.get("NickName"));
                    }
                    break;
                }
            }
            // 特殊账号
            for (JsonElement element : specialUsersList) {
                JsonObject item = element.getAsJsonObject();
                if (item.get("UserName").getAsString().equals(userId)) {
                    user.put("RemarkName", userId);
                    user.put("NickName", userId);
                    user.put("ShowName", userId);
                    break;
                }
            }
        }
        return user;
    }

    /**
     * 发送微信消息
     *
     * @param msg
     * @param to
     * @return
     */
    public JsonObject wxSendMessage(String msg, String to) {

        String url = conf.get("API_webwxsendmsg") + "?pass_ticket=%s";
        url = String.format(url, session.getPassTicket());

        String              clientMsgId = System.currentTimeMillis() + Utils.getRandomNumber(5);
        Map<String, Object> params      = new HashMap<String, Object>();
        params.put("BaseRequest", this.baseRequest);
        Map<String, Object> Msg = new HashMap<String, Object>();
        Msg.put("Type", 1);
        Msg.put("Content", Utils.unicodeToUtf8(msg));
        Msg.put("FromUserName", this.user.get("UserName"));
        Msg.put("ToUserName", to);
        Msg.put("LocalID", clientMsgId);
        Msg.put("ClientMsgId", clientMsgId);
        params.put("Msg", Msg);

        JsonElement response = doPost(url, params);
        if (null == response) {
            return null;
        }
        return response.getAsJsonObject();
    }

    /**
     * 发送文本消息
     *
     * @param msg
     * @param uid
     */
    public void sendText(String msg, String uid) {
        this.wxSendMessage(msg, uid);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(connTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .build();

    private String doGet(String url, Map<String, Object>... params) {
        return doGet(url, null, params);
    }

    private String doGet(String url, String cookie, Map<String, Object>... params) {
        if (null != params && params.length > 0) {
            Map<String, Object> param = params[0];
            Set<String>         keys  = param.keySet();
            StringBuilder       sbuf  = new StringBuilder(url);
            if (url.contains("=")) {
                sbuf.append("&");
            } else {
                sbuf.append("?");
            }
            for (String key : keys) {
                sbuf.append(key).append('=').append(param.get(key)).append('&');
            }
            url = sbuf.substring(0, sbuf.length() - 1);
        }
        try {
            Request.Builder requestBuilder = new Request.Builder().url(url);

            if (null != cookie) {
                requestBuilder.addHeader("Cookie", this.cookie);
            }

            Request request = requestBuilder.build();

            log.debug("[*] 请求 => {}\n", request);

            Response response = client.newCall(request).execute();
            String   body     = response.body().string();

            log.debug("[*] 响应 => {}", body);
            return body;
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    private JsonElement doPost(String url, Object object) {
        String      bodyJson    = null;
        RequestBody requestBody = RequestBody.create(JSON, "");
        if (null != object) {
            bodyJson = Utils.toJson(object);
            requestBody = RequestBody.create(JSON, bodyJson);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        if (null != cookie) {
            requestBuilder.addHeader("Cookie", cookie);
        }

        Request request = requestBuilder.build();

        log.debug("[*] 请求 => {}\n", request);
        try {
            Response response = client.newCall(request).execute();
            String   body     = response.body().string();
            if (null != body && body.length() <= 300) {
                log.debug("[*] 响应 => {}", body);
            }
            return new JsonParser().parse(body);
        } catch (Exception e) {
            log.error("", e);
            return new JsonParser().parse("{}");
        }
    }

}
