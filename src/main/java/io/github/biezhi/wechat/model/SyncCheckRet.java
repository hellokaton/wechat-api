package io.github.biezhi.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author biezhi
 * @date 2018/1/20
 */
@Data
@AllArgsConstructor
public class SyncCheckRet {

    private int code;
    private int selector;

}
