package io.github.biezhi.wechat.model.request;

import java.io.Serializable;

/**
 * @author biezhi
 *         15/06/2017
 */
public class WechatRequest implements Serializable {

    private BaseRequest BaseRequest;

    public BaseRequest getBaseRequest() {
        return BaseRequest;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        BaseRequest = baseRequest;
    }
}
