package io.github.biezhi.wechat;

import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.StorageResponse;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import io.github.biezhi.wechat.storage.StorageMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Slf4j
public class MyBot extends WeChatBot {

    public MyBot(Config config) {
        super(config);
    }

    @Override
    public StorageMessage storageMessage() {
        return new StorageMessage() {
            @Override
            public MsgType[] bindMsgType() {
                return new MsgType[]{MsgType.TEXT};
            }

            @Override
            public StorageResponse saveBatch(List<WeChatMessage> messages) {
                log.info("存储器接收到 {} 条消息", messages.size());
                return StorageResponse.builder().success(true).build();
            }
        };
    }

    @Bind(msgType = MsgType.TEXT)
    public void handleText(WeChatMessage message) {
        log.info("接收到 [{}] 的消息: {}", message.getName(), message.getText());
        this.sendText(message.getFromUserName(), message.getText() + " : 嘻嘻嘻 [坏笑]");
    }

    public static void main(String[] args) {
        new MyBot(Config.me().showTerminal(true)).start();
    }

}
