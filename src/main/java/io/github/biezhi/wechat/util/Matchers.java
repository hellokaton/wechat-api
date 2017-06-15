package io.github.biezhi.wechat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matchers {

    public static String match(String p, String str) {
        Pattern pattern = Pattern.compile(p);
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

}
