package io.github.biezhi.wechat.listener;

import io.github.biezhi.wechat.event.Event;
import io.github.biezhi.wechat.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author biezhi
 *         15/06/2017
 */
public class OfflineListener implements EventListener {

    private static final Logger log = LoggerFactory.getLogger(OfflineListener.class);

    @Override
    public void handleEvent(Event e) {
        log.info("你在手机上登出了微信，债见");
    }

}
