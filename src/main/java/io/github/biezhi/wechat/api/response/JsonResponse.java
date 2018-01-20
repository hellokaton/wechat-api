package io.github.biezhi.wechat.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * JSON 响应
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JsonResponse extends ApiResponse {

    @SerializedName("Ret")
    private Integer ret;

    @SerializedName("ErrMsg")
    private String msg;

    @SerializedName("BaseResponse")
    private BaseResponse baseResponse;

    @Expose
    private Map<String, Object> map;

    public Object get(String key) {
        if (null == map) {
            map = WeChatUtils.fromJson(rawBody, new TypeToken<Map<String, Object>>() {});
        }
        return map.get(key);
    }

    public String getString(String key) {
        return this.get(key).toString();
    }

    public boolean success() {
        return null != baseResponse && baseResponse.getRet().equals(0);
    }

}
