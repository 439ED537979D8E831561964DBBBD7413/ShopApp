package com.yj.shopapp.util;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.yj.shopapp.ubeen.LookItem;
import com.yj.shopapp.ubeen.Spitem;
import com.yj.shopapp.ui.activity.ShowLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LK on 2018/3/6.
 *
 * @author LK
 */

public class BugGoodsDialog extends DialogFragment {
    @BindView(R.id.shapname)
    TextView shapname;
    @BindView(R.id.shopprice)
    TextView shopprice;
    @BindView(R.id.shopspec)
    TextView shopspec;
    @BindView(R.id.shopCount)
    TextView shopCount;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.goodtips)
    TextView goodtips;
    @BindView(R.id.goods_imag)
    ImageView goodsImag;
    private Context mContext;
    private int minnum, maxnum, gsum;
    private String uid, token;
    private Goods goods;
    private int number = 1;
    private String text1 = "", text2 = "";
    private LookItem lookItem;
    private Spitem spitem;
    Unbinder unbinder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        mContext = getActivity();
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        Object o = getArguments().getParcelable("goods");
        if (o instanceof LookItem) {
            lookItem = getArguments().getParcelable("goods");
        } else if (o instanceof Goods) {
            goods = getArguments().getParcelable("goods");
        } else {
            spitem = getArguments().getParcelable("goods");
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

    private void setDeta() {
        if (goods != null) {
            shapname.setText(goods.getName());
            shopprice.setText("￥" + goods.getPrice());
            shopspec.setText(goods.getSpecs());
            Glide.with(mContext).load(goods.getImgurl()).apply(new RequestOptions().centerCrop()).into(goodsImag);
        } else if (lookItem != null) {
            shapname.setText(lookItem.getName());
            shopprice.setText("￥" + lookItem.getSprice());
            shopspec.setText(lookItem.getSpecs());
            Glide.with(mContext).load(lookItem.getImgurl()).apply(new RequestOptions().centerCrop()).into(goodsImag);
        } else {
            shapname.setText(spitem.getItemname());
            shopprice.setText("￥" + spitem.getPrice());
            shopspec.setText("");
            Glide.with(mContext).load(spitem.getImgurl()).apply(new RequestOptions().centerCrop()).into(goodsImag);
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
        } else {
            requestMinandMaxNum(spitem.getId());
        }
        setDeta();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buggoods, container);
        getDialog().getWindow().setWindowAnimations(R.style.popuwindow_animation);
        unbinder=ButterKnife.bind(this, view);
        return view;
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
    }

    @OnClick({R.id.exit_imag, R.id.add, R.id.minus, R.id.bayGoodsCar})
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
                    }
                } else {
                    if (number < maxnum) {
                        number++;
                        count.setText(number + "");
                    } else {
                        Toast.makeText(mContext, "最多购买" + maxnum + goods.getUnit(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.minus:
                if (minnum == 0) {
                    if (number > 1) {
                        number--;
                        count.setText(number + "");
                    } else {
                        Toast.makeText(mContext, "最少购买一" + goods.getUnit(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (number > 1 && number > minnum) {
                        number--;
                        count.setText(number + "");
                    } else {
                        Toast.makeText(mContext, "最少购买" + minnum + goods.getUnit(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.bayGoodsCar:
                if (goods != null) {
                    saveDolistcart(goods.getId(), number + "");
                } else if (lookItem != null) {
                    saveDolistcart(lookItem.getId(), number + "");
                } else {
                    saveDolistcart(spitem.getId(), number + "");
                }

                dismiss();
                break;
            default:
                break;
        }
    }

    private void judge() {
        if (gsum < minnum) {
            //editText.setFocusable(false);
            text1 = "起购数量为" + minnum;
            text2 = "库存为" + gsum;
            minnum = gsum;
            maxnum = gsum;
        } else if (gsum < maxnum) {
            if (minnum != 0) {
                text1 = "起购数量为" + minnum;
                text2 = "限购数量为" + gsum;
            } else {
                text1 = "限购数量为" + minnum;
                text2 = "库存为" + gsum;
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
                    shopCount.setText("库存为" + gsum + goods.getUnit());
                } else if (lookItem != null) {
                    shopCount.setText("库存为" + gsum + lookItem.getUnit());
                } else {
                    shopCount.setText("库存为" + gsum + spitem.getUnit());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        goodtips.setText(text1 + text2);
        if (minnum == 0) {
            count.setText(number + "");
        } else {
            //editText.setText("" + minnum);
            count.setText("" + minnum);
            number = minnum;
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

    /**
     * 保存购物车
     */
    public void saveDolistcart(String itemid, String itemsum) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemid);
        params.put("itemsum", itemsum);

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
}
