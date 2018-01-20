package io.github.biezhi.wechat.handler;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.constant.Constant;
import io.github.biezhi.wechat.api.constant.StateCode;
import io.github.biezhi.wechat.api.model.LoginSession;
import io.github.biezhi.wechat.api.model.SyncCheckRet;
import io.github.biezhi.wechat.api.model.SyncKey;
import io.github.biezhi.wechat.api.model.Account;
import io.github.biezhi.wechat.api.request.BaseRequest;
import io.github.biezhi.wechat.api.request.FileRequest;
import io.github.biezhi.wechat.api.request.JsonRequest;
import io.github.biezhi.wechat.api.request.StringRequest;
import io.github.biezhi.wechat.api.response.*;
import io.github.biezhi.wechat.utils.DateUtils;
import io.github.biezhi.wechat.utils.QRCodeUtils;
import io.github.biezhi.wechat.utils.StringUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.biezhi.wechat.api.constant.Constant.*;

/**
 * 登录处理
 * <p>
 * 1. 扫码
 * 2. 微信初始化
 * 3. 开启状态通知
 * 4. 获取联系人
 * 5. 开启消息监听
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Slf4j
public class LoginHandler {

    private static final Pattern UUID_PATTERN          = Pattern.compile("window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";");
    private static final Pattern CHECK_LOGIN_PATTERN   = Pattern.compile("window.code=(\\d+)");
    private static final Pattern PROCESS_LOGIN_PATTERN = Pattern.compile("window.redirect_uri=\"(\\S+)\";");
    private static final Pattern SYNC_CHECK_PATTERN    = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"}");

    private WeChatBot bot;
    private String    uuid;
    private boolean   logging;

    public LoginHandler(WeChatBot weChatBot) {
        this.bot = weChatBot;
    }

    /**
     * 扫码登录
     */
    public void login() {
        if (bot.isRunning() || logging) {
            log.warn("微信已经登录");
            return;
        }
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
        this.webInit();
        this.statusNotify();
        bot.getContactHandler().loadContact(0);

        log.info("应有 {} 个联系人，读取到联系人 {} 个", bot.getContactHandler().getMemberCount(), bot.getContactHandler().getAccountMap().size());
        System.out.println();
        log.info("共有 {} 个群 | {} 个直接联系人 | {} 个特殊账号 ｜ {} 公众号或服务号",
                bot.getContactHandler().getGroupList().size(), bot.getContactHandler().getContactList().size(),
                bot.getContactHandler().getSpecialUsersList().size(), bot.getContactHandler().getPublicUsersList().size());

        bot.getContactHandler().loadGroupList();

        log.info("[{}] 登录成功.", bot.session().getNickName());
        this.startRevice();
        logging = false;
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public String getUUID() {
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
    public void getQrImage(String uuid, boolean terminalShow) {
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
    public String checkLogin(String uuid) {
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
    public boolean processLoginSession(String loginContent) {
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
    public String pushLogin() {
        String uin = bot.api().cookie("wxUin");
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
    public void statusNotify() {
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
    public WebInitResponse webInit() {
        log.info("微信初始化...");
        int r = (int) (-System.currentTimeMillis() / 1000) / 1579;
        String url = String.format("%s/webwxinit?r=%d&pass_ticket=%s",
                bot.session().getUrl(), r, bot.session().getPassTicket());

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest()));

        WebInitResponse webInitResponse = response.parse(WebInitResponse.class);

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
        Thread thread = new Thread(new Loop(bot));
        thread.setName("wechat-listener");
        thread.setDaemon(true);
        thread.start();
    }

    class Loop implements Runnable {
        private WeChatBot bot;
        private long      lastCheckTs;

        Loop(WeChatBot bot) {
            this.bot = bot;
        }

        @Override
        public void run() {
            while (bot.isRunning()) {
                this.lastCheckTs = System.currentTimeMillis();
                SyncCheckRet syncCheckRet = LoginHandler.this.syncCheck();

                if (syncCheckRet.getCode() == 1100) {
                    log.info("你在手机上登出了微信，债见");
                    break;
                }
                if (syncCheckRet.getCode() == 1101) {
                    log.info("你在其他地方登录了 WEB 版微信，债见");
                    break;
                }
                if (syncCheckRet.getCode() == 0) {
                    switch (syncCheckRet.getSelector()) {
                        case 2:
                            WebSyncResponse webSyncResponse = LoginHandler.this.webSync();
                            if (null != webSyncResponse) {
                                bot.getMessageHandler().handleMsg(webSyncResponse);
                            }
                            break;
                        case 6:
                            log.info("收到疑似红包消息");
                            break;
                        case 7:
                            log.info("你在手机上玩微信被我发现了");
                            break;
                        default:
                            DateUtils.sleep(100);
                            break;
                    }
                }
                if (System.currentTimeMillis() - this.lastCheckTs <= 20) {
                    DateUtils.sleep(System.currentTimeMillis() - this.lastCheckTs);
                }
            }
        }
    }

    /**
     * 心跳检查
     *
     * @return
     */
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
    private WebSyncResponse webSync() {
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
        bot.api().cookies().clear();
    }

}
