package io.github.biezhi.wechat.model.entity;

import io.github.biezhi.wechat.Constant;
import io.github.biezhi.wechat.model.request.BaseRequest;
import io.github.biezhi.wechat.model.request.LoginRequest;

public class WechatMeta {

    private String base_uri, redirect_uri, webpush_url = Constant.BASE_URL;

    private String uuid;
    private String skey;
    private String synckey;
    private String wxsid;
    private String wxuin;
    private String pass_ticket;
    private String deviceId = "e" + System.currentTimeMillis();

    private String cookie;

    private BaseRequest baseRequest;
    private SyncKey SyncKey;
    private WechatUser User;

    public WechatMeta() {

    }

    public String getBase_uri() {
        return base_uri;
    }

    public void setBase_uri(String base_uri) {
        this.base_uri = base_uri;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getWebpush_url() {
        return webpush_url;
    }

    public void setWebpush_url(String webpush_url) {
        this.webpush_url = webpush_url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getSynckey() {
        return synckey;
    }

    public void setSynckey(String synckey) {
        this.synckey = synckey;
    }

    public String getWxsid() {
        return wxsid;
    }

    public void setWxsid(String wxsid) {
        this.wxsid = wxsid;
    }

    public String getWxuin() {
        return wxuin;
    }

    public void setWxuin(String wxuin) {
        this.wxuin = wxuin;
    }

    public String getPass_ticket() {
        return pass_ticket;
    }

    public void setPass_ticket(String pass_ticket) {
        this.pass_ticket = pass_ticket;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public BaseRequest getBaseRequest() {
        return baseRequest;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
    }

    public io.github.biezhi.wechat.model.entity.SyncKey getSyncKey() {
        return SyncKey;
    }

    public void setSyncKey(io.github.biezhi.wechat.model.entity.SyncKey syncKey) {
        SyncKey = syncKey;
    }

    public WechatUser getUser() {
        return User;
    }

    public void setUser(WechatUser user) {
        User = user;
    }
}
