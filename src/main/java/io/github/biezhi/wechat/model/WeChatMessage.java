package io.github.biezhi.wechat.model;

import io.github.biezhi.wechat.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信消息
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatMessage {

    private Message raw;
    private String text;
    private String imagePath;
    private String videoPath;
    private String voicePath;
    private String fromUserName;
    private String fromNickName;
    private String fromRemarkName;

    public String getName() {
        return StringUtils.isEmpty(fromRemarkName) ? this.fromNickName : this.fromRemarkName;
    }

}
