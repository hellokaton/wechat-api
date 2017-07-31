package io.github.biezhi.wechat.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.biezhi.wechat.Utils;
import io.github.biezhi.wechat.api.WechatApi;
import io.github.biezhi.wechat.handle.MessageHandle;
import io.github.biezhi.wechat.model.Const;
import io.github.biezhi.wechat.model.Environment;
import io.github.biezhi.wechat.model.GroupMessage;
import io.github.biezhi.wechat.model.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author biezhi
 * 17/06/2017
 */
public class StartUI extends WechatApi {

    private static final Logger log = LoggerFactory.getLogger(StartUI.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    private MessageHandle messageHandle;

    private QRCodeFrame qrCodeFrame;

    public StartUI(Environment environment) {
        super(environment);
    }

    public void setMsgHandle(MessageHandle messageHandle) {
        this.messageHandle = messageHandle;
    }

    private void waitForLogin() {
        while (true) {
            log.info(Const.LOG_MSG_GET_UUID);
            getUUID();
            log.info(Const.LOG_MSG_GET_QRCODE);
            final String qrCodePath = genqrcode();
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        if (null != qrCodeFrame) qrCodeFrame.dispose();
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
            break;
        }
        qrCodeFrame.setVisible(false);
        qrCodeFrame.dispose();
    }

    /**
     * 启动机器人
     */
    public void start() {
        log.info(Const.LOG_MSG_START);
        log.info(Const.LOG_MSG_TRY_INIT);

        if (webwxinit()) {
            log.info(Const.LOG_MSG_SUCCESS);
        } else {
            waitForLogin();
            log.info(Const.LOG_MSG_LOGIN);
            if (!login()) {
                log.warn("登录失败");
            }
            log.info(Const.LOG_MSG_INIT);
            if (!webwxinit()) {
                log.warn("初始化失败");
            }
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
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    log.info(Const.LOG_MSG_GET_GROUP_MEMBER);
                    StartUI.super.fetchGroupContacts();
                }
            });
        }
        log.info(Const.LOG_MSG_SNAPSHOT);
        super.snapshot();
        this.listen();
    }

    private void listen() {
        while (true) {
            // retcode, selector
            int[] checkResponse = synccheck();
            int   retcode       = checkResponse[0];
            int   selector      = checkResponse[1];
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
                    this.handle(selector);
                    break;
                default:
                    log.debug("wxSync: {}\n", wxSync().toString());
                    break;
            }
        }
    }

    private void handle_mod(JsonObject dic) {
        log.debug("handle modify");
        handle_msg(dic);

        JsonArray modContactList = dic.getAsJsonArray("ModContactList");
        for (JsonElement element : modContactList) {
            JsonObject m = element.getAsJsonObject();
            if (m.get("UserName").getAsString().startsWith("@@")) {
                boolean in_list = false;
                String  g_id    = m.get("UserName").getAsString();
                for (JsonElement ge : groupList) {
                    JsonObject group = ge.getAsJsonObject();
                    if (g_id.equals(group.get("UserName").getAsString())) {
                        in_list = true;
                        group.addProperty("MemberCount", m.get("MemberCount").getAsInt());
                        group.addProperty("NickName", m.get("NickName").getAsInt());
                        this.groupMemeberList.put(g_id, m.get("MemberList").getAsJsonArray());
                        if (null != messageHandle) {
                            messageHandle.groupMemberChange(g_id, m.get("MemberList").getAsJsonArray());
                        }
                        break;
                    }
                }
                if (!in_list) {
                    this.groupList.add(m);
                    this.groupMemeberList.put(g_id, m.get("MemberList").getAsJsonArray());
                    if (null != messageHandle) {
                        messageHandle.groupListChange(g_id, m.get("MemberList").getAsJsonArray());
                        messageHandle.groupMemberChange(g_id, m.get("MemberList").getAsJsonArray());
                    }
                }
            } else if (m.get("UserName").getAsString().equals("@")) {
                boolean in_list = false;
                for (JsonElement ue : memberList) {
                    JsonObject u    = ue.getAsJsonObject();
                    String     u_id = m.get("UserName").getAsString();
                    if (u_id.equals(u.get("UserName").getAsString())) {
                        u = m;
                        in_list = true;
                        break;
                    }
                }
                if (!in_list) {
                    this.memberList.add(m);
                }
            }
        }
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

            String      msgType     = msg.get("MsgType").getAsString();
            String      msgId       = msg.get("MsgId").getAsString();
            String      content     = msg.get("Content").getAsString().replace("&lt;", "<").replace("&gt;", ">");
            UserMessage userMessage = new UserMessage(this);
            userMessage.setRawMsg(msg);

            // 文本groupMessage
            if (conf.get("MSGTYPE_TEXT").equals(msgType)) {
                // 地理位置消息
                if (content.contains("pictype=location")) {
                    String location = content.split("<br/>")[1];
                    userMessage.setLocation(location);
                    userMessage.setLog(String.format(Const.LOG_MSG_LOCATION, location));
                } else {
                    // 普通文本
                    String text = null;
                    if (content.contains(":<br/>")) {
                        text = content.split(":<br/>")[1];
                    } else {
                        text = content;
                    }
                    userMessage.setText(text);
                    userMessage.setLog(text.replace("<br/>", "\n"));
                }
            } else if (conf.get("MSGTYPE_STATUSNOTIFY").equals(msgType)) {
                log.info(Const.LOG_MSG_NOTIFY_PHONE);
                return;
            }

            this.show_msg(userMessage);

            boolean isGroupMsg = (msg.get("FromUserName").getAsString() + msg.get("ToUserName").getAsString()).contains("@@");
            if (isGroupMsg) {
                GroupMessage groupMessage = make_group_msg(userMessage);
                if (null != messageHandle) {
                    messageHandle.groupMessage(groupMessage);
                }
            } else {
                if (null != messageHandle) {
                    messageHandle.userMessage(userMessage);
                }
            }
        }
    }

    private GroupMessage make_group_msg(UserMessage userMessage) {
        log.debug("make group message");
        GroupMessage groupMessage = new GroupMessage(this);
        groupMessage.setRawMsg(userMessage.getRawMsg());
        groupMessage.setMsgId(userMessage.getRawMsg().get("MsgId").getAsString());
        groupMessage.setFromUserName(userMessage.getRawMsg().get("FromUserName").getAsString());
        groupMessage.setToUserName(userMessage.getRawMsg().get("ToUserName").getAsString());
        groupMessage.setMsgType(userMessage.getRawMsg().get("MsgType").getAsString());
        groupMessage.setText(userMessage.getText());

        String content = userMessage.getRawMsg().get("Content").getAsString().replace("&lt;", "<")
                .replace("&gt;", ">");

        Map<String, String> group = null, src = null;

        if (groupMessage.getFromUserName().startsWith("@@")) {
            //接收到来自群的消息
            String g_id = groupMessage.getFromUserName();
            groupMessage.setGroupId(g_id);
            group = this.getGroupById(g_id);
            if (content.contains(":<br/>")) {
                String u_id = content.split(":<br/>")[0];
                src = getGroupUserById(u_id, g_id);
            }
        } else if (groupMessage.getToUserName().startsWith("@@")) {
            // 自己发给群的消息
            String g_id = groupMessage.getToUserName();
            groupMessage.setGroupId(g_id);
            String u_id = groupMessage.getFromUserName();
            src = this.getGroupUserById(u_id, g_id);
            group = this.getGroupById(g_id);
        }

        if (null != src) {
            groupMessage.setUser_attrstatus(src.get("AttrStatus"));
            groupMessage.setUser_display_name(src.get("DisplayName"));
            groupMessage.setUser_nickname(src.get("NickName"));
        }
        if (null != group) {
            groupMessage.setGroup_count(group.get("MemberCount"));
            groupMessage.setGroup_owner_uin(group.get("OwnerUin"));
            groupMessage.setGroup_name(group.get("ShowName"));
        }
        groupMessage.setTimestamp(userMessage.getRawMsg().get("CreateTime").getAsString());

        return groupMessage;
    }

    private void show_msg(UserMessage userMessage) {

        Map<String, Object> src   = null;
        Map<String, Object> dst   = null;
        Map<String, String> group = null;
        JsonObject          msg   = userMessage.getRawMsg();

        String content = msg.get("Content").getAsString();
        content = content.replace("&lt;", "<").replace("&gt;", ">");

        String msg_id = msg.get("MsgId").getAsString();

        // 接收到来自群的消息
        if (msg.get("FromUserName").getAsString().substring(2).equals("@@")) {
            String groupId = msg.get("FromUserName").getAsString();
            group = this.getGroupById(groupId);
            if (content.contains(":<br/>")) {
                String u_id = content.split(":<br/>")[0];
                src = new HashMap<String, Object>(this.getGroupUserById(u_id, groupId));
                dst = Utils.createMap("ShowName", "GROUP");
            } else {
                String u_id = msg.get("ToUserName").getAsString();
                src = new HashMap<String, Object>(Utils.createMap("ShowName", "SYSTEM"));
                dst = new HashMap<String, Object>(getGroupUserById(u_id, groupId));
            }
        } else {
            // 非群聊消息
            src = new HashMap<String, Object>(this.getUserById(msg.get("FromUserName").getAsString()));
            dst = new HashMap<String, Object>(this.getUserById(msg.get("ToUserName").getAsString()));
        }
        if (null != group) {
            log.info("{} |{}| {} -> {}: {}\n", msg_id, group.get("ShowName"),
                    dst.get("ShowName"), userMessage.getLog());
        } else {
            log.info("{} {} -> {}: {}\n", msg_id, src.get("ShowName"),
                    dst.get("ShowName"), userMessage.getLog());
        }
    }

    private void handle(int selector) {
        switch (selector) {
            case 2:
                JsonObject dic = wxSync();
                if (null != dic) {
                    handle_msg(dic);
                }
                break;
            case 7:
                wxSync();
                break;
            case 0:
                Utils.sleep(1000);
                break;
            case 4:
                // 保存群聊到通讯录
                // 修改群名称
                // 新增或删除联系人
                // 群聊成员数目变化
                dic = wxSync();
                if (null != dic) {
                    handle_mod(dic);
                }
                break;
            default:
                break;
        }
    }

}
