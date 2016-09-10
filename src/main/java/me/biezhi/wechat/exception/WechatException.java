package me.biezhi.wechat.exception;

public class WechatException extends RuntimeException {
	
	private static final long serialVersionUID = 209248116271894410L;

	public WechatException() {
		super();
	}

	public WechatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WechatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WechatException(String message) {
		super(message);
	}

	public WechatException(Throwable cause) {
		super(cause);
	}

}
