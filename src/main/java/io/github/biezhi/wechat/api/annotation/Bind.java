package io.github.biezhi.wechat.api.annotation;

import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.enums.MsgType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定消息监听注解
 * <p>
 * 用于在机器人实现类中实现某类消息的回调
 *
 * @author biezhi
 * @date 2018/1/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bind {

    /**
     * 接受聊天的消息类型
     *
     * @return
     */
    MsgType[] msgType() default {MsgType.TEXT};

    /**
     * 接受聊天的账户类型
     *
     * @return
     */
    AccountType[] accountType() default {AccountType.TYPE_FRIEND, AccountType.TYPE_GROUP};

}