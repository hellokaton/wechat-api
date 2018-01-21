package io.github.biezhi.wechat.api.request;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信基础请求参数
 *
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class BaseRequest implements Serializable {

    @SerializedName("Skey")
    private String skey;

    @SerializedName("Sid")
    private String sid;

    @SerializedName("Uin")
    private String uin;

    @SerializedName("DeviceID")
    private String deviceID;

}
