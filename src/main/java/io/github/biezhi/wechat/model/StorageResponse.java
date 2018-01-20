package io.github.biezhi.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保存消息返回
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
