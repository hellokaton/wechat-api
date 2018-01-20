package io.github.biezhi.wechat.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 心跳检查返回
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
@AllArgsConstructor
public class SyncCheckRet {

    private int code;
    private int selector;

}
