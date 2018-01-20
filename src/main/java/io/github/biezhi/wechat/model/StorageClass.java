package io.github.biezhi.wechat.model;

import io.github.biezhi.wechat.WeChatBot;
import lombok.Data;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class StorageClass {

    private WeChatBot bot;

    private String userName;
    private String nickName;

    public StorageClass(WeChatBot bot) {
        this.bot = bot;
    }

    public User searchChatRooms(String actualOpposite) {
        return null;
    }

}
