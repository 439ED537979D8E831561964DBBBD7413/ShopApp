package com.yj.shopapp.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by jm on 2016/4/27.
 */
public class DialogUtils {
    public MaterialDialog.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(MaterialDialog.Builder builder) {
        this.builder = builder;
    }

    MaterialDialog dialog;
    MaterialDialog.Builder builder;
    public void getMaterialDialog(Context context, String title, String content, MaterialDialog.SingleButtonCallback callback,MaterialDialog.SingleButtonCallback nacallback){

        builder= new MaterialDialog.Builder(context);

        builder.title(title);
        builder.content(content);
        builder.negativeText("否");
       builder.positiveText("是");
        if (callback!=null) {
            builder.onPositive(callback);
        }
        if (nacallback!=null)
        {
            builder.onNegative(nacallback);
        }








        dialog=builder.build();
    }
    public void getInputMaterialDialog(Context context, String title,String hint,MaterialDialog.InputCallback inputCallback, MaterialDialog.SingleButtonCallback callback,MaterialDialog.SingleButtonCallback nacallback){

        builder= new MaterialDialog.Builder(context);

        builder.title(title);
        builder.input(hint, "", false, inputCallback);
        builder.negativeText("否");
        builder.positiveText("是");
        if (callback!=null) {
            builder.onPositive(callback);
        }
        if (nacallback!=null)
        {
            builder.onNegative(nacallback);
        }

        dialog=builder.build();
    }

    public void show()
    {
        if (dialog!=null)
        {
            dialog.show();
        }

    }

}
