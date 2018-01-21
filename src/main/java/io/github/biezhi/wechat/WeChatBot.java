package io.github.biezhi.wechat;

import io.github.biezhi.wechat.api.StorageMessage;
import io.github.biezhi.wechat.api.WeChatApi;
import io.github.biezhi.wechat.api.WeChatApiImpl;
import io.github.biezhi.wechat.api.client.BotClient;
import io.github.biezhi.wechat.api.client.Callback;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.constant.Constant;
import io.github.biezhi.wechat.api.model.LoginSession;
import io.github.biezhi.wechat.api.request.ApiRequest;
import io.github.biezhi.wechat.api.response.ApiResponse;
import io.github.biezhi.wechat.utils.DateUtils;
import io.github.biezhi.wechat.utils.OkHttpUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.Scanner;

/**
 * 微信机器人
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Slf4j
public class WeChatBot {

    private WeChatApi      api;
    private BotClient      botClient;
    private Config         config;
    @Setter
    private LoginSession   session;
    private StorageMessage storageMessage;

    @Getter
    @Setter
    private boolean running;

    public WeChatBot(Builder builder) {
        this.config = builder.config;
        this.botClient = builder.botClient;
        this.session = new LoginSession();
    }

    public WeChatBot(Config config) {
        this(new Builder().config(config));
    }

    public <T extends ApiRequest, R extends ApiResponse> R execute(ApiRequest<T, R> request) {
        return botClient.send(request);
    }

    public <T extends ApiRequest, R extends ApiResponse> R download(ApiRequest<T, R> request) {
        return botClient.download(request);
    }

    public <T extends ApiRequest<T, R>, R extends ApiResponse> void execute(T request, Callback<T, R> callback) {
        botClient.send(request, callback);
    }

    public Config config() {
        return this.config;
    }

    public LoginSession session() {
        return session;
    }

    public BotClient client() {
        return botClient;
    }

    public WeChatApi api() {
        return this.api;
    }

    /**
     * 启动微信监听
     */
    public void start() {
        this.api = new WeChatApiImpl(this);
        log.info("wechat-bot: {}", Constant.VERSION);
        api.login(config.autoLogin());
        while (true) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                String text = scanner.next();
                if ("quit".equals(text) || "exit".equals(text)) {
                    api.logout();
                    break;
                }
            }
            DateUtils.sleep(100);
        }
    }

    public StorageMessage storageMessage() {
        return storageMessage;
    }

    public boolean autoReply() {
        return config.autoReply();
    }

    public static final class Builder {

        private Config config = Config.me();
        private BotClient    botClient;
        private OkHttpClient okHttpClient;

        public Builder() {
            botClient = new BotClient(client(null));
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
                botClient = new BotClient(okHttpClient);
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

    }

}
