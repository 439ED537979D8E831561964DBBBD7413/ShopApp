package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.AgencyAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.wbeen.Agency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by huang on 2016/9/11.
 */
public class WAgencyActivity extends BaseActivity {

    private static final int REQUEST_CONTACT = 1;
    public static final int CHOOSEAGENT_TYPE_WHAT = 1;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.addAgent)
    ImageView addAgent;
    @BindView(R.id.id_drawer_layout)
    LinearLayout idDrawerLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isRequesting = false;//标记，是否正在刷新
    private int mCurrentPage = 0;
    private List<Agency> agentsList = new ArrayList<>();
    private List<Agency> newAgentsList = new ArrayList<>();
    String username = "";
    public String choosetype = "0";  // 0 默认进来选择  1 进来曹组

    private AgencyAdapter sAdapter;
    private boolean isFilter;

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_chooseagent;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {

        if (getIntent().hasExtra("choosetype")) {
            choosetype = getIntent().getStringExtra("choosetype");
        }
        if (choosetype.equals("0")) {
            addAgent.setVisibility(View.GONE);
        }
        if (choosetype.equals("0")) {
            title.setText("选择供应商");
        } else {
            title.setText("我的供应商");
        }

        sAdapter = new AgencyAdapter(mContext);
        sAdapter.setFiter(true);
        layoutManager = new LinearLayoutManager(mContext);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
            recyclerView.setAdapter(sAdapter);
        }
        sAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (choosetype.equals("0") && !isFilter) {
                    Bundle bundle = new Bundle();
                    bundle.putString("agentuid", agentsList.get(position).getId());
                    bundle.putString("agentuName", agentsList.get(position).getName());
                    CommonUtils.goResult(mContext, bundle, WAgencyActivity.CHOOSEAGENT_TYPE_WHAT);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("agentuid", newAgentsList.get(position).getId());
                    bundle.putString("agentuName", newAgentsList.get(position).getName());
                    CommonUtils.goResult(mContext, bundle, WAgencyActivity.CHOOSEAGENT_TYPE_WHAT);
                }
            }
        });
        if (isNetWork(mContext)) {
            refreshRequest();
        }
        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    isFilter = true;
                    for (Agency a : agentsList) {
                        if (a.getName().contains(s.toString())) {
                            newAgentsList.add(a);
                        }
                    }
                    sAdapter.setList(newAgentsList);
                    sAdapter.setFiter(false);
                } else {
                    isFilter = false;
                    newAgentsList.clear();
                    sAdapter.setList(agentsList);
                    sAdapter.setFiter(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


//    public void delectUser(final int position) {
//        if (choosetype.equals("1")) {
//            new MaterialDialog.Builder(mContext)
//                    .content("是否删除" + agentsList.get(position).getName() + "?")
//                    .positiveText("是")
//                    .negativeText("否")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
//                            delClient(position);
//                        }
//                    })
//                    .show();
//        }
//    }


//    /**************************/
//
//    /**
//     * 右侧事件操作
//     **/
//
//    @OnClick(R.id.submitTv)
//    public void search() {
//        if (isRequesting)
//            return;
//
//        String str = valueEt.getText().toString().trim();
//
//        username = str;
//
//        if (null != swipeRefreshLayout) {
//
//            swipeRefreshLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    swipeRefreshLayout.setRefreshing(true);
//
//                    refreshRequest();
//                }
//            }, 50);
//        }
//    }
//
//
//    @OnClick(R.id.id_right_btu)
//    public void setAddAgent() {
//        showDialogAgent("");
//    }


    /*****************
     * 网络请求
     *****************/

    public void refreshRequest() {
        mCurrentPage = 1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Agency, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
                agentsList.clear();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Agency> jsonHelper = new JsonHelper<Agency>(Agency.class);
                    agentsList.addAll(jsonHelper.getDatas(json));
                    if (choosetype.equals("0")) {
                        Agency agency = new Agency();
                        agency.setId("");
                        agency.setMobile("");
                        agency.setName("");
                        agency.setTel("无");
                        agency.setUid("");
                        agentsList.add(0, agency);
                    }
                    sAdapter.setList(agentsList);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                agentsList.clear();
            }
        });
    }


//    public void loadMoreRequest() {
//
//        if (isRequesting)
//            return;
//        if (agentsList.size() < 20) {
//            return;
//        }
//        mCurrentPage++;
//
//        iLoadView.showLoadingView(loadMoreView);
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uid", uid);
//        params.put("token", token);
//
//
//        HttpHelper.getInstance().post(mContext, Contants.PortA.Agency, params, new OkHttpResponseHandler<String>(mContext) {
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                isRequesting = false;
//            }
//
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                isRequesting = true;
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//
//                System.out.println("response==========" + json);
//                if (JsonHelper.isRequstOK(json, mContext)) {
//                    JsonHelper<Agency> jsonHelper = new JsonHelper<Agency>(Agency.class);
//
//                    if (jsonHelper.getDatas(json).size() == 0) {
//                        iLoadView.showFinishView(loadMoreView);
//                    } else {
//                        agentsList.addAll(jsonHelper.getDatas(json));
//                    }
//                    if (choosetype.equals("0")) {
//                        Agency agency = new Agency();
//                        agency.setId("");
//                        agency.setMobile("");
//                        agency.setName("");
//                        agency.setTel("无");
//                        agency.setUid("");
//                        agentsList.add(agency);
//                    }
//                } else if (JsonHelper.getRequstOK(json) == 6) {
//                    iLoadView.showFinishView(loadMoreView);
//                } else {
//                    showToastShort(JsonHelper.errorMsg(json));
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//                mCurrentPage--;
//                iLoadView.showErrorView(loadMoreView);
//            }
//        });
//    }

//    public void delClient(int pos) {
//        if (isRequesting)
//            return;
//
//        //显示ProgressDialog
//        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uid", uid);
//        params.put("token", token);
//        params.put("agentuid", agentsList.get(pos).getName());
//
//        HttpHelper.getInstance().post(mContext, Contants.PortU.Deluser, params, new OkHttpResponseHandler<String>(mContext) {
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                progressDialog.show();
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//
//                System.out.println("response===============" + json);
//
//                if (JsonHelper.isRequstOK(json, mContext)) {
//                    showToastShort("删除成功");
//
//                    if (null != swipeRefreshLayout) { //删除成功重新刷新数据
//                        swipeRefreshLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                swipeRefreshLayout.setRefreshing(true);
//                                refreshRequest();
//                            }
//                        }, 200);
//                    }
//                } else {
//                    showToastShort(JsonHelper.errorMsg(json));
//                }
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//            }
//        });
//    }

//    public void addAgent(String str) {
//        if (isRequesting)
//            return;
//
//        //显示ProgressDialog
//        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uid", uid);
//        params.put("token", token);
//        params.put("username", str);
//
//        HttpHelper.getInstance().post(mContext, Contants.PortU.AddAgent, params, new OkHttpResponseHandler<String>(mContext) {
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                progressDialog.show();
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//
//                System.out.println("response=" + json);
//
//                if (JsonHelper.errorNo(json).equals("0")) {
//                    showToastShort(JsonHelper.errorMsg(json));
//
//                    if (null != swipeRefreshLayout) { //删除成功重新刷新数据
//                        swipeRefreshLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                swipeRefreshLayout.setRefreshing(true);
//                                refreshRequest();
//                            }
//                        }, 200);
//                    }
//                } else {
//                    showToastShort(JsonHelper.errorMsg(json));
//                }
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//            }
//        });
//    }

//    public void showDialogAgent(String str) {
//        new MaterialDialog.Builder(this)
//                .inputType(InputType.TYPE_CLASS_TEXT |
//                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
//                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
//                .positiveText("确定")
//                .negativeText("手机通讯录")
////                .title(title)
//                .input("批发商手机号码", str, false, new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        if (!CommonUtils.isMobileNum(input.toString())) {
//                            showToastShort("手机号码填写有误");
//                            return;
//                        }
//                        addAgent(input.toString());
//                        dialog.dismiss();
//                    }
//                })
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
//                        startContact();
//                        //  new Mobilephone(mContext,SChooseAgentActivity.this).getNumber();
//
//                    }
//                })
//                .show();
//    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            Uri contactData = data.getData();
//            Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
//            cursor.moveToFirst();
//            String num = this.getContactPhone(cursor);
//            // phoneEt.setText(num);
//            showDialogAgent(num);
//            //打开短信app
//
//        }
//
//
//    }

//    private String getContactPhone(Cursor cursor) {
//        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
//        int phoneNum = cursor.getInt(phoneColumn);
//        String result = "";
//        if (phoneNum > 0) {
//            // 获得联系人的ID号
//            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//            String contactId = cursor.getString(idColumn);
//            // 获得联系人电话的cursor
//            Cursor phone = getContentResolver().query(
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
//            if (phone.moveToFirst()) {
//                for (; !phone.isAfterLast(); phone.moveToNext()) {
//                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                    int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
//                    int phone_type = phone.getInt(typeindex);
//                    String phoneNumber = phone.getString(index);
//                    result = phoneNumber;
////                  switch (phone_type) {//此处请看下方注释
////                  case 2:
////                      result = phoneNumber;
////                      break;
////
////                  default:
////                      break;
////                  }
//                }
//                if (!phone.isClosed()) {
//                    phone.close();
//                }
//            }
//        }
//        return result;
//    }

//    private void startContact() {
//
//        Intent intent = new Intent();
//
//        intent.setAction(Intent.ACTION_PICK);
//
//        intent.setData(ContactsContract.Contacts.CONTENT_URI);
//
//        startActivityForResult(intent, REQUEST_CONTACT);
//
//    }
}