package io.github.biezhi.wechat.listener;

import io.github.biezhi.wechat.event.EventManager;
import io.github.biezhi.wechat.event.EventType;
import io.github.biezhi.wechat.model.entity.WechatMeta;
import io.github.biezhi.wechat.model.response.BaseResponse;
import io.github.biezhi.wechat.service.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WechatListener {

    private static final Logger log = LoggerFactory.getLogger(WechatListener.class);

    int playWeChat = 0;

    private EventManager eventManager;

    public WechatListener(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void start(final WechatService wechatService, final WechatMeta wechatMeta) {
        eventManager.addEventListener(EventType.OFFLINE, new OfflineListener());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("进入消息监听模式 ...");
                    wechatService.choiceSyncLine(wechatMeta);
                    while (true) {
                        int[] arr = wechatService.syncCheck(wechatMeta);
                        log.info("retcode={}, selector={}", arr[0], arr[1]);

                        if (arr[0] == 1100) {
                            eventManager.fireEvent(EventType.OFFLINE);
                            break;
                        }

                        if (arr[0] == 0) {
                            if (arr[1] == 2) {
                                BaseResponse data = wechatService.webwxsync(wechatMeta);
                                wechatService.handleMsg(wechatMeta, data.getAddMsgList());
                            } else if (arr[1] == 6) {
                                BaseResponse data = wechatService.webwxsync(wechatMeta);
                                wechatService.handleMsg(wechatMeta, data.getAddMsgList());
                            } else if (arr[1] == 7) {
                                playWeChat += 1;
                                log.info("你在手机上玩微信被我发现了 {} 次", playWeChat);
                                wechatService.webwxsync(wechatMeta);
                            } else if (arr[1] == 3) {
                                continue;
                            } else if (arr[1] == 0) {
                                continue;
                            }
                        } else {
                            //
                        }
                        try {
                            log.info("等待2000ms...");
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                }
            }
        }, "wechat-listener-thread").start();
    }

}
