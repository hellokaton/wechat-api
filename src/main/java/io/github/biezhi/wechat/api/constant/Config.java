package io.github.biezhi.wechat.api.constant;

import java.util.Properties;

import static io.github.biezhi.wechat.api.constant.Constant.*;

/**
 * 配置
 *
 * @author biezhi
 * @date 2018/1/18
 */
public class Config {

    private Properties props = new Properties();

    public static Config me() {
        return new Config();
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String assetsDir() {
        return props.getProperty(CONF_ASSETS_DIR, CONF_ASSETS_DIR_DEFAULT);
    }

    public Config assetsDir(String dir) {
        props.setProperty(CONF_ASSETS_DIR, dir);
        return this;
    }

    public boolean showTerminal() {
        return Boolean.valueOf(props.getProperty(CONF_PRINT_TERMINAL, CONF_PRINT_TERMINAL_DEFAULT));
    }

    public Config showTerminal(boolean show) {
        props.setProperty(CONF_PRINT_TERMINAL, String.valueOf(show));
        return this;
    }

    public boolean autoReply() {
        return Boolean.valueOf(props.getProperty(CONF_AUTO_REPLY, CONF_AUTO_REPLY_DEFAULT));
    }

    public Config autoReply(boolean flag) {
        props.setProperty(CONF_AUTO_REPLY, String.valueOf(flag));
        return this;
    }
}
