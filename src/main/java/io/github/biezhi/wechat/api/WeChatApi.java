package io.github.biezhi.wechat.api;

import io.github.biezhi.wechat.api.model.Account;
import io.github.biezhi.wechat.api.model.SyncCheckRet;
import io.github.biezhi.wechat.api.response.WebSyncResponse;

import java.util.regex.Pattern;

/**
 * 微信API
 *
 * @author biezhi
 * @date 2018/1/21
 */
public interface WeChatApi {

    /**
     * 扫码登录
     *
     * @return
     */
    void login();

    /**
     * 退出登录
     */
    void logout();

    /**
     * 心跳检测
     *
     * @return
     */
    SyncCheckRet syncCheck();

    /**
     * 拉取新消息
     *
     * @return
     */
    WebSyncResponse webSync();

    /**
     * 处理消息
     *
     * @param webSyncResponse
     */
    void handleMsg(WebSyncResponse webSyncResponse);

    /**
     * 发送文本消息
     *
     * @param toUser
     * @param msg
     */
    void sendText(String toUser, String msg);

    /**
     * 发送文件
     *
     * @param toUser
     * @param filePath
     */
    void sendFile(String toUser, String filePath);

    Account getAccountById(String id);

}
