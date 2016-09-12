package me.biezhi.wechat.exception;

public class WechatException extends RuntimeException {
	
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
