package io.github.biezhi.wechat.model.request;

import io.github.biezhi.wechat.model.entity.AddMessage;

import java.io.Serializable;
import java.util.List;

/**
 * @author biezhi
 *         15/06/2017
 */
public class MessageRequest implements Serializable {

    private List<AddMessage> AddMsgList;

    public List<AddMessage> getAddMsgList() {
        return AddMsgList;
    }

    public void setAddMsgList(List<AddMessage> addMsgList) {
        AddMsgList = addMsgList;
    }
}
