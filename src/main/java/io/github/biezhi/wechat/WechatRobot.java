package io.github.biezhi.wechat;

import com.github.kevinsawicki.http.HttpRequest;
import io.github.biezhi.wechat.event.EventManager;
import io.github.biezhi.wechat.exception.WechatException;
import io.github.biezhi.wechat.listener.WechatListener;
import io.github.biezhi.wechat.model.entity.WechatMeta;
import io.github.biezhi.wechat.model.request.LoginRequest;
import io.github.biezhi.wechat.service.WechatService;
import io.github.biezhi.wechat.service.WechatServiceImpl;
import io.github.biezhi.wechat.ui.QRCodeFrame;
import io.github.biezhi.wechat.util.CookieUtils;
import io.github.biezhi.wechat.util.Matchers;
import io.github.biezhi.wechat.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 微信机器人
 */
public class WechatRobot {

    private static final Logger log = LoggerFactory.getLogger(WechatRobot.class);

    private int tip = 0;
    private EventManager eventManager = new EventManager();

    private WechatListener wechatListener = new WechatListener(eventManager);
    private WechatService wechatService = new WechatServiceImpl();
    private WechatMeta wechatMeta = new WechatMeta();
    private QRCodeFrame qrCodeFrame;

    public WechatRobot() {
        System.setProperty("https.protocols", "TLSv1");
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    /**
     * 显示二维码
     *
     * @return
     */
    public void showQrCode() throws WechatException {
        String uuid = wechatService.getUUID();
        wechatMeta.setUuid(uuid);

        log.info("获取到uuid为 [{}]", uuid);
        String url = Constant.QRCODE_URL + uuid;
        final File output = new File("temp.jpg");
        HttpRequest.post(url, true, "t", "webwx", "_", System.currentTimeMillis()).receive(output);

        if (null != output && output.exists() && output.isFile()) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                        qrCodeFrame = new QRCodeFrame(output.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 等待登录
     */
    public String waitForLogin() throws WechatException {
        this.tip = 1;
        String url = "https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login";
        HttpRequest request = HttpRequest.get(url, true, "tip", this.tip, "uuid", wechatMeta.getUuid(), "_",
                System.currentTimeMillis());

        log.info("等待登录...");
        log.debug("" + request.toString());

        String res = request.body();
        request.disconnect();

        if (null == res) {
            throw new WechatException("扫描二维码验证失败");
        }
        String code = Matchers.match("window.code=(\\d+);", res);
        if (null == code) {
            throw new WechatException("扫描二维码验证失败");
        } else {
            if (code.equals("201")) {
                log.info("成功扫描,请在手机上点击确认以登录");
                tip = 0;
            } else if (code.equals("200")) {
                log.info("正在登录...");
                String pm = Matchers.match("window.redirect_uri=\"(\\S+?)\";", res);
                String redirect_uri = pm + "&fun=new";
                wechatMeta.setRedirect_uri(redirect_uri);

                String base_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf("/"));
                wechatMeta.setBase_uri(base_uri);

                log.debug("redirect_uri={}", redirect_uri);
                log.debug("base_uri={}", base_uri);
            } else if (code.equals("408")) {
                throw new WechatException("登录超时");
            } else {
                log.info("扫描code={}", code);
            }
        }
        return code;
    }

    public void closeQrWindow() {
        qrCodeFrame.dispose();
    }

    /**
     * 登录
     */
    public void login() throws WechatException {
        HttpRequest request = HttpRequest.get(wechatMeta.getRedirect_uri());

        log.debug("" + request);
        String res = request.body();
        wechatMeta.setCookie(CookieUtils.getCookie(request));
        request.disconnect();

        if (StringUtils.isBlank(res)) {
            throw new WechatException("登录失败");
        }
        wechatMeta.setSkey(Matchers.match("<skey>(\\S+)</skey>", res));
        wechatMeta.setWxsid(Matchers.match("<wxsid>(\\S+)</wxsid>", res));
        wechatMeta.setWxuin(Matchers.match("<wxuin>(\\S+)</wxuin>", res));
        wechatMeta.setPass_ticket(Matchers.match("<pass_ticket>(\\S+)</pass_ticket>", res));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUin(wechatMeta.getWxuin());
        loginRequest.setSid(wechatMeta.getWxsid());
        loginRequest.setSkey(wechatMeta.getSkey());
        loginRequest.setDeviceID(wechatMeta.getDeviceId());

//        wechatMeta.setBaseRequest(loginRequest);

        log.debug("skey [{}]", wechatMeta.getSkey());
        log.debug("wxsid [{}]", wechatMeta.getWxsid());
        log.debug("wxuin [{}]", wechatMeta.getWxuin());
        log.debug("pass_ticket [{}]", wechatMeta.getPass_ticket());
        File output = new File("temp.jpg");
        if (output.exists()) {
            output.delete();
        }
    }

    public void start() throws WechatException {

        this.login();
        log.info("微信登录成功");

        log.info("微信初始化...");
        wechatService.wxInit(wechatMeta);
        log.info("微信初始化成功");

        log.info("开启状态通知...");
        wechatService.openStatusNotify(wechatMeta);
        log.info("开启状态通知成功");

        log.info("获取联系人...");
        Constant.CONTACT = wechatService.getContact(wechatMeta);
        log.info("获取联系人成功");
        log.info("共有 [{}] 位联系人", Constant.CONTACT.getContactList().size());

        // 监听消息
        wechatListener.start(wechatService, wechatMeta);
    }

}