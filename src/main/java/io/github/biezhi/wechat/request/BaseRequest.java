package io.github.biezhi.wechat.request;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class BaseRequest {

    @SerializedName("Skey")
    private String skey;

    @SerializedName("Sid")
    private String sid;

    @SerializedName("Uin")
    private String uin;

    @SerializedName("DeviceID")
    private String deviceID;

}
