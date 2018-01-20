package io.github.biezhi.wechat.api.model;

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

}
