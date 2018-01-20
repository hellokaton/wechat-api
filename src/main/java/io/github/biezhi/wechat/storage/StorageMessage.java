package io.github.biezhi.wechat.storage;

import io.github.biezhi.wechat.api.model.StorageResponse;
import io.github.biezhi.wechat.api.model.WeChatMessage;

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
     * 保存消息
     *
     * @param message
     * @return
     */
    StorageResponse save(WeChatMessage message);

}
