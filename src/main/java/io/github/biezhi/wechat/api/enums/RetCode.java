package io.github.biezhi.wechat.api.enums;

/**
 * 返回码枚举
 *
 * @author biezhi
 * @date 2018/1/19
 */
public enum RetCode {

    SUCCESS(0, "请求成功"),
    INVALID_OPTION(-1006, "无效操作"),
    INVALID_PARAM(-1005, "参数错误"),
    SERVER_ERROR(-1004, "服务器返回异常值"),
    SERVER_REJECT_CONNECT(-1003, "服务器拒绝连接"),
    FILE_POSITION_ERROR(-1002, "文件位置错误"),
    NOT_FOUND_MEMBER(-1001, "无法找到对应的成员"),
    NOT_BASE_RESPONSE(-1000, "返回值不带BaseResponse");

    private int    code;
    private String msg;

    RetCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static RetCode parse(int code) {
        switch (code) {
            case 0:
                return RetCode.SUCCESS;
            case -1006:
                return RetCode.INVALID_OPTION;
            case -1005:
                return RetCode.INVALID_PARAM;
            case -1004:
                return RetCode.SERVER_ERROR;
            case -1003:
                return RetCode.SERVER_REJECT_CONNECT;
            case -1002:
                return RetCode.FILE_POSITION_ERROR;
            case -1001:
                return RetCode.NOT_FOUND_MEMBER;
            case -1000:
                return RetCode.NOT_BASE_RESPONSE;
            default:
                return RetCode.INVALID_OPTION;
        }
    }
}
