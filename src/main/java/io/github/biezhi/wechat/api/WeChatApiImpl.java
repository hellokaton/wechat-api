package io.github.biezhi.wechat.api;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.constant.Constant;
import io.github.biezhi.wechat.api.constant.StateCode;
import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.*;
import io.github.biezhi.wechat.api.request.BaseRequest;
import io.github.biezhi.wechat.api.request.FileRequest;
import io.github.biezhi.wechat.api.request.JsonRequest;
import io.github.biezhi.wechat.api.request.StringRequest;
import io.github.biezhi.wechat.api.response.*;
import io.github.biezhi.wechat.exception.WeChatException;
import io.github.biezhi.wechat.utils.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.biezhi.wechat.api.constant.Constant.*;

/**
 * 微信API实现
 *
 * @author biezhi
 * @date 2018/1/21
 */
@Slf4j
public class WeChatApiImpl implements WeChatApi {

    private static final Pattern UUID_PATTERN          = Pattern.compile("window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";");
    private static final Pattern CHECK_LOGIN_PATTERN   = Pattern.compile("window.code=(\\d+)");
    private static final Pattern PROCESS_LOGIN_PATTERN = Pattern.compile("window.redirect_uri=\"(\\S+)\";");
    private static final Pattern SYNC_CHECK_PATTERN    = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"}");

    private String  uuid;
    private boolean logging;
    private int     memberCount;

    private final WeChatBot bot;
    private final Map<MsgType, List<Invoke>> mapping = new ConcurrentHashMap<MsgType, List<Invoke>>();

    /**
     * 所有账号
     */
    @Getter
    private Map<String, Account> accountMap = new HashMap<String, Account>();
    /**
     * 特殊账号
     */
    @Getter
    private List<Account> specialUsersList;
    /**
     * 公众号、服务号
     */
    @Getter
    private List<Account> publicUsersList;
    /**
     * 好友
     */
    @Getter
    private List<Account> contactList;
    /**
     * 群组
     */
    @Getter
    private List<Account> groupList;

    public WeChatApiImpl(WeChatBot bot) {
        this.bot = bot;
        Method[] methods = bot.getClass().getMethods();
        for (Method method : methods) {
            Bind bind = method.getAnnotation(Bind.class);
            if (null != bind) {
                MsgType[] msgTypes = bind.msgType();
                for (MsgType msgType : msgTypes) {
                    List<Invoke> invokes = mapping.get(msgType);
                    if (null == mapping.get(msgType)) {
                        invokes = new ArrayList<Invoke>();
                    }
                    invokes.add(new Invoke(method, Arrays.asList(bind.accountType())));
                    log.info("绑定函数 [{}] - [{}]", method.getName(), msgType);
                    mapping.put(msgType, invokes);
                }
            }
        }
    }

    private void autoLogin() {
        String file = bot.config().assetsDir() + "/login.json";
        try {
            HotReload hotReload = WeChatUtils.fromJson(new FileReader(file), HotReload.class);
            hotReload.relogin(bot);
        } catch (FileNotFoundException e) {
            this.login(false);
        }
    }

    @Override
    public void login(boolean autoLogin) {
        if (bot.isRunning() || logging) {
            log.warn("微信已经登录");
            return;
        }
        if (autoLogin) {
            this.autoLogin();
        } else {
            this.logging = true;
            while (logging) {
                this.uuid = pushLogin();
                if (null == this.uuid) {
                    while (null == this.getUUID()) {
                        DateUtils.sleep(10);
                    }
                    log.info("开始下载二维码");
                    this.getQrImage(this.uuid, bot.config().showTerminal());
                    log.info("请使用手机扫描屏幕二维码");
                }
                Boolean isLoggedIn = false;
                while (null == isLoggedIn || !isLoggedIn) {
                    String status = this.checkLogin(this.uuid);
                    if (StateCode.SUCCESS.equals(status)) {
                        isLoggedIn = true;
                    } else if ("201".equals(status)) {
                        if (null != isLoggedIn) {
                            log.info("请在手机上确认登录");
                            isLoggedIn = null;
                        }
                    } else if ("408".equals(status)) {
                        break;
                    }
                    DateUtils.sleep(300);
                }
                if (null != isLoggedIn && isLoggedIn) {
                    break;
                }
                if (logging) {
                    log.info("登录超时，重新加载二维码");
                }
            }
        }

        this.webInit();
        this.statusNotify();
        this.loadContact(0);

        log.info("应有 {} 个联系人，读取到联系人 {} 个", this.memberCount, this.accountMap.size());
        System.out.println();

        log.info("共有 {} 个群 | {} 个直接联系人 | {} 个特殊账号 ｜ {} 公众号或服务号",
                this.groupList.size(), this.contactList.size(),
                this.specialUsersList.size(), this.publicUsersList.size());

        this.loadGroupList();

        log.info("[{}] 登录成功.", bot.session().getNickName());
        this.startRevice();
        this.logging = false;
    }

    /**
     * 获取UUID
     *
     * @return
     */
    private String getUUID() {
        log.info("获取二维码UUID");
        // 登录
        ApiResponse response = bot.execute(new StringRequest("https://login.weixin.qq.com/jslogin")
                .add("appid", "wx782c26e4c19acffb").add("fun", "new"));

        Matcher matcher = UUID_PATTERN.matcher(response.getRawBody());
        if (matcher.find() && StateCode.SUCCESS.equals(matcher.group(1))) {
            this.uuid = matcher.group(2);
        }
        return this.uuid;
    }

    /**
     * 读取二维码图片
     *
     * @param uuid
     * @param terminalShow 是否在终端显示输出
     */
    private void getQrImage(String uuid, boolean terminalShow) {
        String uid    = null != uuid ? uuid : this.uuid;
        String imgDir = bot.config().assetsDir();

        FileResponse fileResponse = bot.download(
                new FileRequest(String.format("%s/qrcode/%s", Constant.BASE_URL, uid)));

        InputStream inputStream = fileResponse.getInputStream();
        File        qrCode      = WeChatUtils.saveFile(inputStream, imgDir, "qrcode.png");
        DateUtils.sleep(500);
        QRCodeUtils.showQrCode(qrCode, terminalShow);
    }

    /**
     * 检查是否登录
     *
     * @param uuid
     * @return
     */
    private String checkLogin(String uuid) {
        String uid  = null != uuid ? uuid : this.uuid;
        String url  = String.format("%s/cgi-bin/mmwebwx-bin/login", Constant.BASE_URL);
        Long   time = System.currentTimeMillis();

        ApiResponse response = bot.execute(new StringRequest(url)
                .add("loginicon", true).add("uuid", uid)
                .add("tip", "1").add("_", time)
                .add("r", (int) (-time / 1000) / 1579)
                .timeout(30));

        Matcher matcher = CHECK_LOGIN_PATTERN.matcher(response.getRawBody());
        if (matcher.find()) {
            if (StateCode.SUCCESS.equals(matcher.group(1))) {
                if (!this.processLoginSession(response.getRawBody())) {
                    return StateCode.FAIL;
                }
                return StateCode.SUCCESS;
            }
            return matcher.group(1);
        }
        return StateCode.FAIL;
    }

    /**
     * 处理登录session
     *
     * @param loginContent
     * @return
     */
    private boolean processLoginSession(String loginContent) {
        LoginSession loginSession = bot.session();
        Matcher      matcher      = PROCESS_LOGIN_PATTERN.matcher(loginContent);
        if (matcher.find()) {
            loginSession.setUrl(matcher.group(1));
        }
        ApiResponse response = bot.execute(new StringRequest(loginSession.getUrl()).noRedirect());
        loginSession.setUrl(loginSession.getUrl().substring(0, loginSession.getUrl().lastIndexOf("/")));

        String body = response.getRawBody();

        List<String> fileUrl = new ArrayList<String>();
        List<String> syncUrl = new ArrayList<String>();
        for (int i = 0; i < FILE_URL.size(); i++) {
            fileUrl.add(String.format("https://%s/cgi-bin/mmwebwx-bin", FILE_URL.get(i)));
            syncUrl.add(String.format("https://%s/cgi-bin/mmwebwx-bin", WEBPUSH_URL.get(i)));
        }
        boolean flag = false;
        for (int i = 0; i < FILE_URL.size(); i++) {
            String indexUrl = INDEX_URL.get(i);
            if (loginSession.getUrl().contains(indexUrl)) {
                loginSession.setFileUrl(fileUrl.get(i));
                loginSession.setSyncUrl(syncUrl.get(i));
                flag = true;
                break;
            }
        }
        if (!flag) {
            loginSession.setFileUrl(loginSession.getUrl());
            loginSession.setSyncUrl(loginSession.getUrl());
        }

        loginSession.setDeviceId("e" + String.valueOf(System.currentTimeMillis()));

        BaseRequest baseRequest = new BaseRequest();
        loginSession.setBaseRequest(baseRequest);

        loginSession.setSKey(WeChatUtils.match("<skey>(\\S+)</skey>", body));
        loginSession.setWxSid(WeChatUtils.match("<wxsid>(\\S+)</wxsid>", body));
        loginSession.setWxUin(WeChatUtils.match("<wxuin>(\\S+)</wxuin>", body));
        loginSession.setPassTicket(WeChatUtils.match("<pass_ticket>(\\S+)</pass_ticket>", body));

        baseRequest.setSkey(loginSession.getSKey());
        baseRequest.setSid(loginSession.getWxSid());
        baseRequest.setUin(loginSession.getWxUin());
        baseRequest.setDeviceID(loginSession.getDeviceId());

        return true;
    }

    /**
     * 推送登录
     *
     * @return
     */
    private String pushLogin() {
        String uin = bot.client().cookie("wxUin");
        if (StringUtils.isEmpty(uin)) {
            return null;
        }
        String url = String.format("%s/cgi-bin/mmwebwx-bin/webwxpushloginurl?uin=%s",
                Constant.BASE_URL, uin);

        JsonResponse jsonResponse = bot.execute(new JsonRequest(url));
        return jsonResponse.getString("uuid");
    }

    /**
     * 开启状态通知
     *
     * @return
     */
    private void statusNotify() {
        log.info("开启状态通知");

        String url = String.format("%s/webwxstatusnotify?lang=zh_CN&pass_ticket=%s",
                bot.session().getUrl(), bot.session().getPassTicket());

        bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Code", 3)
                .add("FromUserName", bot.session().getUserName())
                .add("ToUserName", bot.session().getUserName())
                .add("ClientMsgId", System.currentTimeMillis() / 1000));
    }

    /**
     * web 初始化
     */
    private WebInitResponse webInit() {
        log.info("微信初始化...");
        int r = (int) (-System.currentTimeMillis() / 1000) / 1579;
        String url = String.format("%s/webwxinit?r=%d&pass_ticket=%s",
                bot.session().getUrl(), r, bot.session().getPassTicket());

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest()));

        WebInitResponse webInitResponse = response.parse(WebInitResponse.class);

        List<Account> contactList = webInitResponse.getContactList();
        this.syncRecentContact(contactList);

        Account account = webInitResponse.getAccount();
        SyncKey syncKey = webInitResponse.getSyncKey();

        bot.session().setInviteStartCount(webInitResponse.getInviteStartCount());
        bot.session().setAccount(account);
        bot.session().setUserName(account.getUserName());
        bot.session().setNickName(account.getNickName());
        bot.session().setSyncKey(syncKey);

        return webInitResponse;
    }

    private void startRevice() {
        bot.setRunning(true);
        Thread thread = new Thread(new ChatLoop(bot));
        thread.setName("wechat-listener");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 心跳检查
     *
     * @return
     */
    @Override
    public SyncCheckRet syncCheck() {
        String url = String.format("%s/synccheck", bot.session().getSyncOrUrl());
        try {
            ApiResponse response = bot.execute(new StringRequest(url)
                    .add("r", System.currentTimeMillis())
                    .add("skey", bot.session().getSKey())
                    .add("sid", bot.session().getWxSid())
                    .add("uin", bot.session().getWxUin())
                    .add("deviceid", bot.session().getDeviceId())
                    .add("synckey", bot.session().getSynckeyStr())
                    .add("_", System.currentTimeMillis())
            );

            Matcher matcher = SYNC_CHECK_PATTERN.matcher(response.getRawBody());
            if (matcher.find()) {
                if (!"0".equals(matcher.group(1))) {
                    log.debug("Unexpected sync check result: {}", response.getRawBody());
                    return new SyncCheckRet(1100, 0);
                }
                return new SyncCheckRet(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)));
            }
            return null;
        } catch (Exception e) {
            return new SyncCheckRet(0, 0);
        }
    }

    /**
     * 获取消息
     */
    @Override
    public WebSyncResponse webSync() {
        String url = String.format("%s/webwxsync?sid=%s&sKey=%s&passTicket=%s",
                bot.session().getUrl(), bot.session().getWxSid(),
                bot.session().getSKey(), bot.session().getPassTicket());

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("SyncKey", bot.session().getSyncKey())
                .add("rr", ~(System.currentTimeMillis() / 1000)));

        WebSyncResponse webSyncResponse = response.parse(WebSyncResponse.class);
        if (!webSyncResponse.success()) {
            log.warn("获取消息失败");
            return webSyncResponse;
        }
        bot.session().setSyncKey(webSyncResponse.getSyncKey());
        return webSyncResponse;
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public void logout() {
        if (bot.isRunning()) {
            String url = String.format("%s/webwxlogout", bot.session().getUrl());
            bot.execute(new StringRequest(url)
                    .add("redirect", 1)
                    .add("type", 1)
                    .add("sKey", bot.session().getSKey()));
            bot.setRunning(false);
        }
        this.logging = false;
        bot.client().cookies().clear();
    }

    /**
     * 加载联系人信息
     *
     * @return
     */
    @Override
    public void loadContact(int seq) {
        log.info("开始获取联系人信息");
        while (true) {
            String url = String.format("%s/webwxgetcontact?r=%s&seq=%s&skey=%s",
                    bot.session().getUrl(), System.currentTimeMillis(),
                    seq, bot.session().getSKey());

            JsonResponse response = bot.execute(new JsonRequest(url).jsonBody());

            JsonObject jsonObject = response.toJsonObject();
            seq = jsonObject.get("Seq").getAsInt();

            this.memberCount += jsonObject.get("MemberCount").getAsInt();
            List<Account> memberList = WeChatUtils.fromJson(WeChatUtils.toJson(jsonObject.getAsJsonArray("MemberList")), new TypeToken<List<Account>>() {});

            for (Account account : memberList) {
                if (null == account.getUserName()) {
                    accountMap.put(account.getUserName(), account);
                }
            }
            // 查看seq是否为0，0表示好友列表已全部获取完毕，若大于0，则表示好友列表未获取完毕，当前的字节数（断点续传）
            if (seq == 0) {
                break;
            }
        }

        this.contactList = new ArrayList<Account>(this.getAccountByType(AccountType.TYPE_FRIEND));
        this.publicUsersList = new ArrayList<Account>(this.getAccountByType(AccountType.TYPE_MP));
        this.specialUsersList = new ArrayList<Account>(this.getAccountByType(AccountType.TYPE_SPECIAL));
        this.groupList = new ArrayList<Account>(this.getAccountByType(AccountType.TYPE_GROUP));
    }

    /**
     * 加载群信息
     */
    public void loadGroupList() {
        log.info("加载群聊信息");

        // 群账号
        List<Map<String, String>> list = new ArrayList<Map<String, String>>(groupList.size());

        for (Account account : groupList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("UserName", account.getUserName());
            map.put("EncryChatRoomId", "");
            list.add(map);
        }

        String url = String.format("%s/webwxbatchgetcontact?type=ex&r=%s&pass_ticket=%s",
                bot.session().getUrl(), System.currentTimeMillis() / 1000, bot.session().getPassTicket());

        // 加载群信息
        JsonResponse jsonResponse = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Count", groupList.size())
                .add("List", list)
        );

        List<Account> groups = WeChatUtils.fromJson(WeChatUtils.toJson(jsonResponse.toJsonObject().getAsJsonArray("ContactList")), new TypeToken<List<Account>>() {});

    }

    /**
     * 根据UserName查询Account
     *
     * @param id
     * @return
     */
    @Override
    public Account getAccountById(String id) {
        return accountMap.get(id);
    }

    private String getUserRemarkName(String id) {
        String name = id.contains("@@") ? "未知群" : "陌生人";
        if (id.equals(this.bot.session().getUserName())) {
            return this.bot.session().getNickName();
        }
        Account account = accountMap.get(id);
        if (null == account) {
            return name;
        }
        String nickName = StringUtils.isNotEmpty(account.getRemarkName()) ? account.getRemarkName() : account.getNickName();
        return StringUtils.isNotEmpty(nickName) ? nickName : name;
    }

    private List<Account> getNameById(String id) {
        String url = String.format("%s/webwxbatchgetcontact?type=ex&r=%s&pass_ticket=%s",
                bot.session().getUrl(), System.currentTimeMillis() / 1000, bot.session().getPassTicket());

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String>       map  = new HashMap<String, String>();
        map.put("UserName", id);
        map.put("EncryChatRoomId", id);
        list.add(map);

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Count", bot.session().getBaseRequest())
                .add("List", list));

        return WeChatUtils.fromJson(WeChatUtils.toJson(response.toJsonObject().getAsJsonObject("")), new TypeToken<List<Account>>() {});
    }

    /**
     * 根据账号类型筛选
     *
     * @param accountType
     * @return
     */
    public Set<Account> getAccountByType(AccountType accountType) {
        Set<Account> accountSet = new HashSet<Account>();
        for (Account account : accountMap.values()) {
            if (account.getAccountType().equals(accountType)) {
                accountSet.add(account);
            }
        }
        return accountSet;
    }

    /**
     * 同步最近联系人
     * <p>
     * 避免新建群聊无法同步
     *
     * @param contactList
     */
    public void syncRecentContact(List<Account> contactList) {
        if (null != contactList && contactList.size() > 0) {
            for (Account account : contactList) {
                accountMap.put(account.getUserName(), account);
            }
        }
    }

    /**
     * 处理新消息
     *
     * @param webSyncResponse
     */
    @Override
    public void handleMsg(WebSyncResponse webSyncResponse) {
        List<Message> addMessageList = webSyncResponse.getAddMessageList();
        if (null != addMessageList && addMessageList.size() > 0) {
            log.info("你有新的消息");
            for (Message message : addMessageList) {
                this.processMsg(message);
            }
        }
    }

    private void processMsg(Message message) {
        Integer type = message.getType();
        String  name = this.getUserRemarkName(message.getFromUserName());
        String content = message.getContent().replace("&lt;", "<")
                .replace("&gt;", ">");

        if (message.isGroup()) {
            if (content.contains(":<br/>")) {
                content = content.substring(content.indexOf(":<br/>") + 6);
            }
        }

        String  msgId       = message.getId();
        Account fromAccount = this.getAccountById(message.getFromUserName());
        if (null == fromAccount) {
            log.warn("未知消息类型: {}", WeChatUtils.toJson(message));
            return;
        }

        log.debug("收到消息JSON: {}", WeChatUtils.toJson(message));

        WeChatMessage.WeChatMessageBuilder weChatMessageBuilder = WeChatMessage.builder()
                .raw(message)
                .fromNickName(fromAccount.getNickName())
                .fromRemarkName(fromAccount.getRemarkName())
                .fromUserName(message.getFromUserName())
                .msgType(message.msgType())
                .text(content);

        switch (type) {
            case 1:
                if (bot.autoReply()) {
                    this.sendText("自动回复: " + content, message.getFromUserName());
                } else {
                    this.callBack(mapping.get(MsgType.ALL), weChatMessageBuilder.build());
                    this.callBack(mapping.get(MsgType.TEXT), weChatMessageBuilder.build());
                }
                break;
            // 聊天图片
            case 3:
                String imgPath = this.downloadMsgImg(msgId);
                this.callBack(mapping.get(MsgType.ALL), weChatMessageBuilder.imagePath(imgPath).build());
                this.callBack(mapping.get(MsgType.IMAGE), weChatMessageBuilder.imagePath(imgPath).build());
                break;
            // 语音
            case 34:
                String voicePath = this.downloadVoice(msgId);
                this.callBack(mapping.get(MsgType.ALL), weChatMessageBuilder.voicePath(voicePath).build());
                this.callBack(mapping.get(MsgType.VOICE), weChatMessageBuilder.voicePath(voicePath).build());
                break;
            // 名片
            case 42:
                log.info("{} 发送了一张名片: ", name);
                log.info("=========================");
                log.info("= 昵称: {}", message.getRecommend().getNickName());
                log.info("= 微信号: {}", message.getRecommend().getAlias());
                log.info("= 地区: {}-{}", message.getRecommend().getProvince(), message.getRecommend().getCity());
                log.info("= 性别: {}", message.getRecommend().getSex());
                log.info("=========================");
                this.callBack(mapping.get(MsgType.ALL), weChatMessageBuilder.build());
                this.callBack(mapping.get(MsgType.PERSON_CARD), weChatMessageBuilder.build());
                break;
            // 视频
            case 43:
                String videoPath = this.downloadVideo(msgId);
                this.callBack(mapping.get(MsgType.ALL), weChatMessageBuilder.videoPath(videoPath).build());
                this.callBack(mapping.get(MsgType.VIDEO), weChatMessageBuilder.videoPath(videoPath).build());
                break;
            // 动画表情
            case 47:
                String imgUrl = this.searchContent("cdnurl", content);
                log.info("{} 发了一个动画表情，点击下面链接查看\r\n{}", name, imgUrl);

                WeChatMessage weChatMessage = weChatMessageBuilder.imagePath(imgUrl).build();
                weChatMessage.setText("发了一个动画表情，点击下面链接查看\r\n" + imgUrl);

                this.callBack(mapping.get(MsgType.ALL), weChatMessage);
                this.callBack(mapping.get(MsgType.IMAGE), weChatMessage);
                break;
            // 分享
            case 49:
                break;
            // 联系人初始化
            case 51:
                break;
            // 视频
            case 62:
                videoPath = this.downloadVideo(msgId);
                this.callBack(mapping.get(MsgType.ALL), weChatMessageBuilder.videoPath(videoPath).build());
                this.callBack(mapping.get(MsgType.VIDEO), weChatMessageBuilder.videoPath(videoPath).build());
                break;
            // 邀请好友进群
            case 10000:
                log.info("{}", content);
                break;
            // 撤回消息
            case 10002:
                log.info("{} 撤回了一条消息: {}", name, content);
                this.callBack(mapping.get(MsgType.ALL), weChatMessageBuilder.build());
                this.callBack(mapping.get(MsgType.REVOKE), weChatMessageBuilder.build());
                break;
            default:
                log.info("该消息类型为: {}, 可能是表情，图片, 链接或红包: {}", type, WeChatUtils.toJson(message));
                break;
        }
    }

    private String searchContent(String key, String content) {
        String r = WeChatUtils.match(key + "\\s?=\\s?\"([^\"<]+)\"", content);
        if (StringUtils.isNotEmpty(r)) {
            return r;
        }
        r = WeChatUtils.match(String.format("<%s>([^<]+)</%s>", key, key), content);
        if (StringUtils.isNotEmpty(r)) {
            return r;
        }
        r = WeChatUtils.match(String.format("<%s><\\!\\[CDATA\\[(.*?)\\]\\]></%s>", key, key), content);
        if (StringUtils.isNotEmpty(r)) {
            return r;
        }
        return "";
    }

    /**
     * 回调微信消息给客户端、存储器
     *
     * @param invokes
     * @param message
     */
    private void callBack(List<Invoke> invokes, WeChatMessage message) {
        if (null != invokes && invokes.size() > 0 && null != message) {
            if (null != bot.storageMessage()) {
                bot.storageMessage().save(message);
            }
            for (Invoke invoke : invokes) {
                invoke.call(bot, message);
            }
        }
    }

    /**
     * 下载图片到本地
     *
     * @param msgId
     */
    private String downloadMsgImg(String msgId) {
        String url = String.format("%s/webwxgetmsgimg?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id   = msgId + ".jpg";
        File   file = WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/images", id);
        return file.getPath();
    }

    /**
     * 下载表情到本地
     *
     * @param msgId
     */
    private String downloadIconImg(String msgId) {
        String url = String.format("%s/webwxgeticon?username=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".jpg";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/icons", id).getPath();
    }

    /**
     * 下载头像到本地
     *
     * @param userName
     */
    private String downloadHeadImg(String userName) {
        String       url         = String.format("%s/webwxgetheadimg?username=%s&skey=%s", bot.session().getUrl(), userName, bot.session().getSKey());
        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = userName + ".jpg";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/head", id).getPath();
    }

    /**
     * 下载视频到本地
     *
     * @param msgId
     */
    private String downloadVideo(String msgId) {
        String url = String.format("%s/webwxgetvideo?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".mp4";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/video", id).getPath();
    }

    /**
     * 下载音频到本地
     *
     * @param msgId
     */
    private String downloadVoice(String msgId) {
        String url = String.format("%s/webwxgetvoice?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".mp3";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/video", id).getPath();
    }

    /**
     * 上传附件
     *
     * @param toUser
     * @param filePath
     * @return
     */
    public MediaResponse uploadMedia(String toUser, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new WeChatException("文件[" + filePath + "]不存在");
        }

        long   size     = file.length();
        String mimeType = "image/jpeg";
        String url      = String.format("%s/webwxuploadmedia?f=json", bot.session().getFileUrl());
        String mediaId  = System.currentTimeMillis() / 1000 + StringUtils.random(6);

        Map<String, Object> uploadMediaRequest = new HashMap<String, Object>();
        uploadMediaRequest.put("UploadType", 2);
        uploadMediaRequest.put("BaseRequest", bot.session().getBaseRequest());
        uploadMediaRequest.put("ClientMediaId", mediaId);
        uploadMediaRequest.put("TotalLen", size);
        uploadMediaRequest.put("StartPos", 0);
        uploadMediaRequest.put("DataLen", size);
        uploadMediaRequest.put("MediaType", 4);
        uploadMediaRequest.put("FromUserName", bot.session().getUserName());
        uploadMediaRequest.put("ToUserName", toUser);
        uploadMediaRequest.put("FileMd5", MD5Checksum.getMD5Checksum(file.getPath()));

        String dataTicket = bot.client().cookie("webwx_data_ticket");
        if (StringUtils.isEmpty(dataTicket)) {
            throw new WeChatException("缺少了附件Cookie");
        }

        ApiResponse response = bot.execute(new StringRequest(url).post().multipart()
                .fileName(file.getName())
                .add("id", "WU_FILE_0")
                .add("name", filePath)
                .add("type", mimeType)
                .add("lastModifieDate", new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(new Date()))
                .add("size", String.valueOf(size))
                .add("mediatype", mimeType)
                .add("uploadmediarequest", WeChatUtils.toJson(uploadMediaRequest))
                .add("webwx_data_ticket", dataTicket)
                .add("pass_ticket", bot.session().getPassTicket())
                .add("filename", RequestBody.create(MediaType.parse("image/jpeg"), file)));

        MediaResponse mediaResponse = response.parse(MediaResponse.class);
        if (!mediaResponse.success()) {
            log.warn("上传附件失败: {}", mediaResponse.getMsg());
        }
        return mediaResponse;
    }

    @Override
    public void sendFile(String toUserName, String filePath) {
        String mediaId = this.uploadMedia(toUserName, filePath).getMediaId();
        if (StringUtils.isEmpty(mediaId)) {
            log.warn("Media为空");
            return;
        }

        String url = String.format("%s/webwxsendmsgimg?fun=async&f=json&pass_ticket=%s",
                bot.session().getUrl(), bot.session().getPassTicket());

        String msgId = System.currentTimeMillis() / 1000 + StringUtils.random(6);

        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("Type", 3);
        msg.put("MediaId", mediaId);
        msg.put("FromUserName", bot.session().getUserName());
        msg.put("ToUserName", toUserName);
        msg.put("LocalID", msgId);
        msg.put("ClientMsgId", msgId);

        bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Msg", msg)
        );
    }

    /**
     * 发送文本消息
     *
     * @param msg
     * @param toUserName
     */
    @Override
    public void sendText(String toUserName, String msg) {
        String url   = String.format("%s/webwxsendmsg?pass_ticket=%s", bot.session().getUrl(), bot.session().getPassTicket());
        String msgId = System.currentTimeMillis() / 1000 + StringUtils.random(6);

        bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Msg", new SendMessage(1, msg, bot.session().getUserName(), toUserName, msgId, msgId))
        );

    }

}