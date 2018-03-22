package com.yj.shopapp.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yj.shopapp.R;

/**
 * Created by LK on 2017/11/9.
 */

public class ShowDialog implements View.OnClickListener {
    private EditText editText;
    private TextView textView, cancel, sure, stock;
    private String text1 = "", text2 = "", unit = "";
    private MaterialDialog dialog;
    private int minnum, maxnum, gsum;
    private showDialogInterface dialogInterface;
    private Context mContext;
    private View RootView;

    public ShowDialog(Context context) {
        mContext = context;
    }

    public ShowDialog init(int resid) {
        dialog = new MaterialDialog.Builder(mContext)
                .customView(resid, false).build();
        RootView = dialog.getCustomView();
        return this;
    }

    public ShowDialog initView(int min, int max, int sum, String unit) {
        this.minnum = min;
        this.maxnum = max;
        this.gsum = sum;
        this.unit = unit;
        textView = (TextView) RootView.findViewById(R.id.prompt_tv);
        editText = (EditText) RootView.findViewById(R.id.inputEt);
        cancel = (TextView) RootView.findViewById(R.id.tvcancel);
        sure = (TextView) RootView.findViewById(R.id.tvsure);
        stock = (TextView) RootView.findViewById(R.id.item_sum);
        cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
        return this;
    }

    public ShowDialog judge() {
        if (gsum < minnum) {
            editText.setFocusable(false);
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
            stock.setText("库存为" + gsum + unit);
        }
        textView.setText(text1 + text2);
        if (minnum == 0) {
            editText.setText("");
        } else {
            editText.setText("" + minnum);
        }
        editText.setSelection(editText.getText().length());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    int number = Integer.parseInt(s.toString());
                    if (maxnum != 0 && number > maxnum) {
                        editText.setText(maxnum + "");
                        editText.setSelection(editText.getText().length());
                        textView.setText("最大购买数量为" + maxnum);
                    }
                } else {
                    textView.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return this;
    }

    public void show() {
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvcancel:
                dialog.dismiss();
                break;
            case R.id.tvsure:
                //确定按钮监听
                String str = editText.getText().toString().trim();
                if (StringHelper.isEmpty(str)) {
                    editText.setError("请填写购买数量！");
                } else {
                    int num = Integer.parseInt(str);
                    if (num < minnum) {
                        textView.setText("购买商品小于最小购买量");
                        editText.setText(minnum + "");
                        editText.setSelection(editText.getText().length());
                    } else {
                        dialogInterface.setSunAndName(str);
                        // saveDolistcart(goodsId, str, goodsList.get(postion).getName());
                        dialog.dismiss();
                    }
                }
                break;
            default:
                break;
        }
    }

    public interface showDialogInterface {
        void setSunAndName(String str);
    }

    public ShowDialog setDialogInterface(showDialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
        return this;
    }

}
