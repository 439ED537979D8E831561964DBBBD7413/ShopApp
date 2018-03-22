package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Agents;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jm on 2016/5/13.
 */
public class ReportAgents {
    static ReportAgents reportAgents;

    public ReportAgents getSingle(){
        if(reportAgents == null){
            reportAgents = new ReportAgents();
        }
        return reportAgents;
    }

    List<Agents> agentsList = new ArrayList<>();

    private void refreshRequest(final Context mContext, String uid, String token, final Agents agents) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mContext, Contants.PortU.AGENTS, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onBefore() {
                super.onBefore();

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response" + json);
                agentsList.clear();
                if (JsonHelper.isRequstOK(json,mContext)) {
                    JsonHelper<Agents> jsonHelper = new JsonHelper<Agents>(Agents.class);
                    agentsList.addAll(jsonHelper.getDatas(json));
                } else {
//                    string sjson = PreferenceUtils.getPrefString(mContext,Contants.Preference.AGENTS)

                }
                agents.onResponse(agentsList);
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                agents.onResponse(agentsList);
            }
        });
    }

    public interface Agents{
        void onResponse(List<Agents> agentses);
    }


}
