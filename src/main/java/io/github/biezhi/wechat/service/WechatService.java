package io.github.biezhi.wechat.service;

import io.github.biezhi.wechat.exception.WechatException;
import io.github.biezhi.wechat.model.entity.AddMessage;
import io.github.biezhi.wechat.model.entity.WechatContact;
import io.github.biezhi.wechat.model.entity.WechatMeta;
import io.github.biezhi.wechat.model.response.BaseResponse;

import java.util.List;

public interface WechatService {

    /**
     * 获取UUID
     *
     * @return
     */
    String getUUID() throws WechatException;

    /**
     * 微信初始化
     *
     * @param wechatMeta
     * @throws WechatException
     */
    void wxInit(WechatMeta wechatMeta) throws WechatException;

    /**
     * 开启状态通知
     *
     * @return
     */
    void openStatusNotify(WechatMeta wechatMeta) throws WechatException;

    /**
     * 获取联系人
     *
     * @param wechatMeta
     * @return
     */
    WechatContact getContact(WechatMeta wechatMeta) throws WechatException;

    /**
     * 选择同步线路
     *
     * @param wechatMeta
     * @return
     * @throws WechatException
     */
    void choiceSyncLine(WechatMeta wechatMeta) throws WechatException;

    /**
     * 消息检查
     *
     * @param wechatMeta
     * @return
     */
    int[] syncCheck(WechatMeta wechatMeta) throws WechatException;

    /**
     * 处理聊天信息
     *
     * @param wechatMeta
     * @param message
     */
    void handleMsg(WechatMeta wechatMeta, List<AddMessage> addMsgList) throws WechatException;

    /**
     * 获取最新消息
     *
     * @param meta
     * @return
     */
    BaseResponse webwxsync(WechatMeta meta) throws WechatException;
}
