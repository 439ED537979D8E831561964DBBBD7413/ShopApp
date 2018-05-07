package com.yj.shopapp.util;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LK on 2018/4/15.
 *
 * @author LK
 */
public class GoodsNumInputDialog extends DialogFragment implements TextWatcher {
    @BindView(R.id.num)
    EditText num;
    private Context mContext;
    private String uid;
    private String token;
    private int minnum, maxnum, gsum;
    private String itemid;
    private int number = 1;
    private String unit;
    Unbinder unbinder;
    private OnShopNumListener listener;

    public static GoodsNumInputDialog newInstance(String id, String unit, int number) {
        Bundle args = new Bundle();
        args.putString("item_id", id);
        args.putString("unit", unit);
        args.putInt("number", number);
        GoodsNumInputDialog fragment = new GoodsNumInputDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.GoodsDialog);
        mContext = getActivity();
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        itemid = getArguments().getString("item_id");
        unit = getArguments().getString("unit");
        number = getArguments().getInt("number", 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.goodsnumdialog, container);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        num.setText(number + "");
        num.addTextChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestMinandMaxNum(itemid);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(false);
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = (int) (displayMetrics.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void show(android.app.FragmentManager manager, String tag) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed())
                return;
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求最大和最小购买数量
     */
    private void requestMinandMaxNum(final String goodsId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", goodsId);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ITEMS_LIMITS, params, new OkHttpResponseHandler<String>(mContext) {
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
                ShowLog.e(json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    minnum = Integer.parseInt(jsonObject.getString("minnum"));
                    maxnum = Integer.parseInt(jsonObject.getString("maxnum"));
                    gsum = Integer.parseInt(jsonObject.getString("itemsum"));
                    //judge();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Toast.makeText(mContext, Contants.NetStatus.NETDISABLEORNETWORKDISABLE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.cutdown, R.id.add, R.id.cancel_tv, R.id.sure_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cutdown:
                if (minnum == 0) {
                    if (number > 1) {
                        number--;
                        num.setText(number + "");
                        num.setSelection(this.num.getText().length());
                    } else {
                        try {
                            Toast.makeText(mContext, "最少购买一" + unit, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (number > 1 && number > minnum) {
                        number--;
                        num.setText(number + "");
                        num.setSelection(this.num.getText().length());
                    } else {
                        try {
                            Toast.makeText(mContext, "最少购买" + minnum + unit, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.add:
                if (maxnum == 0) {
                    if (number < gsum) {
                        number++;
                        num.setText(number + "");
                        num.setSelection(this.num.getText().length());
                    } else {
                        Toast.makeText(mContext, "最多购买" + gsum + unit, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (number < maxnum) {
                        number++;
                        num.setText(number + "");
                        num.setSelection(this.num.getText().length());
                    } else {
                        Toast.makeText(mContext, "最多购买" + maxnum + unit, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.cancel_tv:
                dismiss();
                break;
            case R.id.sure_tv:
                if (Integer.parseInt(num.getText().toString()) < minnum) {
                    Toast.makeText(mContext, "商品最少购买" + minnum + unit, Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.goodsNum(num.getText().toString());
                dismiss();
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().isEmpty()) {
            //有数据
            int sum = Integer.parseInt(s.toString());
            if (maxnum == 0 && minnum == 0) {
                number = sum;
            } else {
                if (maxnum != 0) {
                    if (sum > maxnum) {
                        this.num.setText(maxnum + "");
                        number = maxnum;
                        this.num.setSelection(this.num.getText().length());
                        Toast.makeText(mContext, "商品最多购买" + maxnum + unit, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public GoodsNumInputDialog setListener(OnShopNumListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnShopNumListener {
        void goodsNum(String number);
    }
}
