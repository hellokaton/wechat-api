package io.github.biezhi.wechat;

import com.google.gson.Gson;
import io.github.biezhi.wechat.api.WeChatBotClient;
import io.github.biezhi.wechat.api.Callback;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.constant.Constant;
import io.github.biezhi.wechat.handler.ContactHandler;
import io.github.biezhi.wechat.handler.GroupHandler;
import io.github.biezhi.wechat.handler.LoginHandler;
import io.github.biezhi.wechat.handler.MessageHandler;
import io.github.biezhi.wechat.api.model.LoginSession;
import io.github.biezhi.wechat.api.request.ApiRequest;
import io.github.biezhi.wechat.api.response.ApiResponse;
import io.github.biezhi.wechat.storage.StorageMessage;
import io.github.biezhi.wechat.utils.OkHttpUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 微信机器人
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Slf4j
public class WeChatBot {

    @Getter
    private LoginHandler   loginHandler;
    @Getter
    private ContactHandler contactHandler;
    @Getter
    private MessageHandler messageHandler;
    @Getter
    private GroupHandler   groupHandler;

    private WeChatBotClient api;
    private Config          config;
    private LoginSession    session;
    private StorageMessage  storageMessage;

    @Getter
    @Setter
    private boolean running;

    public WeChatBot(Builder builder) {
        this.config = builder.config;
        this.api = builder.api;
        this.session = new LoginSession();
    }

    public WeChatBot(Config config) {
        this(new Builder().config(config));
    }

    public <T extends ApiRequest, R extends ApiResponse> R execute(ApiRequest<T, R> request) {
        return api.send(request);
    }

    public <T extends ApiRequest, R extends ApiResponse> R download(ApiRequest<T, R> request) {
        return api.download(request);
    }

    public <T extends ApiRequest<T, R>, R extends ApiResponse> void execute(T request, Callback<T, R> callback) {
        api.send(request, callback);
    }

    public Config config() {
        return this.config;
    }

    public LoginSession session() {
        return session;
    }

    public WeChatBotClient api() {
        return api;
    }

    /**
     * 启动微信监听
     */
    public void start() {
        loginHandler = new LoginHandler(this);
        contactHandler = new ContactHandler(this);
        messageHandler = new MessageHandler(this);
        log.info("wechat-api: {}", Constant.VERSION);
        loginHandler.login();
    }

    public StorageMessage storageMessage() {
        return storageMessage;
    }

    public boolean autoReply() {
        return config.autoReply();
    }

    /**
     * 发送文本消息
     *
     * @param fromUserName 发给谁
     * @param msg          消息内容
     */
    public void sendText(String fromUserName, String msg) {
        messageHandler.sendTextMsg(msg, fromUserName);
    }

    public static final class Builder {

        private Config config = Config.me();
        private WeChatBotClient api;

        private OkHttpClient okHttpClient;

        public Builder() {
            api = new WeChatBotClient(client(null), gson());
        }

        public Builder debug() {
            okHttpClient = client(httpLoggingInterceptor());
            return this;
        }

        public Builder okHttpClient(OkHttpClient client) {
            okHttpClient = client;
            return this;
        }

        public Builder config(Config config) {
            this.config = config;
            return this;
        }

        public WeChatBot build() {
            if (okHttpClient != null) {
                OkHttpClient client = okHttpClient;
                api = new WeChatBotClient(client, gson());
            }
            return new WeChatBot(this);
        }

        private static OkHttpClient client(Interceptor interceptor) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            OkHttpUtils.configureToIgnoreCertificate(builder);
            if (interceptor != null) {
                builder.addInterceptor(interceptor);
            }
            return builder.build();
        }

        private static Interceptor httpLoggingInterceptor() {
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        private static Gson gson() {
            return new Gson();
        }

    }

}
