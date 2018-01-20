package io.github.biezhi.wechat.handler;

import io.github.biezhi.wechat.WeChatBot;
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
import java.util.List;

/**
 * 消息处理
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Slf4j
public class MessageHandler {

    private final WeChatBot bot;

    public MessageHandler(WeChatBot bot) {this.bot = bot;}

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

                switch (msgType) {
                    case 1:
                        if (bot.autoReply()) {
                            this.sendMsg("自动回复: " + content, message.getFromUserName());
                        } else {
                            // 回调
                        }
                        break;
                    // 聊天图片
                    case 3:
                        this.downloadMsgImg(msgId);
                        break;
                    // 语音
                    case 34:
                        this.donwloadVoice(msgId);
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
                        break;
                    // 视频
                    case 43:
                        this.downloadVideo(msgId);
                        break;
                    // 动画表情
                    case 47:
                        log.info("{} 发了一个动画表情");
//                        this.downloadIconImg(msgId);
                        this.downloadMsgImg(message.getNewMsgId().toString());
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
                        break;
                    // 撤回消息
                    case 10002:
                        log.info("{} 撤回了一条消息: {}", name, content);
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
     * 下载图片到本地
     *
     * @param msgId
     */
    private void downloadMsgImg(String msgId) {
        String url = String.format("%s/webwxgetmsgimg?msgid=%s&skey=%s", bot.loginSession().getUrl(), msgId, bot.loginSession().getSKey());

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
        String url = String.format("%s/webwxgeticon?username=%s&skey=%s", bot.loginSession().getUrl(), msgId, bot.loginSession().getSKey());

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
        String       url         = String.format("%s/webwxgetheadimg?username=%s&skey=%s", bot.loginSession().getUrl(), userName, bot.loginSession().getSKey());
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
        String url = String.format("%s/webwxgetvideo?msgid=%s&skey=%s", bot.loginSession().getUrl(), msgId, bot.loginSession().getSKey());

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
        String url = String.format("%s/webwxgetvoice?msgid=%s&skey=%s", bot.loginSession().getUrl(), msgId, bot.loginSession().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        String id = msgId + ".mp3";
        WeChatUtils.saveFile(inputStream, bot.config().assetsDir() + "/video", id);
    }

    private void sendMsg(String msg, String toUserName) {
        String url = String.format("%s/webwxsendmsg?pass_ticket=%s", bot.loginSession().getUrl(), bot.loginSession().getPassTicket());

        String clientMsgId = System.currentTimeMillis() / 1000 + StringUtils.random(6);

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.loginSession().getBaseRequest())
                .add("Msg", new SendMessage(1, msg,
                        bot.loginSession().getUserName(), toUserName,
                        clientMsgId, clientMsgId))
        );

    }
}
