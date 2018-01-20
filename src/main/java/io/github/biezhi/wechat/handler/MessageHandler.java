package io.github.biezhi.wechat.handler;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.annotation.Bind;
import io.github.biezhi.wechat.enums.MsgType;
import io.github.biezhi.wechat.exception.WeChatException;
import io.github.biezhi.wechat.model.Message;
import io.github.biezhi.wechat.model.SendMessage;
import io.github.biezhi.wechat.request.FileRequest;
import io.github.biezhi.wechat.request.JsonRequest;
import io.github.biezhi.wechat.response.FileResponse;
import io.github.biezhi.wechat.response.JsonResponse;
import io.github.biezhi.wechat.response.WebSyncResponse;
import io.github.biezhi.wechat.utils.StringUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Slf4j
public class MessageHandler {

    private final WeChatBot bot;
    private final Map<MsgType, List<Method>> mapping = new ConcurrentHashMap<MsgType, List<Method>>();

    public MessageHandler(WeChatBot bot) {
        this.bot = bot;
        Method[] methods = bot.getClass().getMethods();
        for (Method method : methods) {
            Bind bind = method.getAnnotation(Bind.class);
            if (null != bind) {
                MsgType[] msgTypes = bind.msgType();
                for (MsgType msgType : msgTypes) {
                    List<Method> methodList = mapping.get(msgType);
                    if (null == mapping.get(msgType)) {
                        methodList = new ArrayList<Method>();
                    }
                    methodList.add(method);
                    log.info("绑定函数 [{}] - [{}]", method.getName(), msgType);
                    mapping.put(msgType, methodList);
                }
            }
        }
    }

    public void handleMsg(WebSyncResponse webSyncResponse) {
        List<Message> addMessageList = webSyncResponse.getAddMessageList();
        if (null != addMessageList && addMessageList.size() > 0) {
            log.info("你有新的消息");
            for (Message message : addMessageList) {
                Integer msgType = message.getMsgType();
                String  name    = bot.getContactHandler().getUserRemarkName(message.getFromUserName());
                String  content = message.getContent().replace("&lt;", "<").replace("&gt;", ">");
                String  msgId   = message.getId();

                log.info("Message: {}", WeChatUtils.toJson(message));

                List<Method> methods = mapping.get(MsgType.ALL);
                if (null != methods && methods.size() > 0) {
                    this.callBack(methods, message);
                }

                switch (msgType) {
                    case 1:
                        if (bot.autoReply()) {
                            this.sendMsg("自动回复: " + content, message.getFromUserName());
                        } else {
                            methods = mapping.get(MsgType.TEXT);
                            if (null != methods && methods.size() > 0) {
                                this.callBack(methods, message);
                            }
                        }
                        break;
                    // 聊天图片
                    case 3:
                        this.downloadMsgImg(msgId);
                        methods = mapping.get(MsgType.IMAGE);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, message);
                        }
                        break;
                    // 语音
                    case 34:
                        this.donwloadVoice(msgId);
                        methods = mapping.get(MsgType.VOICE);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, message);
                        }
                        break;
                    // 名片
                    case 42:
                        log.info("{} 发送了一张名片: ", name);
                        log.info("=========================");
                        log.info("= 昵称: {}", message.getRecommendInfo().getNickName());
                        log.info("= 微信号: {}", message.getRecommendInfo().getAlias());
                        log.info("= 地区: {}-{}", message.getRecommendInfo().getProvince(), message.getRecommendInfo().getCity());
                        log.info("= 性别: {}", message.getRecommendInfo().getSex());
                        log.info("=========================");
                        methods = mapping.get(MsgType.PERSON_CARD);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, message);
                        }
                        break;
                    // 视频
                    case 43:
                        this.downloadVideo(msgId);
                        methods = mapping.get(MsgType.VIDEO);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, message);
                        }
                        break;
                    // 动画表情
                    case 47:
                        log.info("{} 发了一个动画表情");
                        this.downloadMsgImg(message.getNewMsgId().toString());
                        methods = mapping.get(MsgType.IMAGE);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, message);
                        }
//                        String url = this.searchContent("cdnurl", content, "attr");
//                        WeChatUtils.localOpen(url);
                        break;
                    // 分享
                    case 49:
                        break;
                    // 联系人初始化
                    case 51:
                        break;
                    // 视频
                    case 62:
                        this.downloadVideo(msgId);
                        methods = mapping.get(MsgType.VIDEO);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, message);
                        }
                        break;
                    // 撤回消息
                    case 10002:
                        log.info("{} 撤回了一条消息: {}", name, content);
                        methods = mapping.get(MsgType.REVOKE);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, message);
                        }
                        break;
                    default:
                        log.info("该消息类型为: {}, 可能是表情，图片, 链接或红包: %s", msgType, WeChatUtils.toJson(message));
                        break;
                }
            }
        }
    }

    private String searchContent(String key, String content, String format) {
        String result = "未知";
        if ("attr".equals(format)) {
            String m = WeChatUtils.match(key + "\\s?=\\s?\"([^\"<]+)\"", content);
            if (StringUtils.isNotEmpty(m)) {
                return m;
            }
        }
        if ("xml".equals(format)) {
            String m = WeChatUtils.match(String.format("<%s>([^<]+)</%s>", key), content);
            if (StringUtils.isNotEmpty(m)) {
                return m;
            } else {
                m = WeChatUtils.match(String.format("<%s><\\!\\[CDATA\\[(.*?)\\]\\]></%s>", key), content);
                if (StringUtils.isNotEmpty(m)) {
                    return m;
                }
            }
        }
        return result;
    }

    private void callBack(List<Method> methods, Message message) {
        for (Method method : methods) {
            try {
                method.invoke(bot, message);
            } catch (Exception e) {
                throw new WeChatException(e);
            }
        }
    }

    /**
     * 下载图片到本地
     *
     * @param msgId
     */
    private void downloadMsgImg(String msgId) {
        String url = String.format("%s/webwxgetmsgimg?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".jpg";
        WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/images", id);
    }

    /**
     * 下载表情到本地
     *
     * @param msgId
     */
    private void downloadIconImg(String msgId) {
        String url = String.format("%s/webwxgeticon?username=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".jpg";
        WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/icons", id);
    }

    /**
     * 下载头像到本地
     *
     * @param userName
     */
    private void downloadHeadImg(String userName) {
        String       url         = String.format("%s/webwxgetheadimg?username=%s&skey=%s", bot.session().getUrl(), userName, bot.session().getSKey());
        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = userName + ".jpg";
        WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/head", id);
    }

    /**
     * 下载视频到本地
     *
     * @param msgId
     */
    private void downloadVideo(String msgId) {
        String url = String.format("%s/webwxgetvideo?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".mp4";
        WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/video", id);
    }

    /**
     * 下载音频到本地
     *
     * @param msgId
     */
    private void donwloadVoice(String msgId) {
        String url = String.format("%s/webwxgetvoice?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".mp3";
        WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/video", id);
    }

    private void sendMsg(String msg, String toUserName) {
        String url = String.format("%s/webwxsendmsg?pass_ticket=%s", bot.session().getUrl(), bot.session().getPassTicket());

        String clientMsgId = System.currentTimeMillis() / 1000 + StringUtils.random(6);

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Msg", new SendMessage(1, msg,
                        bot.session().getUserName(), toUserName,
                        clientMsgId, clientMsgId))
        );

    }
}
