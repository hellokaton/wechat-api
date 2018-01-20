package io.github.biezhi.wechat.api.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 附件响应体
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MediaResponse extends JsonResponse {

    @SerializedName("MediaId")
    private String mediaId;

    @SerializedName("StartPos")
    private Integer startPos;

    @SerializedName("CDNThumbImgHeight")
    private Integer cdnThumbImgHeight;

    @SerializedName("CDNThumbImgWidth")
    private Integer cdnThumbImgWidth;

    @SerializedName("EncryFileName")
    private String encryFileName;

}
