package io.github.biezhi.wechat.api;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.model.SyncCheckRet;
import io.github.biezhi.wechat.api.response.WebSyncResponse;
import io.github.biezhi.wechat.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 轮训监听消息
 *
 * @author biezhi
 * @date 2018/1/21
 */
@Slf4j
public class ChatLoop implements Runnable {

    private WeChatBot bot;
    private WeChatApi api;

    ChatLoop(WeChatBot bot) {
        this.bot = bot;
        this.api = bot.api();
    }

    @Override
    public void run() {
        while (bot.isRunning()) {
            long         lastCheckTs  = System.currentTimeMillis();
            SyncCheckRet syncCheckRet = api.syncCheck();

            if (syncCheckRet.getCode() == 1100) {
                log.info("你在手机上登出了微信，债见");
                break;
            }
            if (syncCheckRet.getCode() == 1101) {
                log.info("你在其他地方登录了 WEB 版微信，债见");
                break;
            }
            if (syncCheckRet.getCode() == 0) {
                switch (syncCheckRet.getSelector()) {
                    case 2:
                        WebSyncResponse webSyncResponse = api.webSync();
                        if (null != webSyncResponse) {
                            api.handleMsg(webSyncResponse);
                        }
                        break;
                    case 6:
                        log.info("收到疑似红包消息");
                        break;
                    case 7:
                        log.info("你在手机上玩微信被我发现了");
                        break;
                    default:
                        DateUtils.sleep(100);
                        break;
                }
            }
            if (System.currentTimeMillis() - lastCheckTs <= 20) {
                DateUtils.sleep(System.currentTimeMillis() - lastCheckTs);
            }
        }
    }
}
