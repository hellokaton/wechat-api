package io.github.biezhi.wechat.model.request;

import java.io.Serializable;

/**
 * @author biezhi
 *         15/06/2017
 */
public class LoginRequest implements Serializable {

    private String Uin;
    private String Sid;
    private String Skey;
    private String DeviceID;

    public String getUin() {
        return Uin;
    }

    public void setUin(String uin) {
        Uin = uin;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getSkey() {
        return Skey;
    }

    public void setSkey(String skey) {
        Skey = skey;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
