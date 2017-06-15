package io.github.biezhi.wechat.model.entity;

import java.io.Serializable;

/**
 * @author biezhi
 *         15/06/2017
 */
public class SyncKeyEntity implements Serializable {

    private Integer Key;
    private Integer Val;

    public Integer getKey() {
        return Key;
    }

    public void setKey(Integer key) {
        Key = key;
    }

    public Integer getVal() {
        return Val;
    }

    public void setVal(Integer val) {
        Val = val;
    }
}
