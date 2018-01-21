package io.github.biezhi.wechat.api.enums;

import lombok.Getter;

/**
 * API URL
 *
 * @author biezhi
 * @date 2018/1/21
 */
@Getter
public enum ApiURL {

    IMAGE("%s/webwxgetmsgimg?msgid=%s&skey=%s", ".jpg", "images"),
    HEAD_IMG("%s/webwxgetheadimg?username=%s&skey=%s", ".jpg", "headimg"),
    ICON("%s/webwxgeticon?username=%s&skey=%s", ".jpg", "icons"),
    VOICE("%s/webwxgetvoice?msgid=%s&skey=%s", ".mp3", "voice"),
    VIDEO("%s/webwxgetvideo?msgid=%s&skey=%s", ".mp4", "video");

    private String url;
    private String suffix;
    private String dir;

    ApiURL(String url) {
        this.url = url;
    }

    ApiURL(String url, String suffix, String dir) {
        this.url = url;
        this.suffix = suffix;
        this.dir = dir;
    }

}
