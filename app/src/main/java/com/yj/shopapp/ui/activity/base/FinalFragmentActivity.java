package com.yj.shopapp.ui.activity.base;
import java.lang.reflect.Field;

import net.tsz.afinal.annotation.view.EventListener;
import net.tsz.afinal.annotation.view.Select;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

/**
 * @ClassName FinalFragmentActivity
 * @Descirption 继承FragmentActivity的带有注入功能的Final框架activity
 */
public class FinalFragmentActivity extends FragmentActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		FinalFragmentActivity.initInjectedView(this);
	}


	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		FinalFragmentActivity.initInjectedView(this);
	}



	public void setContentView(View view) {
		super.setContentView(view);
		FinalFragmentActivity.initInjectedView(this);
	}

	/**
	 * 初始化Actvity中的注入属性
	 * 可用于与其他框架合用（如ActionBarShelock）
	 * <p>
	 * *必须在setContentView之后调用:
	 * <pre>
	 * protected void onCreate(Bundle savedInstanceState) {
	 *  super.onCreate(savedInstanceState);
	 *   setContentView(view);
	 *   FinalActivity.initInjectedView(this);
	 * }
	 * </pre>
	 * @param sourceActivity
	 */
	public static void initInjectedView(Activity sourceActivity){
		initInjectedView(sourceActivity,sourceActivity.getWindow().getDecorView());
	}

	/**
	 * 初始化指定View中的注入属性
	 * 可用于Fragment内使用InjectView<p>
	 * 示例：<p>
	 * 在onCreateView中:
	 * <pre>
	 * public View onCreateView(LayoutInflater inflater, ViewGroup container,
	 *      Bundle savedInstanceState) {
	 *  View viewRoot = inflater.inflate(R.layout.map_frame, container, false);
	 *  FinalActivity.initInjectedView(this,viewRoot);
	 * }
	 * </pre>
	 * @param sourceView
	 */
	public static void initInjectedView(Object injectedSource,View sourceView){
		Field[] fields = injectedSource.getClass().getDeclaredFields();
		if(fields!=null && fields.length>0){
			for(Field field : fields){
				ViewInject viewInject = field.getAnnotation(ViewInject.class);
				if(viewInject!=null){
					int viewId = viewInject.id();
					try {
						field.setAccessible(true);
						field.set(injectedSource,sourceView.findViewById(viewId));
					} catch (Exception e) {
						e.printStackTrace();
					}

					String clickMethod = viewInject.click();
					if(!TextUtils.isEmpty(clickMethod))
						setViewClickListener(sourceView,field,clickMethod);

					String longClickMethod = viewInject.longClick();
					if(!TextUtils.isEmpty(longClickMethod))
						setViewLongClickListener(sourceView,field,longClickMethod);

					String itemClickMethod = viewInject.itemClick();
					if(!TextUtils.isEmpty(itemClickMethod))
						setItemClickListener(sourceView,field,itemClickMethod);

					String itemLongClickMethod = viewInject.itemLongClick();
					if(!TextUtils.isEmpty(itemLongClickMethod))
						setItemLongClickListener(sourceView,field,itemLongClickMethod);

					Select select = viewInject.select();
					if(!TextUtils.isEmpty(select.selected()))
						setViewSelectListener(sourceView,field,select.selected(),select.noSelected());

				}
			}
		}
	}


	private static void setViewClickListener(Object injectedSource,Field field,String clickMethod){
		try {
			Object obj = field.get(injectedSource);
			if(obj instanceof View){
				((View)obj).setOnClickListener(new EventListener(injectedSource).click(clickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setViewLongClickListener(Object injectedSource,Field field,String clickMethod){
		try {
			Object obj = field.get(injectedSource);
			if(obj instanceof View){
				((View)obj).setOnLongClickListener(new EventListener(injectedSource).longClick(clickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setItemClickListener(Object injectedSource,Field field,String itemClickMethod){
		try {
			Object obj = field.get(injectedSource);
			if(obj instanceof AbsListView){
				((AbsListView)obj).setOnItemClickListener(new EventListener(injectedSource).itemClick(itemClickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setItemLongClickListener(Object injectedSource,Field field,String itemClickMethod){
		try {
			Object obj = field.get(injectedSource);
			if(obj instanceof AbsListView){
				((AbsListView)obj).setOnItemLongClickListener(new EventListener(injectedSource).itemLongClick(itemClickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setViewSelectListener(Object injectedSource,Field field,String select,String noSelect){
		try {
			Object obj = field.get(injectedSource);
			if(obj instanceof View){
				((AbsListView)obj).setOnItemSelectedListener(new EventListener(injectedSource).select(select).noSelect(noSelect));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
