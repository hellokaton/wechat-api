package io.github.biezhi.wechat.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;

/**
 * 文件响应
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class FileResponse extends ApiResponse {

    private InputStream inputStream;

}
