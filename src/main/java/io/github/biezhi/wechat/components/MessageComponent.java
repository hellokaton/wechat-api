package io.github.biezhi.wechat.components;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.model.Message;
import io.github.biezhi.wechat.model.SendMessage;
import io.github.biezhi.wechat.request.FileRequest;
import io.github.biezhi.wechat.request.JsonRequest;
import io.github.biezhi.wechat.request.StringRequest;
import io.github.biezhi.wechat.response.ApiResponse;
import io.github.biezhi.wechat.response.FileResponse;
import io.github.biezhi.wechat.response.JsonResponse;
import io.github.biezhi.wechat.response.WebSyncResponse;
import io.github.biezhi.wechat.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/1/20
 */
@Slf4j
public class MessageComponent {

    private final WeChatBot bot;

    public MessageComponent(WeChatBot bot) {this.bot = bot;}

    public void handleMsg(WebSyncResponse webSyncResponse) {
        List<Message> addMessageList = webSyncResponse.getAddMessageList();
        if (null != addMessageList && addMessageList.size() > 0) {
            log.info("你有新的消息");
            for (Message message : addMessageList) {
                Integer msgType = message.getMsgType();
                String  name    = bot.getContactComponent().getUserRemarkName(message.getFromUserName());
                String  content = message.getContent().replace("&lt;", "<").replace("&gt;", ">");
                String  msgId   = message.getId();

                switch (msgType) {
                    case 1:
                        if (bot.autoReply()) {
                            this.sendMsg("自动回复: " + content, message.getFromUserName());
                        }
                        break;
                    case 3:
                        this.webwxGetMsgImg(msgId);
                        break;
                    case 47:
                        this.webwxGetMsgImg(msgId);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void webwxGetMsgImg(String msgId) {
        String url = String.format("%s/webwxgetmsgimg?MsgID=%s&skey=%s", bot.loginSession().getUrl(), msgId, bot.loginSession().getSKey());

        FileResponse response    = bot.download(new FileRequest(url));
        InputStream  inputStream = response.getInputStream();

        File imgFile = new File(bot.config().imgDir(), "img_" + msgId + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
            byte[] buffer = new byte[2048];
            int    len    = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }

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
