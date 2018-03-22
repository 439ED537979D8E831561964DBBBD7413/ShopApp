package com.yj.shopapp.wbeen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghao on 2016/10/30.
 */

public class Adver {

    /**
     * title : bsnsns
     * imgurl : http://u.19diandian.com/Public/uploads/20160922/bb709e943990c779e76a2d08af6f4d20.jpg
     * tag : bsbsb
     */

    private String title;
    private List<String>imgurls=new ArrayList<>();
    private String tag;
    private String id;
    public  Adver(String json)
    {
        try {
            JSONObject jsonObject=new JSONObject(json);
            if (jsonObject.has("title"))
            {
                title=jsonObject.optString("title");
            }
            if (jsonObject.has("imgurl"))
            {
                JSONArray jsonArray=jsonObject.getJSONArray("imgurl");
                for (int i=0;i<jsonArray.length();i++)
                {
                    imgurls.add(jsonArray.getString(i));
                }
            }
            if (jsonObject.has("id"))
            {
                id=jsonObject.optString("id");
            }
            if (jsonObject.has("tag"))
            {
                tag=jsonObject.optString("tag");
            }
        }
        catch (JSONException e)
        {

        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImgurls() {
        return imgurls;
    }

    public void setImgurls(List<String> imgurls) {
        this.imgurls = imgurls;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
