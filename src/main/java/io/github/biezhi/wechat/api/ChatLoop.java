package io.github.biezhi.wechat.api;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.model.SyncCheckRet;
import io.github.biezhi.wechat.api.response.WebSyncResponse;
import io.github.biezhi.wechat.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import static io.github.biezhi.wechat.api.enums.RetCode.*;

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
    private int retryCount = 0;

    ChatLoop(WeChatBot bot) {
        this.bot = bot;
        this.api = bot.api();
    }

    @Override
    public void run() {
        while (bot.isRunning()) {
            try {
                SyncCheckRet syncCheckRet = api.syncCheck();
                if (syncCheckRet.getRetCode() == UNKNOWN) {
                    log.info("未知状态");
                    continue;
                } else if (syncCheckRet.getRetCode() == MOBILE_LOGIN_OUT) {
                    log.info("你在手机上登出了微信，再见");
                    api.logout();
                    break;
                } else if (syncCheckRet.getRetCode() == LOGIN_OTHERWISE) {
                    log.info("你在其他地方登录了 WEB 版微信，再见");
                    api.logout();
                    break;
                } else if (syncCheckRet.getRetCode() == NORMAL) {
                    // 更新最后一次正常检查时间
                    bot.updateLastCheck();
                    WebSyncResponse webSyncResponse = api.webSync();
                    switch (syncCheckRet.getSelector()) {
                        case 2:
                            if (null == webSyncResponse) {
                                break;
                            }
                            bot.addMessages(api.handleMsg(webSyncResponse.getAddMessageList()));
                            break;
                        case 6:
                            log.info("收到疑似红包消息");
                            break;
                        default:
                            break;
                    }
                }
                if (System.currentTimeMillis() - bot.getLastCheckTs() <= 30) {
                    DateUtils.sleep(System.currentTimeMillis() - bot.getLastCheckTs());
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
                retryCount += 1;
                if (bot.getReceiveRetryCount() < retryCount) {
                    bot.setRunning(false);
                } else {
                    DateUtils.sleep(1000);
                }
            }
        }
    }
}
