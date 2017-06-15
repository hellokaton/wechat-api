package io.github.biezhi.wechat.model.request;

import io.github.biezhi.wechat.model.entity.SyncKey;

import java.io.Serializable;

/**
 * @author biezhi
 *         15/06/2017
 */
public class SyncRequest implements Serializable {

    private BaseRequest BaseRequest;
    private io.github.biezhi.wechat.model.entity.SyncKey SyncKey;
    private long rr = System.currentTimeMillis();

    public io.github.biezhi.wechat.model.request.BaseRequest getBaseRequest() {
        return BaseRequest;
    }

    public void setBaseRequest(io.github.biezhi.wechat.model.request.BaseRequest baseRequest) {
        BaseRequest = baseRequest;
    }

    public io.github.biezhi.wechat.model.entity.SyncKey getSyncKey() {
        return SyncKey;
    }

    public void setSyncKey(io.github.biezhi.wechat.model.entity.SyncKey syncKey) {
        SyncKey = syncKey;
    }

    public long getRr() {
        return rr;
    }

    public void setRr(long rr) {
        this.rr = rr;
    }
}
