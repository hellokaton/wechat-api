package io.github.biezhi.wechat.handler;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.model.Account;
import io.github.biezhi.wechat.api.request.JsonRequest;
import io.github.biezhi.wechat.api.response.JsonResponse;
import io.github.biezhi.wechat.utils.StringUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 联系人处理
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Slf4j
@Getter
public class ContactHandler {

    private WeChatBot bot;

    private Map<String, Account> accountMap = new HashMap<String, Account>();
    private int memberCount;

    /**
     * 特殊账号
     */
    private List<Account> specialUsersList;
    /**
     * 公众号、服务号
     */
    private List<Account> publicUsersList;

    /**
     * 好友
     */
    private List<Account> contactList;
    /**
     * 群
     */
    private List<Account> groupList;

    public ContactHandler(WeChatBot weChatBot) {
        this.bot = weChatBot;
    }

    /**
     * 加载联系人信息
     *
     * @return
     */
    public void loadContact(int seq) {
        log.info("开始获取联系人信息");
        while (true) {
            String url = String.format("%s/webwxgetcontact?r=%s&seq=%s&skey=%s",
                    bot.session().getUrl(), System.currentTimeMillis(),
                    seq, bot.session().getSKey());

            JsonResponse response = bot.execute(new JsonRequest(url).jsonBody());

            JsonObject jsonObject = response.toJsonObject();
            seq = jsonObject.get("Seq").getAsInt();

            this.memberCount += jsonObject.get("MemberCount").getAsInt();
            List<Account> memberList = WeChatUtils.fromJson(WeChatUtils.toJson(jsonObject.getAsJsonArray("MemberList")), new TypeToken<List<Account>>() {});

            for (Account account : memberList) {
                accountMap.put(account.getUserName(), account);
            }
            // 查看seq是否为0，0表示好友列表已全部获取完毕，若大于0，则表示好友列表未获取完毕，当前的字节数（断点续传）
            if (seq == 0) {
                break;
            }
        }

        this.contactList = new ArrayList<Account>(this.getAccountByType(AccountType.TYPE_FRIEND));
        this.publicUsersList = new ArrayList<Account>(this.getAccountByType(AccountType.TYPE_MP));
        this.specialUsersList = new ArrayList<Account>(this.getAccountByType(AccountType.TYPE_SPECIAL));
        this.groupList = new ArrayList<Account>(this.getAccountByType(AccountType.TYPE_GROUP));
    }

    /**
     * 加载群信息
     */
    public void loadGroupList() {

        log.info("加载群聊信息");

        // 群账号
        List<Map<String, String>> list = new ArrayList<Map<String, String>>(groupList.size());

        for (Account account : groupList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("UserName", account.getUserName());
            map.put("EncryChatRoomId", "");
            list.add(map);
        }

        String url = String.format("%s/webwxbatchgetcontact?type=ex&r=%s&pass_ticket=%s",
                bot.session().getUrl(), System.currentTimeMillis() / 1000, bot.session().getPassTicket());

        // 加载群信息
        JsonResponse jsonResponse = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Count", groupList.size())
                .add("List", list)
        );

        List<Account> groups = WeChatUtils.fromJson(WeChatUtils.toJson(jsonResponse.toJsonObject().getAsJsonArray("ContactList")), new TypeToken<List<Account>>() {});

    }

    public Account getUserById(String id) {
        if (id.equals(this.bot.session().getUserName())) {
            return this.bot.session().getAccount();
        }

        // 特殊账号
        for (Account account : specialUsersList) {
            if (account.getUserName().equals(id)) {
                return account;
            }
        }
        // 公众号或服务号
        for (Account account : publicUsersList) {
            if (account.getUserName().equals(id)) {
                return account;
            }
        }
        // 联系人
        for (Account account : contactList) {
            if (account.getUserName().equals(id)) {
                return account;
            }
        }
        return null;
    }

    public String getUserRemarkName(String id) {
        String name = id.contains("@@") ? "未知群" : "陌生人";
        if (id.equals(this.bot.session().getUserName())) {
            return this.bot.session().getNickName();
        }
        Account account = accountMap.get(id);
        if (null == account) {
            return name;
        }
        String nickName = StringUtils.isNotEmpty(account.getRemarkName()) ? account.getRemarkName() : account.getNickName();
        return StringUtils.isNotEmpty(nickName) ? nickName : name;
    }

    private List<Account> getNameById(String id) {
        String url = String.format("%s/webwxbatchgetcontact?type=ex&r=%s&pass_ticket=%s",
                bot.session().getUrl(), System.currentTimeMillis() / 1000, bot.session().getPassTicket());

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String>       map  = new HashMap<String, String>();
        map.put("UserName", id);
        map.put("EncryChatRoomId", id);
        list.add(map);

        JsonResponse response = bot.execute(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Count", bot.session().getBaseRequest())
                .add("List", list));

        List<Account> contactAccount = WeChatUtils.fromJson(WeChatUtils.toJson(response.toJsonObject().getAsJsonObject("")),
                new TypeToken<List<Account>>() {});

        return contactAccount;
    }

    /**
     * 根据账号类型筛选
     *
     * @param accountType
     * @return
     */
    public Set<Account> getAccountByType(AccountType accountType) {
        Set<Account> accountSet = new HashSet<Account>();
        for (Account account : accountMap.values()) {
            if (account.getAccountType().equals(accountType)) {
                accountSet.add(account);
            }
        }
        return accountSet;
    }

}
