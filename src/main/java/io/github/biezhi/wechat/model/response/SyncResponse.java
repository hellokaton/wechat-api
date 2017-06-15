package io.github.biezhi.wechat.model.response;

import io.github.biezhi.wechat.model.response.BaseResponse;

import java.io.Serializable;

/**
 * @author biezhi
 *         15/06/2017
 */
public class SyncResponse implements Serializable {

    private BaseResponse BaseResponse;
    private int Ret;

    public BaseResponse getBaseResponse() {
        return BaseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        BaseResponse = baseResponse;
    }

    public int getRet() {
        return Ret;
    }

    public void setRet(int ret) {
        Ret = ret;
    }
}
