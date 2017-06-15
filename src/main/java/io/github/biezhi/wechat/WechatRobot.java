package io.github.biezhi.wechat;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.UIManager;

import io.github.biezhi.wechat.listener.WechatListener;
import io.github.biezhi.wechat.service.WechatService;
import io.github.biezhi.wechat.ui.QRCodeFrame;
import io.github.biezhi.wechat.util.CookieUtil;
import io.github.biezhi.wechat.util.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;
import com.blade.kit.json.JSONObject;

import io.github.biezhi.wechat.exception.WechatException;
import io.github.biezhi.wechat.model.entity.WechatMeta;
import io.github.biezhi.wechat.service.WechatServiceImpl;

/**
 * Hello world!
 */
public class WechatRobot {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatRobot.class);

    private int tip = 0;
    private WechatListener wechatListener = new WechatListener();
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

        LOGGER.info("获取到uuid为 [{}]", uuid);
        String url = Constant.QRCODE_URL + uuid;
        final File output = new File("temp.jpg");
        HttpRequest.post(url, true, "t", "webwx", "_", DateKit.getCurrentUnixTime()).receive(output);

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
                DateKit.getCurrentUnixTime());

        LOGGER.info("等待登录...");
        LOGGER.debug("" + request.toString());

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
                LOGGER.info("成功扫描,请在手机上点击确认以登录");
                tip = 0;
            } else if (code.equals("200")) {
                LOGGER.info("正在登录...");
                String pm = Matchers.match("window.redirect_uri=\"(\\S+?)\";", res);
                String redirect_uri = pm + "&fun=new";
                wechatMeta.setRedirect_uri(redirect_uri);

                String base_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf("/"));
                wechatMeta.setBase_uri(base_uri);

                LOGGER.debug("redirect_uri={}", redirect_uri);
                LOGGER.debug("base_uri={}", base_uri);
            } else if (code.equals("408")) {
                throw new WechatException("登录超时");
            } else {
                LOGGER.info("扫描code={}", code);
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

        LOGGER.debug("" + request);
        String res = request.body();
        wechatMeta.setCookie(CookieUtil.getCookie(request));
        request.disconnect();

        if (StringKit.isBlank(res)) {
            throw new WechatException("登录失败");
        }
        wechatMeta.setSkey(Matchers.match("<skey>(\\S+)</skey>", res));
        wechatMeta.setWxsid(Matchers.match("<wxsid>(\\S+)</wxsid>", res));
        wechatMeta.setWxuin(Matchers.match("<wxuin>(\\S+)</wxuin>", res));
        wechatMeta.setPass_ticket(Matchers.match("<pass_ticket>(\\S+)</pass_ticket>", res));

        JSONObject baseRequest = new JSONObject();
        baseRequest.put("Uin", wechatMeta.getWxuin());
        baseRequest.put("Sid", wechatMeta.getWxsid());
        baseRequest.put("Skey", wechatMeta.getSkey());
        baseRequest.put("DeviceID", wechatMeta.getDeviceId());
        wechatMeta.setBaseRequest(baseRequest);

        LOGGER.debug("skey [{}]", wechatMeta.getSkey());
        LOGGER.debug("wxsid [{}]", wechatMeta.getWxsid());
        LOGGER.debug("wxuin [{}]", wechatMeta.getWxuin());
        LOGGER.debug("pass_ticket [{}]", wechatMeta.getPass_ticket());
        File output = new File("temp.jpg");
        if (output.exists()) {
            output.delete();
        }
    }

    public void start() throws WechatException {

        this.login();
        LOGGER.info("微信登录成功");

        LOGGER.info("微信初始化...");
        wechatService.wxInit(wechatMeta);
        LOGGER.info("微信初始化成功");

        LOGGER.info("开启状态通知...");
        wechatService.openStatusNotify(wechatMeta);
        LOGGER.info("开启状态通知成功");

        LOGGER.info("获取联系人...");
        Constant.CONTACT = wechatService.getContact(wechatMeta);
        LOGGER.info("获取联系人成功");
        LOGGER.info("共有 {} 位联系人", Constant.CONTACT.getContactList().size());

        // 监听消息
        wechatListener.start(wechatService, wechatMeta);
    }

}