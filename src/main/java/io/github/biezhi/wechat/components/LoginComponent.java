package io.github.biezhi.wechat.components;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.constant.Constant;
import io.github.biezhi.wechat.constant.StateCode;
import io.github.biezhi.wechat.model.*;
import io.github.biezhi.wechat.request.BaseRequest;
import io.github.biezhi.wechat.request.FileRequest;
import io.github.biezhi.wechat.request.JsonRequest;
import io.github.biezhi.wechat.request.StringRequest;
import io.github.biezhi.wechat.response.*;
import io.github.biezhi.wechat.utils.DateUtils;
import io.github.biezhi.wechat.utils.QRCodeUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.biezhi.wechat.constant.Constant.*;

/**
 * @author biezhi
 * @date 2018/1/18
 */
@Slf4j
public class LoginComponent {

    private static final Pattern UUID_PATTERN          = Pattern.compile("window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";");
    private static final Pattern CHECK_LOGIN_PATTERN   = Pattern.compile("window.code=(\\d+)");
    private static final Pattern PROCESS_LOGIN_PATTERN = Pattern.compile("window.redirect_uri=\"(\\S+)\";");
    private static final Pattern SYNC_CHECK_PATTERN    = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"}");

    private WeChatBot bot;
    private String    uuid;
    private boolean   logging;

    public LoginComponent(WeChatBot weChatBot) {
        this.bot = weChatBot;
    }

    /**
     * 扫码登录
     */
    public void login() {
        if (bot.isRunning() || logging) {
            log.warn("WeChatBot has already logged in.");
            return;
        }
        this.logging = true;
        while (logging) {
            this.uuid = pushLogin();
            if (null == this.uuid) {
                log.info("Getting uuid of QR code.");
                while (null == this.getUUID()) {
                    DateUtils.sleep(10);
                }
                log.info("Download QR Code.");
                this.getQrImage(this.uuid, bot.config().showTerminal());
                log.info("Please scan the QR code to log in.");
            }
            Boolean isLoggedIn = false;
            while (null == isLoggedIn || !isLoggedIn) {
                String status = this.checkLogin(this.uuid);
                if (StateCode.SUCCESS.equals(status)) {
                    isLoggedIn = true;
                } else if ("201".equals(status)) {
                    if (null != isLoggedIn) {
                        log.info("Please press confirm on your phone.");
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
                log.info("Log in time out, reloading QR code.");
            }
        }
        log.info("Loading the contact, this may take a little while.");
        this.webInit();
        this.showMobileLogin();
        bot.getContactComponent().getContact();
//        utils.clear_screen()
//        if os.path.exists(picDir or config.DEFAULT_QR):
//        os.remove(picDir or config.DEFAULT_QR)
        log.info("Login successfully as {}", bot.loginSession().getNickName());
        this.startRevice();
        logging = false;
        DateUtils.sleep(9999999999999L);
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public String getUUID() {
        // 登录
        ApiResponse response = bot.execute(new StringRequest("https://login.weixin.qq.com/jslogin")
                .add("appid", "wx782c26e4c19acffb")
                .add("fun", "new"));

        Matcher matcher = UUID_PATTERN.matcher(response.getRawBody());
        if (matcher.find()) {
            if (StateCode.SUCCESS.equals(matcher.group(1))) {
                this.uuid = matcher.group(2);
            }
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
        String imgDir = bot.config().assetsDir() + "/qrcode/";
        FileResponse fileResponse = bot.download(
                new FileRequest(String.format("%s/qrcode/%s", Constant.BASE_URL, uid)));

        InputStream      inputStream      = fileResponse.getInputStream();
        FileOutputStream fileOutputStream = null;
        try {
            if (!new File(imgDir).isDirectory()) {
                new File(imgDir).mkdirs();
            }
            File qrCode = new File(imgDir, "qr.png");
            fileOutputStream = new FileOutputStream(qrCode);
            byte[] buffer = new byte[2048];
            int    len    = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            DateUtils.sleep(500);
            QRCodeUtils.showQrCode(qrCode, terminalShow);
        } catch (IOException e) {
            log.error("读取二维码失败", e);
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
            }
        }
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
                .add("loginicon", "true")
                .add("uuid", uid)
                .add("tip", "1")
                .add("r", (int) (-time / 1000) / 1579)
                .add("_", time)
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
        LoginSession loginSession = bot.loginSession();
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
        String wxuin = bot.api().cookie("wxUin");
        if (null == wxuin || wxuin.isEmpty()) {
            return null;
        }
        String url = String.format("%s/cgi-bin/mmwebwx-bin/webwxpushloginurl?uin=%s",
                Constant.BASE_URL, wxuin);

        JsonResponse jsonResponse = bot.execute(new JsonRequest(url));
        return jsonResponse.getString("uuid");
    }

    /**
     * 显示手机登录
     *
     * @return
     */
    public ReturnValue showMobileLogin() {
        log.info("Show Mobile Login Start");

        String url = String.format("%s/webwxstatusnotify?lang=zh_CN&pass_ticket=%s",
                bot.loginSession().getUrl(), bot.loginSession().getPassTicket());

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.loginSession().getBaseRequest())
                .add("Code", 3)
                .add("FromUserName", bot.loginSession().getUserName())
                .add("ToUserName", bot.loginSession().getUserName())
                .add("ClientMsgId", System.currentTimeMillis() / 1000));

        return new ReturnValue();
    }

    /**
     * web 初始化
     */
    public WebInitResponse webInit() {
        log.info("Web Init Start");
        int r = (int) (-System.currentTimeMillis() / 1000) / 1579;
        String url = String.format("%s/webwxinit?r=%d&pass_ticket=%s",
                bot.loginSession().getUrl(), r, bot.loginSession().getPassTicket());

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.loginSession().getBaseRequest()));

        WebInitResponse webInitResponse = response.parse(WebInitResponse.class);

        User    user    = webInitResponse.getUser();
        SyncKey syncKey = webInitResponse.getSyncKey();

        bot.loginSession().setInviteStartCount(webInitResponse.getInviteStartCount());
        bot.loginSession().setUser(user);
        bot.loginSession().setUserName(user.getUserName());
        bot.loginSession().setNickName(user.getNickName());
        bot.loginSession().setSyncKey(syncKey);

        bot.getMemberList().add(bot.loginSession().getUser());
        
        return webInitResponse;
    }

    public void startRevice() {
        bot.setRunning(true);
        Thread thread = new Thread(new Loop(bot));
        thread.setName("wechat-listener");
        thread.setDaemon(true);
        thread.start();
    }

    class Loop implements Runnable {
        private WeChatBot bot;
        private long      lastCheckTs;

        public Loop(WeChatBot bot) {
            this.bot = bot;
        }

        @Override
        public void run() {
            while (bot.isRunning()) {
                this.lastCheckTs = System.currentTimeMillis();
                SyncCheckRet syncCheckRet = LoginComponent.this.syncCheck();

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
                            WebSyncResponse webSyncResponse = LoginComponent.this.webSync();
                            if (null != webSyncResponse) {
                                bot.getMessageComponent().handleMsg(webSyncResponse);
                            }
                            break;
                        case 6:
                            log.info("收到疑似红包消息");
                            break;
                        case 7:
                            log.info("你在手机上玩微信被我发现了");
                            break;
                        case 0:
                            DateUtils.sleep(100);
                        default:
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
        String url = String.format("%s/synccheck", bot.loginSession().getSyncOrUrl());
        try {
            ApiResponse response = bot.execute(new StringRequest(url)
                    .add("r", System.currentTimeMillis())
                    .add("skey", bot.loginSession().getSKey())
                    .add("sid", bot.loginSession().getWxSid())
                    .add("uin", bot.loginSession().getWxUin())
                    .add("deviceid", bot.loginSession().getDeviceId())
                    .add("synckey", bot.loginSession().getSynckeyStr())
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
    public WebSyncResponse webSync() {
        String url = String.format("%s/webwxsync?sid=%s&sKey=%s&passTicket=%s",
                bot.loginSession().getUrl(), bot.loginSession().getWxSid(),
                bot.loginSession().getSKey(), bot.loginSession().getPassTicket());

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.loginSession().getBaseRequest())
                .add("SyncKey", bot.loginSession().getSyncKey())
                .add("rr", ~(System.currentTimeMillis() / 1000)));

        WebSyncResponse webSyncResponse = response.parse(WebSyncResponse.class);
        if (!webSyncResponse.success()) {
            return null;
        }

        bot.loginSession().setSyncKey(webSyncResponse.getSyncKey());

        return webSyncResponse;
    }

    /**
     * 退出登录
     *
     * @return
     */
    public ReturnValue logout() {
        if (bot.isRunning()) {
            String url = String.format("%s/webwxlogout", bot.loginSession().getUrl());
            bot.execute(new StringRequest(url)
                    .add("redirect", 1)
                    .add("type", 1)
                    .add("sKey", bot.loginSession().getSKey()));
            bot.setRunning(false);
        }

        this.logging = false;
        bot.api().cookies().clear();
        return new ReturnValue();
    }

}
