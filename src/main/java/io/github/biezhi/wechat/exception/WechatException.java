package io.github.biezhi.wechat.exception;

/**
 * 微信异常
 */
public class WechatException extends Exception {

    private static final long serialVersionUID = 209248116271894410L;

    public WechatException() {
        super();
    }

    public WechatException(String message) {
        super(message);
    }

    public WechatException(Throwable cause) {
        super(cause);
    }

}
