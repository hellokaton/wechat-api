package io.github.biezhi.wechat.api.enums;

import lombok.Getter;

/**
 * 账号类型
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Getter
public enum AccountType {

    /**
     * 公众号/服务号
     */
    TYPE_MP,
    /**
     * 特殊账号
     */
    TYPE_SPECIAL,
    /**
     * 群组
     */
    TYPE_GROUP,
    /**
     * 好友
     */
    TYPE_FRIEND

}
