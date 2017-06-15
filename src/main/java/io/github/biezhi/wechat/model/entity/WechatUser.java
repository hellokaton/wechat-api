package io.github.biezhi.wechat.model.entity;

import java.io.Serializable;

/**
 * @author biezhi
 *         15/06/2017
 */
public class WechatUser implements Serializable {

    private String UserName;
    private String RemarkName;
    private String NickName;
    private Integer VerifyFlag;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Integer getVerifyFlag() {
        return VerifyFlag;
    }

    public void setVerifyFlag(Integer verifyFlag) {
        VerifyFlag = verifyFlag;
    }

    public String getRemarkName() {
        return RemarkName;
    }

    public void setRemarkName(String remarkName) {
        RemarkName = remarkName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }
}
