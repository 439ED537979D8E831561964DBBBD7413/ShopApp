package com.yj.shopapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON数据解析工具
 */
public class JsonHelper<T> {
    public static int getcode(String codestr) {
        int code = 0;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(codestr);
            if (jsonObject.has("errcode")) {
                code = jsonObject.getInt("errcode");
            }
        } catch (JSONException e) {

        }
        return code;
    }

    @SuppressLint("Assert")
    public static boolean isRequstOK(String json, Context context) {

        if (StringHelper.isEmpty(json)) {
            return true;
        }
        int statusCode = 0;
        try {
            if ("[".equals(json.replaceAll(" ", "").substring(0, 1))) {
                return true;
            }
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("errcode")) {
                statusCode = Integer.parseInt(jsonObject.getString("errcode"));
            }
        } catch (Exception ex) {
            statusCode = -1;
        }
        if (statusCode == 5) {
            PreferenceUtils.remove(context, Contants.Preference.UID);
            PreferenceUtils.remove(context, Contants.Preference.UTYPE);
            PreferenceUtils.remove(context, Contants.Preference.TOKEN);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return statusCode == 0;
    }

    public static int getRequstOK(String json) {

        if (StringHelper.isEmpty(json)) {
            return -1;
        }
        int statusCode = 0;
        try {
            if ("[".equals(json.replaceAll(" ", "").substring(0, 1))) {
                return 0;
            }
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("errcode")) {
                statusCode = Integer.parseInt(jsonObject.getString("errcode"));
            }
        } catch (Exception ex) {
            statusCode = -1;
        }
        return statusCode;
    }

    public static String errorNo(String json) {
        System.out.println("错误========================");
        System.out.println("json----------" + json);
        String errcode;
        try {
            JSONObject jsonObject = new JSONObject(json);

            errcode = jsonObject.getString("errcode");


        } catch (Exception ex) {
            errcode = "-1";
        }
        return errcode;
    }

    public static String errorMsg(String json) {
        String msg = "";
        try {
            JSONObject jsonObject = new JSONObject(json);

            int statusCode = Integer.parseInt(jsonObject.getString("errcode"));

            msg = Contants.Error.ERRORARRAY[statusCode];

        } catch (Exception ex) {
            //msg = "网络异常";
        }
        return msg;
    }

    private static volatile JsonHelper ice = null;

    private Class cls;
    private List<T> list;

    public JsonHelper(Class cls) {
        this.cls = cls;
        list = new ArrayList<T>();
    }

    /**
     * 根据节点转化对象列表
     *
     * @param json
     * @param nodeName
     * @return
     */
    public List<T> getDatas(String json, String nodeName) {
        if (list != null) {
            list.clear();
        }
        try {
            JSONArray array = new JSONObject(json).getJSONArray(nodeName);

            for (int i = 0; i < array.length(); i++) {
                Gson gson = new Gson();
                list.add((T) gson.fromJson(array.getJSONObject(i).toString(),
                        cls));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }

    /**
     * @param json
     * @return
     */
    public List<T> getDatas(String json) {
        if (list != null) {
            list.clear();
        }
        if (StringHelper.isEmpty(json)) {
            return list;
        }
        try {
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                Gson gson = new Gson();
                list.add((T) gson.fromJson(array.getJSONObject(i).toString(), cls));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }

    /**
     * 根据节点转化对象
     *
     * @param json
     * @param nodeName
     * @return
     */

    public T getData(String json, String nodeName) {
        try {

            JSONObject object = new JSONObject(json);
            if (nodeName != null) {
                object = object.getJSONObject(nodeName);
            }
            Gson gson = new Gson();
            return (T) gson.fromJson(object.toString(), cls);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
