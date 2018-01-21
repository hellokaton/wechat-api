package io.github.biezhi.wechat.api.model;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.exception.WeChatException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 消息执行器
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
@Slf4j
@AllArgsConstructor
public class Invoke {

    private Method            method;
    private List<AccountType> accountTypes;
    private MsgType           msgType;

    /**
     * 回调给客户端
     *
     * @param bot
     * @param message
     * @param <T>
     */
    public <T extends WeChatBot> void call(T bot, WeChatMessage message) {
        try {
            Account account = bot.api().getAccountById(message.getFromUserName());
            if (null == account) {
                method.invoke(bot, message);
                return;
            }
            if (msgType == MsgType.ALL || msgType == message.getMsgType()) {
                if (message.getMsgType() == MsgType.ADD_FRIEND) {
                    method.invoke(bot, message);
                } else {
                    if (accountTypes.contains(account.getAccountType())) {
                        method.invoke(bot, message);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("回调给客户端出错: {}\r\n", message, e);
        }
    }

}
