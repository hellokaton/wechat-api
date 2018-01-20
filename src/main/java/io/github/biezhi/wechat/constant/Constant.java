package io.github.biezhi.wechat.constant;

import java.util.*;

/**
 * 常量
 *
 * @author biezhi
 * @date 2018/1/18
 */
public interface Constant {

    String VERSION  = "0.1.0";
    String BASE_URL = "https://login.weixin.qq.com";
    String GET      = "GET";

    String OS         = System.getenv("os.name");
    String DIR        = "";
    String DEFAULT_QR = "QR.png";
    String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
    int[]  TIMEOUT    = new int[]{10, 60};

    int MAX_GET_NUMBER = 50;

    // 特殊用户 须过滤
    Set<String> API_SPECIAL_USER = new HashSet<String>(Arrays.asList("newsapp", "filehelper", "weibo", "qqmail",
            "fmessage", "tmessage", "qmessage", "qqsync",
            "floatbottle", "lbsapp", "shakeapp", "medianote",
            "qqfriend", "readerapp", "blogapp", "facebookapp",
            "masssendapp", "meishiapp", "feedsapp", "voip",
            "blogappweixin", "brandsessionholder", "weixin",
            "weixinreminder", "officialaccounts", "wxitil",
            "notification_messages", "wxid_novlwrv3lqwv11",
            "gh_22b87fa7cb3c", "userexperience_alarm"));

    List<String> INDEX_URL = new ArrayList<String>(
            Arrays.asList("wx2.qq.com", "wx8.qq.com", "wx.qq.com",
                    "web2.wechat.com", "wechat.com"));

    List<String> FILE_URL = new ArrayList<String>(
            Arrays.asList("file.wx2.qq.com", "file.wx8.qq.com", "file.wx.qq.com",
                    "file.web2.wechat.com", "file.web.wechat.com"));

    List<String> WEBPUSH_URL = new ArrayList<String>(
            Arrays.asList("webpush.wx2.qq.com", "webpush.wx8.qq.com", "webpush.wx.qq.com",
                    "webpush.web2.wechat.com", "webpush.web.wechat.com"));

    String TEXT       = "Text";
    String MAP        = "Map";
    String CARD       = "Card";
    String NOTE       = "Note";
    String SHARING    = "Sharing";
    String PICTURE    = "Picture";
    String VOICE      = "Recording";
    String RECORDING  = "Recording";
    String ATTACHMENT = "Attachment";
    String VIDEO      = "Video";
    String FRIENDS    = "Friends";
    String SYSTEM     = "System";

    Set<String> INCOME_MSG = new HashSet<String>(Arrays.asList(TEXT, MAP, CARD, NOTE, SHARING, PICTURE,
            RECORDING, VOICE, ATTACHMENT, VIDEO, FRIENDS, SYSTEM));

    /**
     * 资源存储的文件夹，包括图片、视频、音频
     */
    String CONF_ASSETS_DIR         = "wechat.asstes-path";
    String CONF_ASSETS_DIR_DEFAULT = "assets";

    /**
     * 是否输出二维码到终端
     */
    String CONF_PRINT_TERMINAL         = "wechat.print-terminal";
    String CONF_PRINT_TERMINAL_DEFAULT = "false";

    /**
     * 自动回复消息，测试时用
     */
    String CONF_AUTO_REPLY         = "wechat.autoreply";
    String CONF_AUTO_REPLY_DEFAULT = "false";
}