package io.github.biezhi.wechat.event;

/**
 * 事件类型
 *
 * @author biezhi
 *         15/06/2017
 */
public enum EventType {

    LOGIN_SUCCESS("登录成功"),
    OFFLINE("下线");

    private String msg;

    EventType(String msg) {
        this.msg = msg;
    }

}
