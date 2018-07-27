package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2018/5/17.
 *
 * @author LK
 */
public class IntegralRule {

    /**
     * status : 1
     * rule : 积分月度返点规则:
     1.5000以上返点3%;
     2.10000以上返点4%;
     3.20000以上返点5%;
     4.每个季度(三个月)返点1%.
     * title : 如何成为VIP
     * contents : 请联系客服81722253
     * vip_title : VIP规则
     * vip_contents : 请联系客服81722253
     */

    private int status;
    private String rule;
    private String title;
    private String contents;
    private String vip_title;
    private String vip_contents;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getVip_title() {
        return vip_title;
    }

    public void setVip_title(String vip_title) {
        this.vip_title = vip_title;
    }

    public String getVip_contents() {
        return vip_contents;
    }

    public void setVip_contents(String vip_contents) {
        this.vip_contents = vip_contents;
    }
}
