package io.github.biezhi.wechat.constant;

import java.util.Properties;

import static io.github.biezhi.wechat.constant.Constant.*;

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

    public String imgDir() {
        return props.getProperty(CONF_IMG_DIR, CONF_IMG_DIR_DEFAULT);
    }

    public boolean showTerminal() {
        return Boolean.valueOf(props.getProperty(CONF_PRINT_TERMINAL, CONF_PRINT_TERMINAL_DEFAULT));
    }

    public Config showTerminal(boolean show) {
        props.setProperty(CONF_PRINT_TERMINAL, String.valueOf(show));
        return this;
    }

}
