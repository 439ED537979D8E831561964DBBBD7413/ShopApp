package com.yj.shopapp.util;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.request.RequestOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ubeen.HotIndex;
import com.yj.shopapp.ubeen.LookItem;
import com.yj.shopapp.ubeen.Spitem;
import com.yj.shopapp.ui.activity.ShowLog;

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

public class BugGoodsDialog extends DialogFragment implements TextWatcher {
    @BindView(R.id.shapname)
    TextView shapname;
    @BindView(R.id.shopprice)
    TextView shopprice;
    @BindView(R.id.shopspec)
    TextView shopspec;
    @BindView(R.id.shopCount)
    TextView shopCount;
    @BindView(R.id.count)
    EditText count;
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
    private Context mContext;
    private int minnum, maxnum, gsum;
    private String uid, token;
    private Goods goods;
    private int number = 1;
    private String text1 = "", text2 = "";
    private LookItem lookItem;
    private Spitem spitem;
    private String unit;
    private HotIndex index;
    Unbinder unbinder;
    private Double ratio;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mContext = getActivity();
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        Object o = getArguments().getParcelable("goods");
        if (o instanceof LookItem) {
            lookItem = getArguments().getParcelable("goods");
            unit = lookItem.getUnit();
        } else if (o instanceof Goods) {
            goods = getArguments().getParcelable("goods");
            unit = goods.getUnit();
        } else if (o instanceof Spitem) {
            spitem = getArguments().getParcelable("goods");
            unit = spitem.getUnit();
        } else {
            index = getArguments().getParcelable("goods");
            unit = index.getUnit();
        }

    }

    public static BugGoodsDialog newInstance(Goods g) {

        Bundle args = new Bundle();
        args.putParcelable("goods", g);
        BugGoodsDialog fragment = new BugGoodsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static BugGoodsDialog newInstance(LookItem l) {
        Bundle args = new Bundle();
        args.putParcelable("goods", l);
        BugGoodsDialog fragment = new BugGoodsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static BugGoodsDialog newInstance(Spitem s) {
        Bundle args = new Bundle();
        args.putParcelable("goods", s);
        BugGoodsDialog fragment = new BugGoodsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static BugGoodsDialog newInstance(HotIndex h) {

        Bundle args = new Bundle();
        args.putParcelable("goods", h);
        BugGoodsDialog fragment = new BugGoodsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void setDeta() {
        if (goods != null) {
            shapname.setText(goods.getName());
            shopprice.setText("￥" + goods.getPrice());
            shopspec.setText(goods.getSpecs());
            warningTvSuper.setVisibility(goods.getMsg().equals("") ? View.GONE : View.VISIBLE);
            warningTv.setText(goods.getMsg());
            Glide.with(mContext).load(goods.getImgurl()).apply(new RequestOptions().centerCrop()).into(goodsImag);
        } else if (lookItem != null) {
            shapname.setText(lookItem.getName());
            shopprice.setText("￥" + lookItem.getSprice());
            shopspec.setText(lookItem.getSpecs());
            warningTvSuper.setVisibility(lookItem.getMsg().equals("") ? View.GONE : View.VISIBLE);
            warningTv.setText(lookItem.getMsg());
            Glide.with(mContext).load(lookItem.getImgurl()).apply(new RequestOptions().centerCrop()).into(goodsImag);
        } else if (spitem != null) {
            shapname.setText(spitem.getItemname());
            shopprice.setText("￥" + spitem.getPrice());
            shopspec.setText(spitem.getSpecs());
            warningTvSuper.setVisibility(spitem.getMsg().equals("") ? View.GONE : View.VISIBLE);
            warningTv.setText(spitem.getMsg());
            Glide.with(mContext).load(spitem.getImgurl()).apply(new RequestOptions().centerCrop()).into(goodsImag);
        } else {
            shapname.setText(index.getName());
            shopprice.setText("￥" + index.getPrice());
            shopspec.setText(index.getSpecs());
            warningTvSuper.setVisibility(index.getMsg().equals("") ? View.GONE : View.VISIBLE);
            warningTv.setText(index.getMsg());
            Glide.with(mContext).load(index.getImgurl()).apply(new RequestOptions().centerCrop()).into(goodsImag);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window dialogWindow = getDialog().getWindow();
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

    @Override
    public void onResume() {
        super.onResume();
        if (goods != null) {
            requestMinandMaxNum(goods.getId());
        } else if (lookItem != null) {
            requestMinandMaxNum(lookItem.getId());
        } else if (spitem != null) {
            requestMinandMaxNum(spitem.getId());
        } else {
            requestMinandMaxNum(index.getId());
        }
        setDeta();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buggoods, container);
        getDialog().getWindow().setWindowAnimations(R.style.popuwindow_animation);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        count.addTextChangedListener(this);
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
        count.setHighlightColor(getResources().getColor(R.color.Orange));
        count.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkNum();
                return true;
            }
        });
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
                        count.setText(number + "");
                        count.setSelection(this.count.getText().length());
                    } else {
                        Toast.makeText(mContext, "最多购买" + gsum + unit, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (number < maxnum) {
                        number++;
                        count.setText(number + "");
                        count.setSelection(this.count.getText().length());
                    } else {
                        Toast.makeText(mContext, "最多购买" + maxnum + unit, Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.minus:
                if (minnum == 0) {
                    if (number > 1) {
                        number--;
                        count.setText(number + "");
                        count.setSelection(this.count.getText().length());
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
                        count.setText(number + "");
                        count.setSelection(this.count.getText().length());
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
            text1 = "起购数量为" + minnum;
            text2 = "库存" + gsum;
            minnum = gsum;
            maxnum = gsum;
        } else if (gsum < maxnum) {
            if (minnum != 0) {
                text1 = "起购数量为" + minnum;
                text2 = "限购数量为" + gsum;
            } else {
                text1 = "限购数量为" + minnum;
                text2 = "库存" + gsum;
            }
            maxnum = gsum;
        } else {
            if (minnum != 0) {
                text1 = "起购数量为" + minnum;
            }
            if (maxnum != 0) {
                text2 = "限购数量为" + maxnum;
            }
        }
        if (gsum != 0) {

            try {
                if (goods != null) {
                    shopCount.setText("库存" + gsum + goods.getUnit());
                } else if (lookItem != null) {
                    shopCount.setText("库存" + gsum + lookItem.getUnit());
                } else if (spitem != null) {
                    shopCount.setText("库存" + gsum + spitem.getUnit());
                } else {
                    shopCount.setText(String.format("库存%1$s%2$s", gsum, index.getUnit()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (goodtips != null) {
            goodtips.setText(text1 + text2);
        }
        if (minnum == 0) {
            count.setText(number + "");
        } else {
            //editText.setText("" + minnum);
            count.setText("" + minnum);
            number = minnum;
        }
    }

    private void checkNum() {
        if (count.getText().toString().equals("") || !isNumeric(count.getText().toString())) {
            Toast.makeText(mContext, "输入内容为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(count.getText().toString()) < minnum) {
            goodtips.setText("商品最少购买" + minnum + "件");
            return;
        }
        if (goods != null) {
            saveDolistcart(goods.getId());
        } else if (lookItem != null) {
            saveDolistcart(lookItem.getId());
        } else if (spitem != null) {
            saveDolistcart(spitem.getId());
        } else {
            saveDolistcart(index.getId());
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

    public boolean isNumeric(String str) {
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
    public void saveDolistcart(String itemid) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemid);
        params.put("itemsum", count.getText().toString().trim());

        final KProgressHUD kProgressHUD = growProgress(Contants.Progress.SUMBIT_ING);

        HttpHelper.getInstance().post(mContext, Contants.PortU.DOLISTCART, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                kProgressHUD.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                kProgressHUD.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    Toast.makeText(mContext, "加入购物车成功", Toast.LENGTH_SHORT).show();
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
        KProgressHUD builder = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setCancellable(false);
        return builder;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.count.setSelectAllOnFocus(true);
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
                        this.count.setText(maxnum + "");
                        number = maxnum;
                        goodtips.setText("商品最大购买数量为" + maxnum);
                        this.count.setSelection(this.count.getText().length());
                    }
                }
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
