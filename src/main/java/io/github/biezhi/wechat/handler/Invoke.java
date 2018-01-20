package io.github.biezhi.wechat.handler;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import io.github.biezhi.wechat.exception.WeChatException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 消息执行器
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
@AllArgsConstructor
public class Invoke {

    private Method            method;
    private List<AccountType> accountTypes;

    public <T extends WeChatBot> void call(T bot, WeChatMessage message) {
        if (accountTypes.contains(message.getAccountType())) {
            try {
                method.invoke(bot, message);
            } catch (Exception e) {
                throw new WeChatException(e);
            }
        }
    }

}
