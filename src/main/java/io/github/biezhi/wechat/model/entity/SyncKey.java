package io.github.biezhi.wechat.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author biezhi
 *         15/06/2017
 */
public class SyncKey implements Serializable {

    private int Count;
    private List<SyncKeyEntity> List;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public java.util.List<SyncKeyEntity> getList() {
        return List;
    }

    public void setList(java.util.List<SyncKeyEntity> list) {
        List = list;
    }
}
