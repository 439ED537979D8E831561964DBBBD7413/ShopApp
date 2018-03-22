package com.yj.shopapp.view.EasyBanner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by huanghao on 2016/11/18.
 */

public class BannerView {
    OnclickLisenner onclickLisenner;
    private int currentItem = 0;
    List<View> views = new ArrayList<>();
    Context context;
    ViewPager viewPager;
    int layoutId;
    List<?> banners;

    LinearLayout dinLin;

    BannerAdapter bannerAdapter;
    int bannerPosition = 0;
    private List<View> dots = new ArrayList<>();
    public int isStop=1;

    public BannerAdapter getBannerAdapter() {
        return bannerAdapter;
    }

    public void setOnclickLisenner(OnclickLisenner onclickLisenner) {
        this.onclickLisenner = onclickLisenner;
    }

    public LinearLayout getDinLin() {
        return dinLin;
    }

    public void setDinLin(LinearLayout dinLin) {
        this.dinLin = dinLin;
    }

    public void setBannerAdapter(BannerAdapter bannerAdapter) {
        this.bannerAdapter = bannerAdapter;
        InitData();
    }

    private ScheduledExecutorService scheduledExecutorService;

    protected BannerView() {
    }

    public BannerView(ViewPager viewPager, List<Banner> banners, Context context, LinearLayout dinLin) {
        this.viewPager = viewPager;
        this.banners = banners;
        this.context = context;
        this.dinLin = dinLin;
    }


    public void InitData() {
        addDynamicView();
        addDian();
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE)
                {
                       isStop=0;
                }
                else if (event.getAction()==MotionEvent.ACTION_UP)
                {
                   isStop=1;
                }
                else if (event.getAction()==MotionEvent.ACTION_CANCEL)
                {
                    isStop=1;
                }
                return false;
            }
        });
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new MyAdapter() {
        });
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new MyPageChangeListener());
        startAd();
    }

    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 褰揂ctivity鏄剧ず鍑烘潵鍚庯紝姣忎袱绉掑垏鎹竴娆″浘鐗囨樉绀�
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2, 5,
                TimeUnit.SECONDS);

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            bannerPosition++;
            viewPager.setCurrentItem(bannerPosition);
            System.out.println("viewPager.setCurrentItem(bannerPosition):---" + bannerPosition);


        }

        ;
    };

    public class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {

                if (isStop==1) {
                    handler.obtainMessage().sendToTarget();
                }
            }
        }
    }

    private void addDynamicView() {
        // 鍔ㄦ�佹坊鍔犲浘鐗囧拰涓嬮潰鎸囩ず鐨勫渾鐐�

        // 鍒濆鍖栧浘鐗囪祫婧�
        // ImageView imageView0=new ImageView(getActivity());
        // ImageLoader.getInstance().displayImage(bannerList.get(0),
        // imageView0);
        // imageView0.setScaleType(ScaleType.CENTER_CROP);
        for (int i = 0; i < bannerAdapter.count(); i++) {
            final int a = i;

            if (onclickLisenner != null) {
                bannerAdapter.getView(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onclickLisenner.Onclick(a);
                    }
                });
            }

            views.add(bannerAdapter.getView(i));

        }
       if (bannerAdapter.count()<4)
       {
           for (int i = 0; i < bannerAdapter.count()*2; i++) {
               final int a = i;

               if (onclickLisenner != null) {
                   bannerAdapter.getView(i).setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           onclickLisenner.Onclick(a);
                       }
                   });
               }

               views.add(bannerAdapter.getView(i%bannerAdapter.count()));

           }
       }
        else
       {
           for (int i = 0; i < bannerAdapter.count(); i++) {
               final int a = i;

               if (onclickLisenner != null) {
                   bannerAdapter.getView(i).setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           onclickLisenner.Onclick(a);
                       }
                   });
               }

               views.add(bannerAdapter.getView(i));

           }
       }
        // ImageView imageViewFinal=new ImageView(getActivity());
        // ImageLoader.getInstance().displayImage(bannerList.get(0),
        // imageViewFinal);
        // imageViewFinal.setScaleType(ScaleType.CENTER_CROP);
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // System.out.println("onPageScrolled:---"+arg2);
        }

        @Override
        public void onPageSelected(int position) {
            System.out.println("onpageselected:---" + position);

                currentItem = position % (banners.size());


            bannerPosition = position;

            System.out.println("bannerPosition:---" + position);
            dots.get(oldPosition).setBackgroundResource(R.drawable.check_false);
            dots.get(currentItem).setBackgroundResource(R.drawable.check_true);
            oldPosition = currentItem;
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("instantiateItem--" + position);
            position %= views.size();
            if (position < 0) {
                position = views.size() + position;
            }

            View iv = views.get(position);
            ViewParent vp = iv.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(iv);
            }
            container.addView(iv);

            // 鍦ㄨ繖涓柟娉曢噷闈㈣缃浘鐗囩殑鐐瑰嚮浜嬩欢
            iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 澶勭悊璺宠浆閫昏緫
                    // Intent intent = new Intent();
                    // intent.setClass(getActivity(), ProductActivity.class);
                    // startActivity(intent);

                }
            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // ((ViewPager) arg0).removeView((View) arg2);
            System.out.println("destroyItem---" + arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }

    public void addDian() {


        for (int i = 0; i < banners.size(); i++)

        {
            View view = new View(context);
            view.setBackground(context.getResources().getDrawable(
                    R.drawable.dot_normal));
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                    20, 20);
            //
            linearParams.leftMargin = 2;
            linearParams.rightMargin = 2;
            view.setLayoutParams(linearParams);
            dots.add(view);
            dinLin.addView(view);


        }

    }

    public interface OnclickLisenner {
        void Onclick(int position);

    }
}
