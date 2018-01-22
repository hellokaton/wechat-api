package io.github.biezhi.wechat.api;

import io.github.biezhi.wechat.api.model.*;
import io.github.biezhi.wechat.api.response.MediaResponse;
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
    boolean sendText(String toUser, String msg);

    /**
     * 发送图片
     *
     * @param toUser
     * @param filePath
     */
    boolean sendImg(String toUser, String filePath);

    /**
     * 发送文件
     *
     * @param toUser
     * @param filePath
     */
    boolean sendFile(String toUser, String filePath);

    /**
     * 上传附件
     *
     * @param toUser
     * @param filePath
     * @return
     */
    MediaResponse uploadMedia(String toUser, String filePath);

    /**
     * 根据UserName获取账号信息
     *
     * @param id
     * @return
     */
    Account getAccountById(String id);

    /**
     * 根据备注或昵称查找账户
     *
     * @param name
     * @return
     */
    Account getAccountByName(String name);

    /**
     * 撤回本条消息 (应为 2 分钟内发出的消息)
     *
     * @param msgId  消息id
     * @param toUser 发送消息的人
     * @return
     */
    boolean revokeMsg(String msgId, String toUser);

    /**
     * 添加好友验证
     *
     * @param recommend 好友信息
     */
    boolean verify(Recommend recommend);

    /**
     * 添加好友
     *
     * @param friend 好友的UserName
     * @param msg    添加好友时的消息
     * @return
     */
    boolean addFriend(String friend, String msg);

    /**
     * 创建群聊
     *
     * @param topic   群名称
     * @param members 群成员UserName列表
     */
    boolean createChatRoom(String topic, List<String> members);

    /**
     * 从群聊中移除某个群成员
     *
     * @param member
     * @param group
     */
    boolean removeMemberByGroup(String member, String group);

    /**
     * 邀请好友进群
     *
     * @param member
     * @param group
     * @return
     */
    boolean inviteJoinGroup(String member, String group);

    /**
     * 修改群名
     *
     * @param oldTopic 旧群名
     * @param newTopic 新群名
     * @return
     */
    boolean modifyGroupName(String oldTopic, String newTopic);
}