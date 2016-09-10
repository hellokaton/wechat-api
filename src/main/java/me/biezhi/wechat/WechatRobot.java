package me.biezhi.wechat;

import java.awt.EventQueue;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;
import com.blade.kit.json.JSONObject;

import me.biezhi.wechat.exception.WechatException;
import me.biezhi.wechat.listener.WechatListener;
import me.biezhi.wechat.model.WechatContact;
import me.biezhi.wechat.model.WechatMeta;
import me.biezhi.wechat.model.WechatRequest;
import me.biezhi.wechat.service.WechatService;
import me.biezhi.wechat.service.WechatServiceImpl;
import me.biezhi.wechat.ui.QRCodeFrame;
import me.biezhi.wechat.util.CookieUtil;
import me.biezhi.wechat.util.JSUtil;
import me.biezhi.wechat.util.Matchers;

/**
 * Hello world!
 *
 */
public class WechatRobot {

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatRobot.class);

	private WechatListener wechatListener = new WechatListener();
	private WechatService wechatService = new WechatServiceImpl();

	private int tip = 0;
	private String base_uri, redirect_uri, webpush_url = Constant.BASE_URL;

	private WechatMeta wechatMeta = new WechatMeta();
	private WechatRequest wechatRequest;
	private WechatContact wechatContact;

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

		LOGGER.info("[*] 获取到uuid为 [{}]", uuid);
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

		LOGGER.info("[*] " + request.toString());

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
				LOGGER.info("[*] 成功扫描,请在手机上点击确认以登录");
				tip = 0;
			} else if (code.equals("200")) {
				LOGGER.info("[*] 正在登录...");
				String pm = Matchers.match("window.redirect_uri=\"(\\S+?)\";", res);
				String redirectHost = "wx.qq.com";
				try {
					URL pmURL = new URL(pm);
					redirectHost = pmURL.getHost();
				} catch (MalformedURLException e) {
					throw new WechatException(e);
				}
				String pushServer = JSUtil.getPushServer(redirectHost);
				this.webpush_url = "https://" + pushServer + "/cgi-bin/mmwebwx-bin";
				this.redirect_uri = pm + "&fun=new";

				LOGGER.info("[*] redirect_uri={}", this.redirect_uri);

				this.base_uri = this.redirect_uri.substring(0, this.redirect_uri.lastIndexOf("/"));

				LOGGER.info("[*] base_uri={}", this.base_uri);
			} else if (code.equals("408")) {
				throw new WechatException("登录超时");
			} else {
				LOGGER.info("[*] 扫描code={}", code);
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

		HttpRequest request = HttpRequest.get(this.redirect_uri);

		LOGGER.info("[*] " + request);

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

		LOGGER.info("[*] skey[{}]", wechatMeta.getSkey());
		LOGGER.info("[*] wxsid[{}]", wechatMeta.getWxsid());
		LOGGER.info("[*] wxuin[{}]", wechatMeta.getWxuin());
		LOGGER.info("[*] pass_ticket[{}]", wechatMeta.getPass_ticket());
		LOGGER.info("[*] 微信登录成功");
	}

	/**
	 * 微信初始化
	 */
	public void wxInit() throws WechatException {
		this.wechatRequest = wechatService.wxInit(wechatMeta);
		LOGGER.info("[*] 微信初始化成功");
	}

	/**
	 * 微信状态通知
	 */
	public void wxStatusNotify() throws WechatException {
		wechatService.openStatusNotify(wechatMeta, wechatRequest);
		LOGGER.info("[*] 开启状态通知成功");
	}

	/**
	 * 获取联系人
	 */
	public boolean getContact() {
		this.wechatContact = wechatService.getContact(wechatMeta, wechatRequest);
		LOGGER.info("[*] 获取联系人成功");
		LOGGER.info("[*] 共有 %d 位联系人", wechatContact.getContactList().size());
		return false;
	}

	public void listenMsgMode() {
		wechatListener.start(this);
	}

	public WechatService getWechatService() {
		return wechatService;
	}

	public void setWechatService(WechatService wechatService) {
		this.wechatService = wechatService;
	}

	public WechatMeta getWechatMeta() {
		return wechatMeta;
	}

	public void setWechatMeta(WechatMeta wechatMeta) {
		this.wechatMeta = wechatMeta;
	}

	public WechatRequest getWechatRequest() {
		return wechatRequest;
	}

	public void setWechatRequest(WechatRequest wechatRequest) {
		this.wechatRequest = wechatRequest;
	}

	public WechatContact getWechatContact() {
		return wechatContact;
	}

	public void setWechatContact(WechatContact wechatContact) {
		this.wechatContact = wechatContact;
	}

	public String getWebpush_url() {
		return webpush_url;
	}

	public void setWebpush_url(String webpush_url) {
		this.webpush_url = webpush_url;
	}

	public void start() throws WechatException {
		this.login();
		this.wxInit();
		this.wxStatusNotify();
		this.getContact();
		// 监听消息
		this.listenMsgMode();
	}
	
}