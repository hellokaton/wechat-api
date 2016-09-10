package me.biezhi.wechat;

/**
 * 
 */
public class Application {
	
	public static void main(String[] args) {
		try {
			WechatRobot wechatRobot = new WechatRobot();
			wechatRobot.showQrCode();
			while(!wechatRobot.waitForLogin().equals("200")){
				Thread.sleep(2000);
			}
			wechatRobot.closeQrWindow();
			wechatRobot.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}