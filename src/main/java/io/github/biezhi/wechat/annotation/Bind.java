package io.github.biezhi.wechat.annotation;

import io.github.biezhi.wechat.enums.MsgType;

/**
 * @author biezhi
 * @date 2018/1/19
 */
public @interface Bind {

    MsgType msgType() default MsgType.TEXT;

}
