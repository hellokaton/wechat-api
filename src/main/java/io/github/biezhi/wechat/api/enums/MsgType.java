package io.github.biezhi.wechat.api.enums;

/**
 * 消息类型
 * <p>
 * ALL: 所有消息
 * TEXT: 普通文本消息, type=1
 * IMAGE: 图片消息，type=3
 * VOICE: 音频消息, type=34
 * ADD_FRIEND: 添加好友请求, type=37
 * VIDEO: 视频消息, type=43, 62
 * PERSON_CARD: 个人名片, type=42
 * EMOTICONS: 动画表情, type=47
 * REVOKE_MSG: 撤回消息, type=10002
 * SYSTEM: 系统消息, type=10000
 * SHARE: 分享, type=49。AppMsgType=33 微信小程序 | AppMsgType=5 链接
 * CONTACT_INIT: 联系人初始化, type=51
 * UNKNOWN: 未知类型
 *
 * @author biezhi
 * @date 2018/1/19
 */
public enum MsgType {

    ALL, TEXT, IMAGE, EMOTICONS, VOICE, VIDEO, PERSON_CARD,
    SYSTEM, ADD_FRIEND, REVOKE_MSG, SHARE, CONTACT_INIT, UNKNOWN

}
