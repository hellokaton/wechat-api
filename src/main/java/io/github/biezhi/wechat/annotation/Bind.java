package io.github.biezhi.wechat.annotation;

import io.github.biezhi.wechat.enums.MsgType;

/**
 * 绑定消息监听注解
 * <p>
 * 用于在机器人实现类中实现某类消息的回调
 *
 * @author biezhi
 * @date 2018/1/19
 */
public @interface Bind {

    MsgType[] msgType() default {MsgType.TEXT};

}