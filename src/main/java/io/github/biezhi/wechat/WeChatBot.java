package io.github.biezhi.wechat;

import io.github.biezhi.wechat.api.WeChatApi;
import io.github.biezhi.wechat.api.WeChatApiImpl;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.client.BotClient;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.constant.Constant;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.*;
import io.github.biezhi.wechat.utils.DateUtils;
import io.github.biezhi.wechat.utils.OkHttpUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 微信机器人
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Slf4j
public class WeChatBot {

    /**
     * 操作微信接口的API
     */
    private WeChatApi api;

    /**
     * 调用HTTP请求的客户端
     */
    private BotClient botClient;

    /**
     * 微信API配置
     */
    private Config config;

    @Getter
    @Setter
    private boolean running;

    /**
     * 登录会话
     */
    @Setter
    private LoginSession session;

    /**
     * 最后一次正常检查时间戳
     */
    @Getter
    private long lastCheckTs;

    /**
     * 接收消息重试次数
     */
    @Getter
    private final int receiveRetryCount = 5;

    /**
     * 待处理的消息队列
     */
    @Getter
    private volatile BlockingQueue<WeChatMessage> messages = new LinkedBlockingQueue<>();

    /**
     * 注解绑定的函数映射
     */
    private final Map<MsgType, List<Invoke>> mapping = new HashMap<>(8);

    public WeChatBot(Builder builder) {
        this.config = builder.config;
        this.botClient = builder.botClient;
        this.session = new LoginSession();
        this.init();
    }

    public WeChatBot(Config config) {
        this(new Builder().config(config));
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

    public void addMessages(List<WeChatMessage> messages) {
        try {
            if (null == messages || messages.size() == 0) {
                return;
            }
            for (WeChatMessage message : messages) {
                this.messages.put(message);
            }
        } catch (InterruptedException e) {
            log.error("向队列添加 Message 出错", e);
        }
    }

    public boolean hasMessage() {
        return this.messages.size() > 0;
    }

    public WeChatMessage nextMessage() {
        try {
            return this.messages.take();
        } catch (InterruptedException e) {
            log.error("从队列获取 Message 出错", e);
            return null;
        }
    }

    private void init() {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            Bind bind = method.getAnnotation(Bind.class);
            if (null == bind) {
                continue;
            }
            MsgType[] msgTypes = bind.msgType();
            for (MsgType msgType : msgTypes) {
                List<Invoke> invokes = mapping.get(msgType);
                if (null == mapping.get(msgType)) {
                    invokes = new ArrayList<>();
                }
                invokes.add(new Invoke(method, Arrays.asList(bind.accountType())));
                log.info("绑定消息监听函数 [{}] - [{}]", method.getName(), msgType);
                mapping.put(msgType, invokes);
            }
        }
    }

    /**
     * 启动微信监听
     */
    public void start() {
        this.api = new WeChatApiImpl(this);
        log.info("wechat-bot: {}", Constant.VERSION);
        api.login(config.autoLogin());

        Thread msgHandle = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (hasMessage()) {
                        WeChatMessage weChatMessage = nextMessage();
                        callBack(mapping.get(MsgType.ALL), weChatMessage);
                        callBack(mapping.get(weChatMessage.getMsgType()), weChatMessage);
                    }
                }
            }
        });
        msgHandle.setName("message-handle");
        msgHandle.setDaemon(true);
        msgHandle.start();

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

    /**
     * 回调微信消息给客户端、存储器
     *
     * @param invokes 执行器
     * @param message 消息
     */
    private void callBack(List<Invoke> invokes, WeChatMessage message) {
        if (null != invokes && invokes.size() > 0 && null != message) {
            for (Invoke invoke : invokes) {
                invoke.call(this, message);
            }
        }
    }

    public boolean autoReply() {
        return config.autoReply();
    }

    public void updateLastCheck() {
        this.lastCheckTs = System.currentTimeMillis();
        if (this.config().autoLogin()) {
            String file = this.config().assetsDir() + "/login.json";
            WeChatUtils.writeJson(file, HotReload.build(this.session()));
            if (log.isDebugEnabled()) {
                log.debug("写入本地登录JSON");
            }
        }
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
