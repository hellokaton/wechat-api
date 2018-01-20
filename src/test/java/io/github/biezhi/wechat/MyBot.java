package io.github.biezhi.wechat;

import io.github.biezhi.wechat.annotation.Bind;
import io.github.biezhi.wechat.constant.Config;
import io.github.biezhi.wechat.enums.MsgType;
import io.github.biezhi.wechat.model.Message;
import io.github.biezhi.wechat.model.StorageResponse;
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
            public StorageResponse saveBatch(List<Message> messages) {
                System.out.println("接收到");
                return StorageResponse.builder().success(true).build();
            }
        };
    }

    @Bind(msgType = MsgType.TEXT)
    public void handleText(Message message) {
        log.info("接收到 [{}] 的消息: {}", message.getContent());
    }

    public static void main(String[] args) {
        new MyBot(Config.me().showTerminal(true)).start();
    }

}
