package io.github.biezhi.wechat.api.model;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.enums.AccountType;
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

    /**
     * 回调给客户端
     *
     * @param bot
     * @param message
     * @param <T>
     */
    public <T extends WeChatBot> void call(T bot, WeChatMessage message) {
        AccountType accountType = bot.api().getAccountById(message.getFromUserName()).getAccountType();
        if (accountTypes.contains(accountType)) {
            try {
                method.invoke(bot, message);
            } catch (Exception e) {
                throw new WeChatException(e);
            }
        }
    }

}
