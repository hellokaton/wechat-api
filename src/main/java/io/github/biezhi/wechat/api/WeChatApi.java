package io.github.biezhi.wechat.api;

import io.github.biezhi.wechat.api.model.Account;
import io.github.biezhi.wechat.utils.StringUtils;

import java.util.regex.Pattern;

/**
 * @author biezhi
 * @date 2018/1/21
 */
public interface WeChatApi {

    Pattern UUID_PATTERN          = Pattern.compile("window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";");
    Pattern CHECK_LOGIN_PATTERN   = Pattern.compile("window.code=(\\d+)");
    Pattern PROCESS_LOGIN_PATTERN = Pattern.compile("window.redirect_uri=\"(\\S+)\";");
    Pattern SYNC_CHECK_PATTERN    = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"}");

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
