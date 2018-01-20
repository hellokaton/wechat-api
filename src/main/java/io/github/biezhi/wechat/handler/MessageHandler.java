package io.github.biezhi.wechat.handler;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.Message;
import io.github.biezhi.wechat.api.model.SendMessage;
import io.github.biezhi.wechat.api.model.Account;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import io.github.biezhi.wechat.api.request.FileRequest;
import io.github.biezhi.wechat.api.request.JsonRequest;
import io.github.biezhi.wechat.api.request.StringRequest;
import io.github.biezhi.wechat.api.response.ApiResponse;
import io.github.biezhi.wechat.api.response.FileResponse;
import io.github.biezhi.wechat.api.response.MediaResponse;
import io.github.biezhi.wechat.api.response.WebSyncResponse;
import io.github.biezhi.wechat.exception.WeChatException;
import io.github.biezhi.wechat.utils.MD5Checksum;
import io.github.biezhi.wechat.utils.StringUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private final Map<MsgType, List<Invoke>> mapping = new ConcurrentHashMap<MsgType, List<Invoke>>();

    public MessageHandler(WeChatBot bot) {
        this.bot = bot;
        Method[] methods = bot.getClass().getMethods();
        for (Method method : methods) {
            Bind bind = method.getAnnotation(Bind.class);
            if (null != bind) {
                MsgType[] msgTypes = bind.msgType();
                for (MsgType msgType : msgTypes) {
                    List<Invoke> invokes = mapping.get(msgType);
                    if (null == mapping.get(msgType)) {
                        invokes = new ArrayList<Invoke>();
                    }
                    invokes.add(new Invoke(method, Arrays.asList(bind.accountType())));
                    log.info("绑定函数 [{}] - [{}]", method.getName(), msgType);
                    mapping.put(msgType, invokes);
                }
            }
        }
    }

    public void handleMsg(WebSyncResponse webSyncResponse) {
        List<Message> addMessageList = webSyncResponse.getAddMessageList();
        if (null != addMessageList && addMessageList.size() > 0) {
            log.info("你有新的消息");
            for (Message message : addMessageList) {
                Integer msgType     = message.getMsgType();
                String  name        = bot.getContactHandler().getUserRemarkName(message.getFromUserName());
                String  content     = message.getContent().replace("&lt;", "<").replace("&gt;", ">");
                String  msgId       = message.getId();
                Account fromAccount = bot.getContactHandler().getUserById(message.getFromUserName());
                if (null == fromAccount) {
                    log.warn("未知消息类型: {}", WeChatUtils.toJson(message));
                    return;
                }

                log.debug("收到消息JSON: {}", WeChatUtils.toJson(message));

                WeChatMessage.WeChatMessageBuilder weChatMessageBuilder = WeChatMessage.builder()
                        .raw(message)
                        .fromNickName(fromAccount.getNickName())
                        .fromRemarkName(fromAccount.getRemarkName())
                        .fromUserName(message.getFromUserName())
                        .text(content);

                List<Invoke> invokes = mapping.get(MsgType.ALL);
                if (null != invokes && invokes.size() > 0) {
                    this.callBack(invokes, weChatMessageBuilder.build());
                }

                switch (msgType) {
                    case 1:
                        if (bot.autoReply()) {
                            this.sendTextMsg("自动回复: " + content, message.getFromUserName());
                        } else {
                            invokes = mapping.get(MsgType.TEXT);
                            if (null != invokes && invokes.size() > 0) {
                                this.callBack(invokes, weChatMessageBuilder.build());
                            }
                        }
                        break;
                    // 聊天图片
                    case 3:
                        String imgPath = this.downloadMsgImg(msgId);
                        invokes = mapping.get(MsgType.IMAGE);
                        if (null != invokes && invokes.size() > 0) {
                            this.callBack(invokes, weChatMessageBuilder.imagePath(imgPath).build());
                        }
                        break;
                    // 语音
                    case 34:
                        String voicePath = this.downloadVoice(msgId);
                        invokes = mapping.get(MsgType.VOICE);
                        if (null != invokes && invokes.size() > 0) {
                            this.callBack(invokes, weChatMessageBuilder.voicePath(voicePath).build());
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
                        invokes = mapping.get(MsgType.PERSON_CARD);
                        if (null != invokes && invokes.size() > 0) {
                            this.callBack(invokes, weChatMessageBuilder.build());
                        }
                        break;
                    // 视频
                    case 43:
                        String videoPath = this.downloadVideo(msgId);
                        invokes = mapping.get(MsgType.VIDEO);
                        if (null != invokes && invokes.size() > 0) {
                            this.callBack(invokes, weChatMessageBuilder.videoPath(videoPath).build());
                        }
                        break;
                    // 动画表情
                    case 47:
                        log.info("{} 发了一个动画表情");
                        imgPath = this.downloadMsgImg(message.getNewMsgId().toString());
                        invokes = mapping.get(MsgType.IMAGE);
                        if (null != invokes && invokes.size() > 0) {
                            this.callBack(invokes, weChatMessageBuilder.imagePath(imgPath).build());
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
                        invokes = mapping.get(MsgType.VIDEO);
                        if (null != invokes && invokes.size() > 0) {
                            this.callBack(invokes, weChatMessageBuilder.videoPath(videoPath).build());
                        }
                        break;
                    // 撤回消息
                    case 10002:
                        log.info("{} 撤回了一条消息: {}", name, content);
                        invokes = mapping.get(MsgType.REVOKE);
                        if (null != invokes && invokes.size() > 0) {
                            this.callBack(invokes, weChatMessageBuilder.build());
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
     * @param invokes
     * @param message
     */
    private void callBack(List<Invoke> invokes, WeChatMessage message) {
        if (null != bot.storageMessage()) {
            bot.storageMessage().save(message);
        }
        for (Invoke invoke : invokes) {
            invoke.call(bot, message);
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
    private String downloadVoice(String msgId) {
        String url = String.format("%s/webwxgetvoice?msgid=%s&skey=%s", bot.session().getUrl(), msgId, bot.session().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".mp3";
        return WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/video", id).getPath();
    }

    /**
     * 上传附件
     *
     * @param toUser
     * @param filePath
     * @return
     */
    public MediaResponse uploadMedia(String toUser, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new WeChatException("文件[" + filePath + "]不存在");
        }

        String url           = String.format("%s/webwxuploadmedia?f=json", bot.session().getFileUrl());
        String clientMediaId = System.currentTimeMillis() / 1000 + StringUtils.random(6);
        String mimeType      = "image/jpeg";

        long size = file.length();

        Map<String, Object> uploadMediaRequest = new HashMap<String, Object>();
        uploadMediaRequest.put("UploadType", 2);
        uploadMediaRequest.put("BaseRequest", bot.session().getBaseRequest());
        uploadMediaRequest.put("ClientMediaId", clientMediaId);
        uploadMediaRequest.put("TotalLen", size);
        uploadMediaRequest.put("StartPos", 0);
        uploadMediaRequest.put("DataLen", size);
        uploadMediaRequest.put("MediaType", 4);
        uploadMediaRequest.put("FromUserName", bot.session().getUserName());
        uploadMediaRequest.put("ToUserName", toUser);
        uploadMediaRequest.put("FileMd5", MD5Checksum.getMD5Checksum(file.getPath()));

        String dataTicket = bot.api().cookie("webwx_data_ticket");
        if (StringUtils.isEmpty(dataTicket)) {
            throw new WeChatException("缺少了附件Cookie");
        }

        String      lastModifieDate = new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(new Date());
        RequestBody filePart        = RequestBody.create(MediaType.parse("image/jpeg"), file);

        ApiResponse response = bot.execute(new StringRequest(url).post().multipart()
                .fileName(file.getName())
                .add("id", "WU_FILE_0")
                .add("name", filePath)
                .add("type", mimeType)
                .add("lastModifieDate", lastModifieDate)
                .add("size", String.valueOf(size))
                .add("mediatype", mimeType)
                .add("uploadmediarequest", WeChatUtils.toJson(uploadMediaRequest))
                .add("webwx_data_ticket", dataTicket)
                .add("pass_ticket", bot.session().getPassTicket())
                .add("filename", filePart));

        MediaResponse mediaResponse = response.parse(MediaResponse.class);
        if (!mediaResponse.success()) {
            log.warn("上传附件失败: {}", mediaResponse.getMsg());
        }
        return mediaResponse;
    }

    public void sendImgMsg(String toUserName, String filePath) {

        String mediaId = this.uploadMedia(toUserName, filePath).getMediaId();
        if (StringUtils.isEmpty(mediaId)) {
            log.warn("Media为空");
            return;
        }

        String url = String.format("%s/webwxsendmsgimg?fun=async&f=json&pass_ticket=%s",
                bot.session().getUrl(), bot.session().getPassTicket());

        String clientMsgId = System.currentTimeMillis() / 1000 + StringUtils.random(6);

        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("Type", 3);
        msg.put("MediaId", mediaId);
        msg.put("FromUserName", bot.session().getUserName());
        msg.put("ToUserName", toUserName);
        msg.put("LocalID", clientMsgId);
        msg.put("ClientMsgId", clientMsgId);

        bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Msg", msg)
        );
    }

    /**
     * 发送文本消息
     *
     * @param msg
     * @param toUserName
     */
    public void sendTextMsg(String toUserName, String msg) {

        String url = String.format("%s/webwxsendmsg?pass_ticket=%s", bot.session().getUrl(), bot.session().getPassTicket());

        String clientMsgId = System.currentTimeMillis() / 1000 + StringUtils.random(6);

        bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Msg", new SendMessage(1, msg,
                        bot.session().getUserName(), toUserName,
                        clientMsgId, clientMsgId))
        );

    }
}
