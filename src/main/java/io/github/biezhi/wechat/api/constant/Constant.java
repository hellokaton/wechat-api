package io.github.biezhi.wechat.api.constant;

import java.util.*;

/**
 * 常量
 *
 * @author biezhi
 * @date 2018/1/18
 */
public interface Constant {

    String VERSION           = "1.0.5";
    String BASE_URL          = "https://login.weixin.qq.com";
    String GET               = "GET";
    String GROUP_BR          = ":<br/>";
    String GROUP_IDENTIFY    = "@@";
    String LOCATION_IDENTIFY = "/cgi-bin/mmwebwx-bin/webwxgetpubliclinkimg?url=";

    String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";

    /**
     * 特殊用户 须过滤
     */
    Set<String> API_SPECIAL_USER = new HashSet<>(
            Arrays.asList("newsapp", "filehelper", "weibo", "qqmail",
                    "fmessage", "tmessage", "qmessage", "qqsync",
                    "floatbottle", "lbsapp", "shakeapp", "medianote",
                    "qqfriend", "readerapp", "blogapp", "facebookapp",
                    "masssendapp", "meishiapp", "feedsapp", "voip",
                    "blogappweixin", "brandsessionholder", "weixin",
                    "weixinreminder", "officialaccounts", "wxitil",
                    "notification_messages", "wxid_novlwrv3lqwv11",
                    "gh_22b87fa7cb3c", "userexperience_alarm"));

    /**
     * index url
     */
    List<String> INDEX_URL = new ArrayList<>(
            Arrays.asList("wx2.qq.com", "wx8.qq.com",
                    "wx.qq.com", "web2.wechat.com", "wechat.com"));

    /**
     * file url
     */
    List<String> FILE_URL = new ArrayList<>(
            Arrays.asList("file.wx2.qq.com", "file.wx8.qq.com",
                    "file.wx.qq.com", "file.web2.wechat.com", "file.web.wechat.com"));

    /**
     * webpush url
     */
    List<String> WEB_PUSH_URL = new ArrayList<>(
            Arrays.asList("wx2.qq.com", "wx8.qq.com",
                    "wx.qq.com", "web2.wechat.com", "web.wechat.com"));

}