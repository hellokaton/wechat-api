package io.github.biezhi.wechat.request;

import io.github.biezhi.wechat.response.FileResponse;

/**
 * @author biezhi
 * @date 2018/1/18
 */
public class FileRequest extends ApiRequest<FileRequest, FileResponse> {

    public FileRequest(String url) {
        super(url, FileResponse.class);
    }

}
