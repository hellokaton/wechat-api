package io.github.biezhi.wechat.model;

import lombok.Data;

/**
 * 保存消息返回
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
public class StorageResponse {

    /**
     * 是否保存成功
     */
    private boolean success;

    /**
     * 保存成功的个数
     */
    private Integer count;

}
