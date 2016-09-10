package me.biezhi.wechat.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONObject;

import me.biezhi.wechat.WechatRobot;

public class WechatListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatListener.class);
	
	private ExecutorService pool = Executors.newFixedThreadPool(3);
	
	public void start(final WechatRobot wechatRobot){
		pool.execute(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("[*] 进入消息监听模式 ...");
				int playWeChat = 0;
				while(true){
					
					int[] arr = wechatRobot.getWechatService().syncCheck(wechatRobot.getWebpush_url(), wechatRobot.getWechatMeta());
					
					LOGGER.info("[*] retcode={},selector={}", arr[0], arr[1]);
					
					if(arr[0] == 1100){
//						LOGGER.info("[*] 你在手机上登出了微信，债见");
//						break;
						arr = wechatRobot.getWechatService().syncCheck(wechatRobot.getWebpush_url(), wechatRobot.getWechatMeta());
					}
					
					if(arr[0] == 0){
						if(arr[1] == 2){
							JSONObject data = wechatRobot.getWechatService().webwxsync(wechatRobot.getWechatMeta(), wechatRobot.getWechatRequest());
							wechatRobot.getWechatService().handleMsg(wechatRobot.getWechatMeta(), wechatRobot.getWechatRequest(), wechatRobot.getWechatContact(), data);
						} else if(arr[1] == 6){
							JSONObject data = wechatRobot.getWechatService().webwxsync(wechatRobot.getWechatMeta(), wechatRobot.getWechatRequest());
							wechatRobot.getWechatService().handleMsg(wechatRobot.getWechatMeta(), wechatRobot.getWechatRequest(), wechatRobot.getWechatContact(), data);
						} else if(arr[1] == 7){
							playWeChat += 1;
							LOGGER.info("[*] 你在手机上玩微信被我发现了 %d 次", playWeChat);
							wechatRobot.getWechatService().webwxsync(wechatRobot.getWechatMeta(), wechatRobot.getWechatRequest());
						} else if(arr[1] == 3){
						} else if(arr[1] == 0){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
}
