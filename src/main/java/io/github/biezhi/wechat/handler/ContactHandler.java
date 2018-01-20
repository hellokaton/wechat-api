package io.github.biezhi.wechat.handler;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.model.Member;
import io.github.biezhi.wechat.model.User;
import io.github.biezhi.wechat.request.JsonRequest;
import io.github.biezhi.wechat.response.JsonResponse;
import io.github.biezhi.wechat.utils.StringUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static io.github.biezhi.wechat.constant.Constant.API_SPECIAL_USER;

/**
 * 联系人处理
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Slf4j
public class ContactHandler {

    private WeChatBot bot;

    private List<User>   specialUsersList = new ArrayList<User>();
    private List<User>   publicUsersList  = new ArrayList<User>();
    private List<User>   contactList      = new ArrayList<User>();
    private List<Member> groupMemeberList = new ArrayList<Member>();
    private List<User>   groupList        = new ArrayList<User>();

    public ContactHandler(WeChatBot weChatBot) {
        this.bot = weChatBot;
    }

    public boolean loadContact() {
        log.info("开始获取联系人信息");

        int        seq = 0;
        List<User> memberList;
        while (true) {
            String url = String.format("%s/webwxgetcontact?r=%s&seq=%s&skey=%s",
                    bot.session().getUrl(), System.currentTimeMillis(),
                    seq, bot.session().getSKey());

            JsonResponse response = bot.execute(new JsonRequest(url).jsonBody());

            JsonObject jsonObject = response.toJsonObject();
            seq = jsonObject.get("Seq").getAsInt();
            memberList = WeChatUtils.fromJson(WeChatUtils.toJson(jsonObject.getAsJsonArray("MemberList")), new TypeToken<List<User>>() {});
            if (seq == 0) {
                break;
            }
        }

        this.contactList.addAll(memberList);
        Iterator<User> iterator = this.contactList.iterator();
        while (iterator.hasNext()) {
            User contact = iterator.next();
            if (contact.getVerifyFlag() != 0) {
                iterator.remove();
                publicUsersList.add(contact);
            } else if (API_SPECIAL_USER.contains(contact.getUserName())) {
                iterator.remove();
                specialUsersList.add(contact);
            } else if (contact.getUserName().contains("@@")) {
                iterator.remove();
                groupList.add(contact);
            } else if (bot.session().getUserName().equals(contact.getUserName())) {
                iterator.remove();
            }
        }
        return true;
    }


    public String getUserRemarkName(String id) {
        String name = id.contains("@@") ? "未知群" : "陌生人";
        if (id.equals(this.bot.session().getUserName())) {
            return this.bot.session().getNickName();
        }
        if (id.contains("@@")) {
            return this.getGroupName(id);
        }
        // 特殊账号
        for (User user : specialUsersList) {
            if (user.getUserName().equals(id)) {
                return StringUtils.isNotEmpty(user.getRemarkName()) ? user.getRemarkName() : user.getNickName();
            }
        }
        // 公众号或服务号
        for (User user : publicUsersList) {
            if (user.getUserName().equals(id)) {
                return StringUtils.isNotEmpty(user.getRemarkName()) ? user.getRemarkName() : user.getNickName();
            }
        }
        // 群友
        for (User user : contactList) {
            if (user.getUserName().equals(id)) {
                return StringUtils.isNotEmpty(user.getRemarkName()) ? user.getRemarkName() : user.getNickName();
            }
        }
        return name;
    }

    private String getGroupName(String id) {
        String name = "未知群";
        for (User user : groupList) {
            if (user.getUserName().equals(id)) {
                return user.getNickName();
            }
        }
        List<User> groupList = getNameById(id);
        for (User group : groupList) {
            this.groupList.add(group);
            if (group.getUserName().equals(id)) {
                name = group.getNickName();
                this.groupMemeberList.addAll(group.getMembers());
                return name;
            }
        }
        return name;
    }

    private List<User> getNameById(String id) {
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

        List<User> contactUser = WeChatUtils.fromJson(WeChatUtils.toJson(response.toJsonObject().getAsJsonObject("")),
                new TypeToken<List<User>>() {});

        return contactUser;
    }

}
