package com.yj.shopapp.ui.activity.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.MyApplication;
import com.yj.shopapp.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseTabActivity extends FinalFragmentActivity{

	protected Context mContext = this;

	protected MyApplication myApplication;

	protected List<Fragment> mFragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		super.onCreate(savedInstanceState);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//初始化
		init(savedInstanceState);

		myApplication = (MyApplication)getApplication();
	}

	/**
	 *
	 * @description: 主要用于控件等初始化工作
	 * @param savedInstanceState
	 * @return void
	 */
	protected abstract void init(Bundle savedInstanceState);


	/**
	 * 重写setContentView，让子类传入的View上方再覆盖一层LoadingView
	 */
	@Override
	public void setContentView(int layoutResID) {
		View view=LayoutInflater.from(mContext).inflate(layoutResID, null);
		super.setContentView(view);
	}

	/**
	 * @Description:finish Activity 提供给xml中"返回"Button的android:Onclick属性调用
	 */
	public void finish(View view) {
		finish();
	}



	@Override
	protected void onResume() {
		super.onResume();



	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().removeActivityFromStack(this);

	}



	private Toast mToast = null;

	private void toast(String msg, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, msg, duration);
		} else {
			mToast.setText(msg);
			//mToast.setDuration(duration);
		}
		mToast.show();
	}


	public void showToastLong(String msg) {
		if (null != msg && !CommonUtils.isEmpty(msg)) {
			toast(msg, Toast.LENGTH_LONG);
		}
	}


	public void showToastShort(String msg) {
		if (null != msg && !CommonUtils.isEmpty(msg)) {
			toast(msg, Toast.LENGTH_SHORT);
		}
	}

	public KProgressHUD growProgress(String label) {
		final KProgressHUD builder = KProgressHUD.create(mContext)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel(label)
				.setCancellable(true);
		return builder;
	}
}

