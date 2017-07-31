package io.github.biezhi.wechat.model;

import io.github.biezhi.wechat.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 环境配置读取
 *
 * @author biezhi
 * 2017/6/1
 */
public class Environment {

    private static final Logger log = LoggerFactory.getLogger(Environment.class);

    private Properties props = new Properties();

    private Environment() {
    }

    public static Environment empty() {
        return new Environment();
    }

    /**
     * Properties to Environment
     *
     * @param props
     * @return
     */
    public static Environment of(Properties props) {
        Environment environment = new Environment();
        environment.props = props;
        return environment;
    }

    /**
     * Map to Environment
     *
     * @param map
     * @return
     */
    public static Environment of(Map<String, String> map) {
        Environment environment = new Environment();
        Set<String> keySet      = map.keySet();
        for (String key : keySet) {
            environment.props.setProperty(key, map.get(key));
        }
        return environment;
    }

    /**
     * load Environment by URL
     *
     * @param url
     * @return
     */
    public Environment of(URL url) {
        String location = url.getPath();
        try {
            location = URLDecoder.decode(location, "utf-8");
            return of(url.openStream(), location);
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return null;
    }

    /**
     * load Environment by file
     *
     * @param file
     * @return
     */
    public static Environment of(File file) {
        try {
            return of(Files.newInputStream(Paths.get(file.getPath())), file.getName());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * load Environment by location
     *
     * @param location
     * @return
     */
    public static Environment of(String location) {
        if (location.startsWith("classpath:")) {
            location = location.substring("classpath:".length());
            return loadClasspath(location);
        } else if (location.startsWith("file:")) {
            location = location.substring("file:".length());
            return new Environment().of(new File(location));
        } else if (location.startsWith("url:")) {
            location = location.substring("url:".length());
            try {
                return new Environment().of(new URL(location));
            } catch (MalformedURLException e) {
                log.error("", e);
                return null;
            }
        } else {
            return new Environment().loadClasspath(location);
        }
    }

    private static Environment loadClasspath(String classpath) {
        if (classpath.startsWith("/")) {
            classpath = classpath.substring(1);
        }
        InputStream is = getDefault().getResourceAsStream(classpath);
        if (null == is) {
            return new Environment();
        }
        return of(is, classpath);
    }

    private static Environment of(InputStream is, String location) {
        if (is == null) {
            log.warn("InputStream not found: " + location);
            return null;
        }
        try {
            Environment environment = new Environment();
            environment.props.load(is);
            return environment;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            Utils.closeQuietly(is);
        }
    }


    /**
     * Returns current thread's context class loader
     */
    public static ClassLoader getDefault() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
        }
        if (loader == null) {
            loader = Environment.class.getClassLoader();
            if (loader == null) {
                try {
                    // getClassLoader() returning null indicates the bootstrap ClassLoader
                    loader = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return loader;
    }

    public Environment set(String key, Object value) {
        props.put(key, value);
        return this;
    }

    public Environment add(String key, Object value) {
        props.put(key, value);
        return this;
    }

    public Environment addAll(Map<String, String> map) {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            this.props.setProperty(key, map.get(key));
        }
        return this;
    }

    public Environment addAll(Properties props) {
        Set<String> keySet = props.stringPropertyNames();
        for (String key : keySet) {
            this.props.setProperty(key, props.getProperty(key));
        }
        return this;
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public Object getObject(String key) {
        return props.get(key);
    }

    public Integer getInt(String key) {
        if (null != getObject(key)) {
            return Integer.valueOf(getObject(key).toString());
        }
        return null;
    }

    public Integer getInt(String key, int defaultValue) {
        if (null != getInt(key)) {
            return getInt(key);
        }
        return defaultValue;
    }

    public Long getLong(String key) {
        if (null != getObject(key)) {
            return Long.valueOf(getObject(key).toString());
        }
        return null;
    }

    public Long getLong(String key, long defaultValue) {
        if (null != getLong(key)) {
            return getLong(key);
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        if (null != getObject(key)) {
            return Boolean.valueOf(getObject(key).toString());
        }
        return null;
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        if (null != getBoolean(key)) {
            return getBoolean(key);
        }
        return defaultValue;
    }

    public Double getDouble(String key) {
        if (null != getObject(key)) {
            return Double.valueOf(getObject(key).toString());
        }
        return null;
    }

    public Double getDouble(String key, double defaultValue) {
        if (null != getDouble(key)) {
            return getDouble(key);
        }
        return defaultValue;
    }

    public Map<String, String> toMap() {
        Map<String, String> map    = new HashMap<String, String>(props.size());
        Set<String>         keySet = props.stringPropertyNames();
        for (String key : keySet) {
            map.put(key, props.getProperty(key));
        }
        return map;
    }

    public Properties props() {
        return props;
    }

}