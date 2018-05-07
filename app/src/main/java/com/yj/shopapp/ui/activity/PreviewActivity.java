package com.yj.shopapp.ui.activity;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegocarloslima.byakugallery.lib.GalleryViewPager;
import com.diegocarloslima.byakugallery.lib.TouchImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by jm on 2016/4/26.
 * 图片预览
 */
public class PreviewActivity extends BaseActivity {

    @BindView(R.id.fabric_image_title_tv)
    TextView fabricImageTitleTv;
    @BindView(R.id.view_pager)
    GalleryViewPager pager;

    int initPosition = 0;
    private String[] imageUrls;
    private String[] values;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("IMAGEURLS")) {
            imageUrls = getIntent().getExtras().getStringArray("IMAGEURLS");
        }
        if (getIntent().hasExtra("VALUES")) {
            values = getIntent().getExtras().getStringArray("VALUES");
        }
        initViews();
    }

    private void initViews() {
//        for(int i = 0;i<imageUrls.size();i++){
//            imageUrls.add("http://www.0739i.com.cn/data/attachment/portal/201603/09/120158ksjocrjsoohrmhtg.jpg");
//        }

        fabricImageTitleTv.setText("图片（" + (initPosition + 1) + "/" + imageUrls.length + "）");

        pager = (GalleryViewPager) findViewById(R.id.view_pager);
        pager.setOffscreenPageLimit(0);
        pager.setAdapter(new ImagePagerAdapter(imageUrls));
        pager.setCurrentItem(initPosition);

//        ImageLoader.getInstance().displayImage("file://" + mCompareImagePath, compareImageIv,
//                DisplayImageOptions.createSimple(), null);

//        Picasso.with(mContext)
//                .load(new File(mCompareImagePath)).resize(2000, 2000).centerInside().into(compareImageIv);
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private String[] images;
        private LayoutInflater inflater;

        ImagePagerAdapter(String[] images) {
            this.images = images;
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.activity_preview_item,
                    view, false);

            String sourceUrl = images[position];

//            if (!sourceUrl.startsWith("http://"))
//                sourceUrl = "file://" + sourceUrl;
//			final string sourceUrl = images.get(position);

            TouchImageView imageView2 = (TouchImageView) imageLayout
                    .findViewById(R.id.image2);
            imageView2.setMaxScale(5);

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });

            Log.e("e", sourceUrl);

//			Picasso.with(BrowserLocalImageActivity.this)
//					.load(new File(sourceUrl)).resize(960, 960).centerInside().into(imageView2);

            ImageLoader.getInstance().displayImage(sourceUrl, imageView2,
                    DisplayImageOptions.createSimple(), null);

            view.addView(imageLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }
    }

}
