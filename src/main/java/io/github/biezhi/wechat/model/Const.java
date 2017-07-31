package io.github.biezhi.wechat.model;

import io.github.biezhi.wechat.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 常量信息
 */
public interface Const {

    String[] SERVER_UPLOAD_ALLOWED_EXTENSIONS = {"txt", "pdf", "png", "jpg", "jpeg", "gif"};

    String RUN_RESULT_SUCCESS          = "成功 %ds\n";
    String RUN_RESULT_FAIL             = "失败\n[*] 退出程序\n";
    String MAIN_RESTART                = "[*] wait for restart";
    String LOG_MSG_FILE                = "WeChat-Msgs-%Y-%m-%d.json";
    String LOG_MSG_GROUP_LIST_FILE     = "group_list.json";
    String LOG_MSG_QUIT                = "\n[*] Force quit.\n";
    String LOG_MSG_FAIL                = "失败\n";
    String LOG_MSG_SUCCESS             = "成功\n";
    String LOG_MSG_START               = "[*] 微信网页版 ... 开动\n";
    String LOG_MSG_RECOVER             = "[*] 从配置文件中恢复 ... ";
    String LOG_MSG_RECOVER_CONTACT     = "[*] 从文件中恢复联系人数据 ... ";
    String LOG_MSG_TRY_INIT            = "[*] 尝试初始化 ... ";
    String LOG_MSG_ASSOCIATION_LOGIN   = "[*] 通过关联登录 ... ";
    String LOG_MSG_GET_UUID            = "[*] 正在获取 uuid ... ";
    String LOG_MSG_GET_QRCODE          = "[*] 正在获取二维码 ... 成功\n";
    String LOG_MSG_SCAN_QRCODE         = "[*] 请使用微信扫描二维码以登录 ... \n";
    String LOG_MSG_CONFIRM_LOGIN       = "[*] 请在手机上点击确认以登录 ... \n";
    String LOG_MSG_WAIT_LOGIN_ERR1     = "[登陆超时] \n";
    String LOG_MSG_WAIT_LOGIN_ERR2     = "[登陆异常] \n";
    String LOG_MSG_LOGIN               = "[*] 正在登录 ... ";
    String LOG_MSG_INIT                = "[*] 微信初始化 ... ";
    String LOG_MSG_STATUS_NOTIFY       = "[*] 开启状态通知 ... ";
    String LOG_MSG_GET_CONTACT         = "[*] 获取联系人 ... ";
    String LOG_MSG_CONTACT_COUNT       = "[*] 应有 [{}] 个联系人，读取到联系人 [{}] 个\n";
    String LOG_MSG_OTHER_CONTACT_COUNT = "[*] 共有 [{}] 个群 | [{}] 个直接联系人 | [{}] 个特殊账号 ｜ [{}] 个公众号/服务号\n";
    String LOG_MSG_GET_GROUP_MEMBER    = "[*] 拉取群聊成员 ... ";
    String LOG_MSG_SNAPSHOT            = "[*] 保存配置 ... ";
    String LOG_MSG_LOGOUT              = "[*] 你在手机上登出了微信\n";
    String LOG_MSG_LOGIN_OTHERWHERE    = "[*] 你在其他地方登录了 WEB 版微信\n";
    String LOG_MSG_QUIT_ON_PHONE       = "[*] 你在手机上主动退出了\n";
    String LOG_MSG_RUNTIME             = "[*] Total run: %s\n";
    String LOG_MSG_KILL_PROCESS        = "kill %d";
    String LOG_MSG_NEW_MSG             = ">>> [{}] 条新消息\n";
    String LOG_MSG_LOCATION            = "[位置] %s";
    String LOG_MSG_PICTURE             = "[图片] %s";
    String LOG_MSG_VOICE               = "[语音] %s";
    String LOG_MSG_RECALL              = "撤回了一条消息";
    String LOG_MSG_ADD_FRIEND          = "%s 请求添加你为好友";
    String LOG_MSG_UNKNOWN_MSG         = "[*] 该消息类型为: %d，内容: %s";
    String LOG_MSG_VIDEO               = "[小视频] %s";
    String LOG_MSG_NOTIFY_PHONE        = "[*] 提示手机网页版微信登录状态\n";
    String LOG_MSG_EMOTION             = "[表情] %s";
    String LOG_MSG_NAME_CARD           =
            "[名片]\n" +
                    "=========================\n" +
                    "= 昵称: %s\n" +
                    "= 微信号: %s\n" +
                    "= 地区: %s %s\n" +
                    "= 性别: %s\n" +
                    "=========================";

    String[] LOG_MSG_SEX_OPTION = {"未知", "男", "女"};

    String LOG_MSG_APP_LINK =
            "[%s]\n" +
                    "=========================\n" +
                    "= 标题: %s\n" +
                    "= 描述: %s\n" +
                    "= 链接: %s\n" +
                    "= 来自: %s\n" +
                    "=========================";

    Map<String, Object> LOG_MSG_APP_LINK_TYPE =
            Utils.createMap("5", "链接", "3", "音乐", "7", "微博");

    String LOG_MSG_APP_IMG =
            "[图片]\n" +
                    "=========================\n" +
                    "= 文件: %s\n" +
                    "= 来自: %s\n" +
                    "=========================";

    String LOG_MSG_SYSTEM             = "系统消息";
    String LOG_MSG_UNKNOWN_NAME       = "未知_";
    String LOG_MSG_UNKNOWN_GROUP_NAME = "未知群_";


    String[] API_USER_AGENT = {"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3)",
            "AppleWebKit/537.36 (KHTML, like Gecko)",
            "Chrome/48.0.2564.109 Safari/537.36"};

    // 特殊用户 须过滤
    Set<String> API_SPECIAL_USER = new HashSet<String>(Arrays.asList("newsapp", "filehelper", "weibo", "qqmail",
            "fmessage", "tmessage", "qmessage", "qqsync",
            "floatbottle", "lbsapp", "shakeapp", "medianote",
            "qqfriend", "readerapp", "blogapp", "facebookapp",
            "masssendapp", "meishiapp", "feedsapp", "voip",
            "blogappweixin", "brandsessionholder", "weixin",
            "weixinreminder", "officialaccounts", "wxitil",
            "notification_messages", "wxid_novlwrv3lqwv11",
            "gh_22b87fa7cb3c", "userexperience_alarm"));

    String[] EMOTICON = {"[Smile]", "[Grimace]", "[Drool]", "[Scowl]", "[CoolGuy]", "[Sob]", "[Shy]",
            "[Silent]", "[Sleep]", "[Cry]", "[Awkward]", "[Angry]", "[Tongue]", "[Grin]",
            "[Surprise]", "[Frown]", "[Ruthless]", "[Blush]", "[Scream]", "[Puke]",
            "[Chuckle]", "[Joyful]", "[Slight]", "[Smug]", "[Hungry]", "[Drowsy]", "[Panic]",
            "[Sweat]", "[Laugh]", "[Commando]", "[Determined]", "[Scold]", "[Shocked]", "[Shhh]",
            "[Dizzy]", "[Tormented]", "[Toasted]", "[Skull]", "[Hammer]", "[Wave]",
            "[Relief]", "[DigNose]", "[Clap]", "[Shame]", "[Trick]", " [Bah！L]", "[Bah！R]",
            "[Yawn]", "[Lookdown]", "[Wronged]", "[Puling]", "[Sly]", "[Kiss]", "[Uh-oh]",
            "[Whimper]", "[Cleaver]", "[Melon]", "[Beer]", "[Basketball]", "[PingPong]",
            "[Coffee]", "[Rice]", "[Pig]", "[Rose]", "[Wilt]", "[Lip]", "[Heart]",
            "[BrokenHeart]", "[Cake]", "[Lightning]", "[Bomb]", "[Dagger]", "[Soccer]", "[Ladybug]",
            "[Poop]", "[Moon]", "[Sun]", "[Gift]", "[Hug]", "[Strong]",
            "[Weak]", "[Shake]", "[Victory]", "[Admire]", "[Beckon]", "[Fist]", "[Pinky]",
            "[Love]", "[No]", "[OK]", "[InLove]", "[Blowkiss]", "[Waddle]", "[Tremble]",
            "[Aaagh!]", "[Twirl]", "[Kotow]", "[Lookback]", "[Jump]", "[Give-in]"};

}