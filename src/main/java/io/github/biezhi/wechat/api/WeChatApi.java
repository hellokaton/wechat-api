package io.github.biezhi.wechat.api;

import io.github.biezhi.wechat.api.model.*;
import io.github.biezhi.wechat.api.response.WebSyncResponse;

import java.util.List;

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
    void login(boolean autoLogin);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 加载联系人
     *
     * @param seq
     */
    void loadContact(int seq);

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
     * 处理消息，转换为 WeChatMessage 类型
     *
     * @param messages
     * @return
     */
    List<WeChatMessage> handleMsg(List<Message> messages);

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

    /**
     * 根据UserName获取账号信息
     *
     * @param id
     * @return
     */
    Account getAccountById(String id);

    /**
     * 添加好友验证
     *
     * @param recommend 好友信息
     */
    void verify(Recommend recommend);

}
