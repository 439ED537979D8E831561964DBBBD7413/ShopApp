package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ubeen.Agents;
import com.yj.shopapp.ui.activity.adapter.SChooseAgentAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/10.
 * 选择批发商列表
 */
public class SChooseAgentActivity extends BaseActivity implements BaseRecyclerView
//        ,Mobilephone.MPreport
{
    private static final int REQUEST_CONTACT = 1;
    public static final int CHOOSEAGENT_TYPE_WHAT = 1;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.addAgent)
    ImageView addAgent;
    @BindView(R.id.id_drawer_layout)
    FrameLayout idDrawerLayout;


    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新
    private int mCurrentPage = 0;
    List<Agents> agentsList = new ArrayList<>();
    //List<SortModel> sortModels = new ArrayList<>();

    String uid;
    String token;
    String username = "";
    public String choosetype = "0";  // 0 默认进来选择  1 进来曹组

    private RecyclerViewHeaderFooterAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_chooseagent;
    }

    @Override
    protected void initData() {
        choosetype = getIntent().getExtras().getString("choosetype", "0");

        if (choosetype.equals("0")) {
            addAgent.setVisibility(View.GONE);
        }

        if (choosetype.equals("0")) {
            title.setText("选择批发商");

        } else {
            title.setText("我的批发商");
            //idRightBtu.setText("+");
        }
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        SChooseAgentAdapter sAdapter = new SChooseAgentAdapter(SChooseAgentActivity.this, agentsList, this);

        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, sAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());

        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);

                        refreshRequest();
                    }
                }, 50);
            }
        } else {
            showToastShort("网络不给力");
        }
    }


    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };

    @Override
    public void onItemClick(int position) {
        if (choosetype.equals("0")) {
            Bundle bundle = new Bundle();
            bundle.putString("agentuid", agentsList.get(position).getAgentuid());
            bundle.putString("agentuName", agentsList.get(position).getShopname());

            CommonUtils.goResult(mContext, bundle, SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT);
        }
        else if (choosetype.equals("setWId"))
        {
            PreferenceUtils.setPrefString(mContext, Contants.Preference.WID, agentsList.get(position).getAgentuid());
            CommonUtils.goActivity(mContext, SMainTabActivity.class, null, true);
        }
    }

    @Override
    public void onLongItemClick(final int position) {
        delectUser(position);
    }
    public void delectUser(final int position)
    {
        if (choosetype.equals("1")) {
            new MaterialDialog.Builder(mContext)
                    .content("是否删除" + agentsList.get(position).getShopname() + "?")
                    .positiveText("是")
                    .negativeText("否")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            delClient(position);
                        }
                    })
                    .show();
        }
    }






    public class mLoadMoreClickListener implements LoadMoreClickListener {

        @Override
        public void clickLoadMoreData() {

        }
    }

    public class MyScrollListener extends OnRecyclerViewScrollListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {
        }

        @Override
        public void onBottom() {
            loadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }



    /**************************/

    /**
     * 右侧事件操作
     **/


    @OnClick(R.id.submitTv)
    public void search() {
        if (isRequesting)
            return;

        String str = valueEt.getText().toString().trim();

        username = str;

        if (null != swipeRefreshLayout) {

            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    swipeRefreshLayout.setRefreshing(true);

                    refreshRequest();
                }
            }, 50);
        }
    }


    @OnClick(R.id.id_right_btu)
    public void setAddAgent() {
        showDialogAgent("");
    }


    /*****************
     * 网络请求
     *****************/

    public void refreshRequest() {
        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("username", username = valueEt.getText().toString().trim());
        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, Contants.PortU.AGENTS, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response" + json);
                agentsList.clear();
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Agents> jsonHelper = new JsonHelper<Agents>(Agents.class);

                    agentsList.addAll(jsonHelper.getDatas(json));

                    if (agentsList.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    adapter.removeFooter(loadMoreView);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                System.out.println("response" + e.toString());
                agentsList.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


    public void loadMoreRequest() {
        if (isRequesting)
            return;
        if (agentsList.size() < 20) {
            return;
        }
        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("username", username = valueEt.getText().toString().trim());

        HttpHelper.getInstance().post(mContext, Contants.PortU.AGENTS, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response==========" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Agents> jsonHelper = new JsonHelper<Agents>(Agents.class);

                    if (jsonHelper.getDatas(json).size() == 0) {
                        iLoadView.showFinishView(loadMoreView);
                    } else {
                        agentsList.addAll(jsonHelper.getDatas(json));
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    iLoadView.showFinishView(loadMoreView);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                mCurrentPage--;
                iLoadView.showErrorView(loadMoreView);
            }
        });
    }

    public void delClient(int pos) {
        if (isRequesting)
            return;

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("agentuid", agentsList.get(pos).getAgentuid());

        HttpHelper.getInstance().post(mContext, Contants.PortU.Deluser, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response===============" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("删除成功");

                    if (null != swipeRefreshLayout) { //删除成功重新刷新数据
                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(true);
                                refreshRequest();
                            }
                        }, 200);
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

    public void addAgent(String str) {
        if (isRequesting)
            return;

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("username", str);

        HttpHelper.getInstance().post(mContext, Contants.PortU.AddAgent, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response=" + json);

                if (JsonHelper.errorNo(json).equals("0")) {
                    showToastShort(JsonHelper.errorMsg(json));

                    if (null != swipeRefreshLayout) { //删除成功重新刷新数据
                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(true);
                                refreshRequest();
                            }
                        }, 200);
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

    public void showDialogAgent(String str) {
        new MaterialDialog.Builder(this)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("确定")
                .negativeText("手机通讯录")
//                .title(title)
                .input("批发商手机号码", str, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!CommonUtils.isMobileNum(input.toString())) {
                            showToastShort("手机号码填写有误");
                            return;
                        }
                        addAgent(input.toString());
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        startContact();
                      //  new Mobilephone(mContext,SChooseAgentActivity.this).getNumber();

                    }
                })
                .show();
    }


//    @Override
//    public void mReport(List<SortModel> list) {
//       // sortModels.addAll(list);
//        myDialog();
//    }
//
//    @Override
//    public void mStart() {
//
//    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri contactData = data.getData();
            Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
            cursor.moveToFirst();
            String num = this.getContactPhone(cursor);
           // phoneEt.setText(num);
            showDialogAgent(num);
            //打开短信app

        }




    }
    private String getContactPhone(Cursor cursor) {
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID  + "=" + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phone.getInt(typeindex);
                    String phoneNumber = phone.getString(index);
                    result = phoneNumber;
//                  switch (phone_type) {//此处请看下方注释
//                  case 2:
//                      result = phoneNumber;
//                      break;
//
//                  default:
//                      break;
//                  }
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
        return result;
    }

    ListView listView;

    public void myDialog() {

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_list, true)
                .title("手机通讯录")
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {

                        materialDialog.dismiss();
                        showDialogAgent("");
                    }
                })
                .autoDismiss(false)
                .build();

        listView = (ListView) dialog.getCustomView().findViewById(R.id.listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
              //  showDialogAgent(sortModels.get(position).getPhone());
                dialog.dismiss();
            }
        });


       // SortAdapter sortAdapter = new SortAdapter(mContext, sortModels);
        //listView.setAdapter(sortAdapter);
        CommonUtils.setListViewHeightBasedOnChildren(listView);
        dialog.show();
    }

    private void startContact() {

        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_PICK);

        intent.setData(ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(intent, REQUEST_CONTACT);

    }
}
