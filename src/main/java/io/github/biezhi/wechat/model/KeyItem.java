package io.github.biezhi.wechat.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class KeyItem {

    @SerializedName("Key")
    private Integer key;

    @SerializedName("Val")
    private Integer val;
}
