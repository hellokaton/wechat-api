package io.github.biezhi.wechat.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * KeyItem
 *
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
