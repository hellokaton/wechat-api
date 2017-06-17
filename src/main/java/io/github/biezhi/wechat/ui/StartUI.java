package io.github.biezhi.wechat.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.biezhi.wechat.model.Const;
import io.github.biezhi.wechat.handle.MessageHandle;
import io.github.biezhi.wechat.api.WechatApi;
import io.github.biezhi.wechat.model.WechatMessage;
import io.github.biezhi.wechat.model.Environment;
import io.github.biezhi.wechat.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author biezhi
 *         17/06/2017
 */
public class StartUI extends WechatApi {

    private static final Logger log = LoggerFactory.getLogger(StartUI.class);

    private MessageHandle messageHandle;

    private QRCodeFrame qrCodeFrame;

    public StartUI(Environment environment) {
        super(environment);
    }

    public void setMsgHandle(MessageHandle messageHandle) {
        this.messageHandle = messageHandle;
    }

    public void start() {
        log.info(Const.LOG_MSG_START);
        log.info(Const.LOG_MSG_TRY_INIT);

        if (webwxinit()) {
            log.info(Const.LOG_MSG_SUCCESS);
        } else {
            while (true) {
                log.info(Const.LOG_MSG_GET_UUID);
                getUUID();
                log.info(Const.LOG_MSG_GET_QRCODE);
                final String qrCodePath = genqrcode();
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                            qrCodeFrame = new QRCodeFrame(qrCodePath);
                        } catch (Exception e) {
                            log.error("显示二维码失败", e);
                        }
                    }
                });
                log.info(Const.LOG_MSG_SCAN_QRCODE);
                if (!waitforlogin(1)) {
                    continue;
                }
                log.info(Const.LOG_MSG_CONFIRM_LOGIN);
                if (!waitforlogin(0)) {
                    continue;
                }
                qrCodeFrame.dispose();
                break;
            }
            log.info(Const.LOG_MSG_LOGIN);
            if (!login()) {
                log.warn("登录失败");
            }
            log.info(Const.LOG_MSG_INIT);
            if (!webwxinit()) {
                log.warn("初始化失败");
            }
            log.info(Const.LOG_MSG_STATUS_NOTIFY);
            if (!openStatusNotify()) {
                log.warn("状态通知打开失败");
            }
            log.info(Const.LOG_MSG_GET_CONTACT);
            if (!getContact()) {
                log.warn("获取联系人失败");
            }
            log.info(Const.LOG_MSG_CONTACT_COUNT, memberCount, memberList.size());
            log.info(Const.LOG_MSG_OTHER_CONTACT_COUNT, groupList.size(), contactList.size(), specialUsersList.size(), publicUsersList.size());

            if (groupList.size() > 0) {
                log.info(Const.LOG_MSG_GET_GROUP_MEMBER);
                fetch_group_contacts();
            }

            log.info(Const.LOG_MSG_SNAPSHOT);
            snapshot();

            while (true) {
                // retcode, selector
                int[] checkResponse = synccheck();
                int retcode = checkResponse[0];
                int selector = checkResponse[1];
                log.debug("retcode: {}, selector: {}", retcode, selector);

                switch (retcode) {
                    case 1100:
                        log.warn(Const.LOG_MSG_LOGOUT);
                        break;
                    case 1101:
                        log.warn(Const.LOG_MSG_LOGIN_OTHERWHERE);
                        break;
                    case 1102:
                        log.warn(Const.LOG_MSG_QUIT_ON_PHONE);
                        break;
                    case 0:
                        handle(selector);
                        break;
                    default:
                        JsonObject dic = webwxsync();
                        log.debug("webwxsync: {}\n", dic.toString());
                        break;

                }
            }
        }

    }

    private void handle_mod(JsonObject dic) {
        log.debug("handle modify");
        handle_msg(dic);

    }

    public void handle_msg(JsonObject dic) {
        log.debug("handle message");
        if (null != messageHandle) {
            messageHandle.wxSync(dic);
        }

        int n = dic.getAsJsonArray("AddMsgList").size();
        if (n == 0) {
            return;
        }

        log.debug(Const.LOG_MSG_NEW_MSG, n);

        JsonArray msgs = dic.getAsJsonArray("AddMsgList");
        for (JsonElement element : msgs) {
            JsonObject msg = element.getAsJsonObject();

            String msgType = msg.get("MsgType").getAsString();
            String msgId = msg.get("MsgId").getAsString();
            String content = msg.get("Content").getAsString().replace("&lt;", "<").replace("&gt;", ">");
            WechatMessage wechatMessage = new WechatMessage();
            wechatMessage.setRawMsg(msg);

            // 文本
            if (conf.get("MSGTYPE_TEXT").equals(msgType)) {
                // 地理位置消息
                if (content.contains("pictype=location")) {
                    String location = content.split("<br/>")[1];
                    wechatMessage.setLocation(location);
                    wechatMessage.setLog(String.format(Const.LOG_MSG_LOCATION, location));
                } else {
                    // 普通文本
                    String text = content.split(":<br/>")[0];
                    wechatMessage.setText(text);
                    wechatMessage.setLog(text.replace("<br/>", "\n"));
                }
            }

            boolean isGroupMsg = (msg.get("FromUserName").getAsString() + msg.get("ToUserName").getAsString()).contains("@@");
            if (isGroupMsg) {

            } else {
                if (null != messageHandle) {
                    messageHandle.userMessage(wechatMessage);
                }
            }
            this.show_msg(wechatMessage);
        }
    }

    private void show_msg(WechatMessage wechatMessage) {

        Map<String, String> src = null;
        Map<String, String> dst = null;
        Map<String, String> group = null;
        JsonObject msg = wechatMessage.getRawMsg();

        String content = msg.get("Content").getAsString();
        content = content.replace("&lt;", "<").replace("&gt;", ">");

        String msg_id = msg.get("MsgId").getAsString();

        // 接收到来自群的消息
        if (msg.get("FromUserName").getAsString().substring(2).equals("@@")) {
            String groupId = msg.get("FromUserName").getAsString();
//                group = self.get_group_by_id(g_id)
        } else {
            // 非群聊消息
            src = this.get_user_by_id(msg.get("FromUserName").getAsString());
            dst = this.get_user_by_id(msg.get("ToUserName").getAsString());
        }
        if (null != group) {

        } else {
            log.info("{} {} -> {}: {}\n", msg_id, src.get("ShowName"),
                    dst.get("ShowName"), wechatMessage.getLog());
        }
    }

    private void handle(int selector) {
        switch (selector) {
            case 2:
                JsonObject dic = webwxsync();
                if (null != dic) {
                    handle_msg(dic);
                }
                break;
            case 7:
                webwxsync();
                break;
            case 0:
                Utils.sleep(1000);
                break;
            case 4:
                // 保存群聊到通讯录
                // 修改群名称
                // 新增或删除联系人
                // 群聊成员数目变化
                dic = webwxsync();
                if (null != dic) {
                    handle_mod(dic);
                }
                break;
            default:
                break;
        }
    }

}
