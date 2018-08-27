package com.yj.shopapp.dialog;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BoughtGoods;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.MessageEvent;
import com.yj.shopapp.util.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LK on 2018/3/6.
 *
 * @author LK
 */

public class BugGoodsV4Dialog extends DialogFragment implements TextWatcher {
    @BindView(R.id.shapname)
    TextView shapname;
    @BindView(R.id.shopprice)
    TextView shopprice;
    @BindView(R.id.shopspec)
    TextView shopspec;
    @BindView(R.id.shopCount)
    TextView shopCount;
    @BindView(R.id.count)
    EditText countTv;
    @BindView(R.id.goodtips)
    TextView goodtips;
    @BindView(R.id.goods_imag)
    ImageView goodsImag;
    @BindView(R.id.mReplayRelativeLayout)
    LinearLayout mReplayRelativeLayout;
    @BindView(R.id.warning_tv)
    TextView warningTv;
    @BindView(R.id.warning_tv_super)
    LinearLayout warningTvSuper;
    @BindView(R.id.special_note)
    TextView specialNote;
    private Context mContext;
    private int minnum, maxnum, gsum;
    private String uid, token;
    private Goods goods;
    private int number = 1;
    private String text1 = "", text2 = "";
    private String unit;
    Unbinder unbinder;
    private Double ratio;
    private KProgressHUD kProgressHUD;
    @BindView(R.id.shopSplit)
    TextView shopSplit;
    private BoughtGoods boughtGoods;
    private String itemId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mContext = getActivity();
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        Object o = getArguments().getParcelable("goods");
        if (o instanceof Goods) {
            goods = getArguments().getParcelable("goods");
            unit = goods.getUnit();
            itemId = goods.getId();
        } else if (o instanceof BoughtGoods) {
            boughtGoods = getArguments().getParcelable("goods");
            unit = boughtGoods.getUnit();
            itemId = boughtGoods.getId();
        }
        kProgressHUD = growProgress(Contants.Progress.SUMBIT_ING);
    }

    public static BugGoodsV4Dialog newInstance(Goods g) {

        Bundle args = new Bundle();
        args.putParcelable("goods", g);
        BugGoodsV4Dialog fragment = new BugGoodsV4Dialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static BugGoodsV4Dialog newInstance(BoughtGoods boughtGoods) {

        Bundle args = new Bundle();
        args.putParcelable("goods", boughtGoods);
        BugGoodsV4Dialog fragment = new BugGoodsV4Dialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void setDeta() {
        if (goods != null) {
            shapname.setText(goods.getName());
            shopprice.setText(String.format("￥%s", goods.getPrice()));
            shopspec.setText(goods.getSpecs());
            warningTvSuper.setVisibility(goods.getMsg().equals("") ? View.GONE : View.VISIBLE);
            warningTv.setText(goods.getMsg());
            specialNote.setText(goods.getSpecialnote());
            Glide.with(mContext).load(goods.getImgurl()).into(goodsImag);
        } else if (boughtGoods != null) {
            shapname.setText(boughtGoods.getName());
            shopprice.setText(String.format("￥%s", boughtGoods.getPrice()));
            shopspec.setText(boughtGoods.getSpecs());
            //warningTvSuper.setVisibility(boughtGoods.getMsg().equals("") ? View.GONE : View.VISIBLE);
            //warningTv.setText(boughtGoods.getMsg());
            warningTvSuper.setVisibility(View.GONE);
            specialNote.setText(boughtGoods.getSpecialnote());
            Glide.with(mContext).load(boughtGoods.getImgurl()).into(goodsImag);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window dialogWindow = getDialog().getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.7f);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buggoods, container);
        getDialog().getWindow().setWindowAnimations(R.style.popuwindow_animation);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countTv.addTextChangedListener(this);
        //软键盘隐藏edittext 失去焦点
//        mReplayRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        Rect r = new Rect();
//                        mReplayRelativeLayout.getWindowVisibleDisplayFrame(r);
//                        int screenHeight = mReplayRelativeLayout.getRootView()
//                                .getHeight();
//                        int heightDifference = screenHeight - (r.bottom);
//                        ratio = screenHeight > 1800 ? 0.5 : 0.4;
//                        if (heightDifference > -screenHeight * ratio) {
//                            //软键盘显示
//                        } else {
//                            //软键盘隐藏
//                            count.setFocusable(false);
//                            count.setFocusableInTouchMode(true);
//                        }
//                    }
//
//                });
        countTv.setHighlightColor(getResources().getColor(R.color.Orange));
        countTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkNum();
                return true;
            }
        });
        requestMinandMaxNum();
        setDeta();
    }

    @Override
    public void show(FragmentManager manager, String tag) {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mContext = null;
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getActivity().getWindow().getDecorView().getWindowToken(), 0);

    }

    @OnClick({R.id.exit_imag, R.id.add, R.id.minus, R.id.bayGoodsCar, R.id.count})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exit_imag:
                dismiss();
                break;
            case R.id.add:
                if (maxnum == 0) {
                    if (number < gsum) {
                        number++;
                        countTv.setText(String.valueOf(number));
                        countTv.setSelection(this.countTv.getText().length());
                    } else {
                        Toast.makeText(mContext, "最多购买" + gsum + unit, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (number < maxnum) {
                        number++;
                        countTv.setText(String.valueOf(number));
                        countTv.setSelection(this.countTv.getText().length());
                    } else {
                        Toast.makeText(mContext, "最多购买" + maxnum + unit, Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.minus:
                if (minnum == 0) {
                    if (number > 1) {
                        number--;
                        countTv.setText(String.valueOf(number));
                        countTv.setSelection(this.countTv.getText().length());
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
                        countTv.setText(String.valueOf(number));
                        countTv.setSelection(this.countTv.getText().length());
                    } else {
                        try {
                            Toast.makeText(mContext, "最少购买" + minnum + unit, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case R.id.bayGoodsCar:
                checkNum();
                break;
            default:
                break;
        }
    }

    private void judge() {
        if (gsum < minnum) {
            //editText.setFocusable(false);
            text1 = "起购：" + minnum;
            text2 = "库存:" + gsum;
            minnum = gsum;
            maxnum = gsum;
        } else if (gsum < maxnum) {
            if (minnum != 0) {
                text1 = "起购：" + minnum;
                text2 = "限购：" + gsum;
            } else {
                text1 = "限购：" + minnum;
                text2 = "库存:" + gsum;
            }
            maxnum = gsum;
        } else {
            if (minnum != 0) {
                text1 = "起购：" + minnum;
            }
            if (maxnum != 0) {
                text2 = "限购：" + maxnum;
            }
        }
        if (gsum != 0) {

            try {
                if (goods != null && !goods.getSplit().equals("")) {
                    shopSplit.setText(String.format("【%s】", goods.getSplit()));
                }
                shopCount.setText(String.format("库存%1$s%2$s", gsum, unit));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {

            if (goodtips != null) {
                ShowLog.e("text1" + text1 + "text2" + text2);
                if (!text1.equals("") && !text2.equals("")) {
                    goodtips.setText(text1 + unit + " " + text2 + unit);
                } else if (text1.equals("") && text2.equals("")) {
                    goodtips.setText("");
                } else if (text2.equals("")) {
                    goodtips.setText(String.format("%s%s", text1, unit));
                } else {
                    goodtips.setText(String.format("%s%s", text2, unit));
                }
            }

            if (countTv != null) {
                if (minnum == 0) {
                    countTv.setText(String.valueOf(number));
                } else {
                    //editText.setText("" + minnum);
                    number = minnum;
                    countTv.setText(String.valueOf(number));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkNum() {
        if (countTv.getText().toString().equals("") || !isNumeric(countTv.getText().toString())) {
            Toast.makeText(mContext, "输入内容为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(countTv.getText().toString()) < minnum) {
            Toast.makeText(mContext, "商品最少购买" + minnum + unit, Toast.LENGTH_SHORT).show();
            countTv.setText(String.valueOf(minnum));
            return;
        }
        if (countTv.getText().toString().equals("0")) {
            Toast.makeText(mContext, "至少购买一件", Toast.LENGTH_SHORT).show();
            return;
        }
        saveDolistcart();
    }

    /**
     * 请求最大和最小购买数量
     */
    private void requestMinandMaxNum() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemId);
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
                    judge();
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

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 保存购物车
     */
    private void saveDolistcart() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemId);
        params.put("itemsum", countTv.getText().toString().trim());

        HttpHelper.getInstance().post(mContext, Contants.PortU.DOLISTCART, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (kProgressHUD != null) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                if (kProgressHUD != null) {
                    kProgressHUD.show();
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    Toast.makeText(mContext, "加入购物车成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().postSticky(new MessageEvent(5,""));
                    dismiss();
                } else {
                    Toast.makeText(mContext, JsonHelper.errorMsg(json), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Toast.makeText(mContext, Contants.NetStatus.NETDISABLEORNETWORKDISABLE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public KProgressHUD growProgress(String label) {
        return KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setCancellable(false);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.countTv.setSelectAllOnFocus(true);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().isEmpty()) {
            //有数据
            int sum = Integer.parseInt(s.toString());
            if (maxnum == 0 && minnum == 0) {
                if (sum <= gsum) {
                    number = sum;
                } else {
                    number = gsum;
                    this.countTv.setText(String.valueOf(number));
                    countTv.setSelection(this.countTv.getText().length());
                    Toast.makeText(mContext, "数量超出库存", Toast.LENGTH_SHORT).show();
                }

            } else {
                if (maxnum != 0) {
                    if (sum > maxnum) {
                        this.countTv.setText(String.valueOf(maxnum));
                        number = maxnum;
                        goodtips.setText("商品最大购买数量为" + maxnum);
                        this.countTv.setSelection(this.countTv.getText().length());
                    } else {
                        number = sum;
                    }
                }
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
            kProgressHUD = null;
        }
    }
}
