package io.github.biezhi.wechat.model;

import io.github.biezhi.wechat.enums.RetCode;
import lombok.Data;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class ReturnValue {

    private RetCode retCode;

    public ReturnValue() {

    }

}
