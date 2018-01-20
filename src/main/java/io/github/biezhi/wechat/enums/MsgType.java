package io.github.biezhi.wechat.enums;

/**
 * 消息类型
 * <p>
 * ALL: 所有消息
 * TEXT: 普通文本消息
 * IMAGE: 图片消息，msgType=3,47
 * VOICE: 音频消息
 * VIDEO: 视频消息
 * PERSON_CARD: 个人名片
 * REVOKE: 撤回消息
 *
 * @author biezhi
 * @date 2018/1/19
 */
public enum MsgType {

    ALL, TEXT, IMAGE, VOICE, VIDEO, PERSON_CARD, REVOKE

}
