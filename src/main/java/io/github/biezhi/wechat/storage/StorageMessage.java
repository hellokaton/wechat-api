package io.github.biezhi.wechat.storage;

import io.github.biezhi.wechat.enums.MsgType;
import io.github.biezhi.wechat.model.Message;
import io.github.biezhi.wechat.model.StorageResponse;

import java.util.List;

/**
 * 消息存储接口
 * <p>
 * 可存储到 MySQL、Redis、MapDB、本地硬盘
 *
 * @author biezhi
 * @date 2018/1/20
 */
public interface StorageMessage {

    /**
     * 要存储的消息类型列表
     *
     * @return
     */
    MsgType[] bindMsgType();

    /**
     * 保存一批消息
     *
     * @param messages
     * @return
     */
    StorageResponse saveBatch(List<Message> messages);

}
