package io.github.biezhi.wechat;

import com.google.gson.Gson;
import io.github.biezhi.wechat.api.WeChatBotClient;
import io.github.biezhi.wechat.callback.Callback;
import io.github.biezhi.wechat.handler.ContactHandler;
import io.github.biezhi.wechat.handler.LoginHandler;
import io.github.biezhi.wechat.handler.MessageHandler;
import io.github.biezhi.wechat.constant.Config;
import io.github.biezhi.wechat.model.LoginSession;
import io.github.biezhi.wechat.model.User;
import io.github.biezhi.wechat.request.ApiRequest;
import io.github.biezhi.wechat.response.ApiResponse;
import io.github.biezhi.wechat.utils.OkHttpUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.ArrayList;
import java.util.List;

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

    private Config          config;
    private LoginSession    loginSession;
    private WeChatBotClient api;
    @Getter
    @Setter
    private boolean         running;

    @Getter
    private List<User> memberList   = new ArrayList<User>();
    @Getter
    private List<User> chatRoomList = new ArrayList<User>();

    public WeChatBot(Builder builder) {
        this.config = builder.config;
        this.api = builder.api;
        this.loginSession = new LoginSession();
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

    public LoginSession loginSession() {
        return loginSession;
    }

    public WeChatBotClient api() {
        return api;
    }

    public void start() {
        loginHandler = new LoginHandler(this);
        contactHandler = new ContactHandler(this);
        messageHandler = new MessageHandler(this);
        loginHandler.login();
    }

    public boolean autoReply() {
        return true;
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
