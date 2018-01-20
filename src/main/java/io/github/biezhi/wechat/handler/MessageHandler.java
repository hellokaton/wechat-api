package io.github.biezhi.wechat.handler;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.Message;
import io.github.biezhi.wechat.api.model.SendMessage;
import io.github.biezhi.wechat.api.model.User;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import io.github.biezhi.wechat.api.request.FileRequest;
import io.github.biezhi.wechat.api.request.JsonRequest;
import io.github.biezhi.wechat.api.response.FileResponse;
import io.github.biezhi.wechat.api.response.JsonResponse;
import io.github.biezhi.wechat.api.response.WebSyncResponse;
import io.github.biezhi.wechat.exception.WeChatException;
import io.github.biezhi.wechat.utils.StringUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
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
                Integer msgType  = message.getMsgType();
                String  name     = bot.getContactHandler().getUserRemarkName(message.getFromUserName());
                String  content  = message.getContent().replace("&lt;", "<").replace("&gt;", ">");
                String  msgId    = message.getId();
                User    fromUser = bot.getContactHandler().getUserById(message.getFromUserName());

                log.debug("收到消息JSON: {}", WeChatUtils.toJson(message));

                WeChatMessage.WeChatMessageBuilder weChatMessageBuilder = WeChatMessage.builder()
                        .raw(message)
                        .fromNickName(fromUser.getNickName())
                        .fromRemarkName(fromUser.getRemarkName())
                        .fromUserName(message.getFromUserName())
                        .text(content);

                List<Method> methods = mapping.get(MsgType.ALL);
                if (null != methods && methods.size() > 0) {
                    this.callBack(methods, weChatMessageBuilder.build());
                }

                switch (msgType) {
                    case 1:
                        if (bot.autoReply()) {
                            this.sendTextMsg("自动回复: " + content, message.getFromUserName());
                        } else {
                            methods = mapping.get(MsgType.TEXT);
                            if (null != methods && methods.size() > 0) {
                                this.callBack(methods, weChatMessageBuilder.build());
                            }
                        }
                        break;
                    // 聊天图片
                    case 3:
                        String imgPath = this.downloadMsgImg(msgId);
                        methods = mapping.get(MsgType.IMAGE);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, weChatMessageBuilder.imagePath(imgPath).build());
                        }
                        break;
                    // 语音
                    case 34:
                        String voicePath = this.donwloadVoice(msgId);
                        methods = mapping.get(MsgType.VOICE);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, weChatMessageBuilder.voicePath(voicePath).build());
                        }
                        break;
                    // 名片
                    case 42:
                        log.info("{} 发送了一张名片: ", name);
                        log.info("=========================");
                        log.info("= 昵称: {}", message.getRecommend().getNickName());
                        log.info("= 微信号: {}", message.getRecommend().getAlias());
                        log.info("= 地区: {}-{}", message.getRecommend().getProvince(), message.getRecommend().getCity());
                        log.info("= 性别: {}", message.getRecommend().getSex());
                        log.info("=========================");
                        methods = mapping.get(MsgType.PERSON_CARD);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, weChatMessageBuilder.build());
                        }
                        break;
                    // 视频
                    case 43:
                        String videoPath = this.downloadVideo(msgId);
                        methods = mapping.get(MsgType.VIDEO);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, weChatMessageBuilder.videoPath(videoPath).build());
                        }
                        break;
                    // 动画表情
                    case 47:
                        log.info("{} 发了一个动画表情");
                        imgPath = this.downloadMsgImg(message.getNewMsgId().toString());
                        methods = mapping.get(MsgType.IMAGE);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, weChatMessageBuilder.imagePath(imgPath).build());
                        }
                        break;
                    // 分享
                    case 49:
                        break;
                    // 联系人初始化
                    case 51:
                        break;
                    // 视频
                    case 62:
                        videoPath = this.downloadVideo(msgId);
                        methods = mapping.get(MsgType.VIDEO);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, weChatMessageBuilder.videoPath(videoPath).build());
                        }
                        break;
                    // 撤回消息
                    case 10002:
                        log.info("{} 撤回了一条消息: {}", name, content);
                        methods = mapping.get(MsgType.REVOKE);
                        if (null != methods && methods.size() > 0) {
                            this.callBack(methods, weChatMessageBuilder.build());
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

    /**
     * 回调微信消息给客户端、存储器
     *
     * @param methods
     * @param message
     */
    private void callBack(List<Method> methods, WeChatMessage message) {
        if(null != bot.storageMessage()){
            bot.storageMessage().save(message);
        }
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
    private String downloadMsgImg(String msgId) {
        String url = String.format("%s/webwxgetmsgimg?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id   = msgId + ".jpg";
        File   file = WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/images", id);
        return file.getPath();
    }

    /**
     * 下载表情到本地
     *
     * @param msgId
     */
    private String downloadIconImg(String msgId) {
        String url = String.format("%s/webwxgeticon?username=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".jpg";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/icons", id).getPath();
    }

    /**
     * 下载头像到本地
     *
     * @param userName
     */
    private String downloadHeadImg(String userName) {
        String       url         = String.format("%s/webwxgetheadimg?username=%s&skey=%s", bot.session().getUrl(), userName, bot.session().getSKey());
        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = userName + ".jpg";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/head", id).getPath();
    }

    /**
     * 下载视频到本地
     *
     * @param msgId
     */
    private String downloadVideo(String msgId) {
        String url = String.format("%s/webwxgetvideo?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".mp4";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/video", id).getPath();
    }

    /**
     * 下载音频到本地
     *
     * @param msgId
     */
    private String donwloadVoice(String msgId) {
        String url = String.format("%s/webwxgetvoice?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".mp3";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/video", id).getPath();
    }

    public void uploadMedia(){
        String url = "https://file2.wx.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";

    }

    public void sendImgMsg(){

    }

    /**
     * 发送文本消息
     *
     * @param msg
     * @param toUserName
     */
    public void sendTextMsg(String msg, String toUserName) {

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
