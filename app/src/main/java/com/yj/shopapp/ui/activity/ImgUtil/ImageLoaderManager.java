package com.yj.shopapp.ui.activity.ImgUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.yj.shopapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片加载工具，功能：基于一个图片网址，检测内存是否有图像有则显示无则到本地去检测
 * ，本地有则从本地加载，本地没有则从网络加载
 */

public class ImageLoaderManager {
    private static final int MSG_OK = 1;
    private static final int MSG_FAIL = 2;
    //内存中存储对象url--Bitmap
    private LruCache<String, Bitmap> cache;
    //本地缓存地址
    private String mLocalCachePath;
    //下载线程池
    private ExecutorService mExecutor;
    //记录当前要下载图像的ImageView
    private ArrayList<ImageView> mLodingImageList;

    private ImageLoaderManager(Context context) {
        // 获取系统分配给应用的最大空间的1/8
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        //初始化内存缓存空间
        cache = new LruCache<String, Bitmap>(maxSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                //返回一个Bitmap对象所占的byte数
                return value.getRowBytes() * value.getHeight();
            }
        };
        //  /mnt/sdcard/abc.mp4    /mnt/sdcard/aaa/video/abc.mp4
        //   /mnt/sdcard/abc.mp4  -->/mnt/sdcard/Android/包名/cache/mntsdcardabcmp4
        //初始化本地缓存路径 /mnt/sdcard/Android/包名/cache/
        mLocalCachePath = context.getExternalCacheDir().toString();
        //初始化下载线程池
        mExecutor = Executors.newCachedThreadPool();
        mLodingImageList = new ArrayList<>();
    }

    private static ImageLoaderManager instance;

    public static ImageLoaderManager newInstance(Context context) {
        if (null == instance) {
            instance = new ImageLoaderManager(context);
        }
        return instance;
    }

    /**
     * 显示图片
     *
     * @param url
     * @param iv
     */
    public void showImage(String url, ImageView iv) {
        int w = iv.getMeasuredWidth();
        int h = iv.getMeasuredHeight();
        if (w <= 0) {
            w = 200;
        }
        if (h <= 0) {
            h = 200;
        }
        showImage(url, iv, w, h);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param iv
     * @param width  显示的最大宽度
     * @param height 显示的最大高度
     */
    public void showImage(String url, ImageView iv, int width, int height) {
        Bitmap b = getFromCacheOrLocal(url, width, height);
        if (b != null) {
            iv.setImageBitmap(b);
        } else {
            iv.setTag(url);
            //网络加载
            //检测当前url是否又在下载中-->约定使用下载的url作为ImageView的标记
            if (!isLoading(url)) {
                //下载
                mExecutor.execute(new DownloadRunnable(url, width, height));
            }
            //记录要显示
            mLodingImageList.add(iv);
        }
    }

    //是否有正在加载
    private boolean isLoading(String url) {
        if (!mLodingImageList.isEmpty()) {
            for (ImageView iv : mLodingImageList) {
                if (url.equals(iv.getTag())) {
                    return true;
                }
            }
        }
        return false;
    }

    private class DownloadRunnable extends Handler implements Runnable {
        String url;
        int width;
        int height;

        DownloadRunnable(String url, int width, int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }

        @Override
        public void run() {
            try {
                URL link = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) link.openConnection();
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    checkCacheFile();
                    //保存到本地
                    String path = mLocalCachePath + "/" + getNameFromHttpUrl(url);
                    FileOutputStream out = new FileOutputStream(path);
                    byte[] buffer = new byte[1024];
                    int num;
                    while ((num = in.read(buffer)) != -1) {
                        out.write(buffer, 0, num);
                    }
                    out.flush();
                    out.close();
                    in.close();
                    Bitmap b = BitmapUtil.decodeBitmap(path, width, height);
                    //显示
                    obtainMessage(MSG_OK, b).sendToTarget();
                    //保存到内存中
                    cache.put(url, b);
                } else {
                    sendEmptyMessage(MSG_FAIL);
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_OK:
                    Bitmap b = (Bitmap) msg.obj;
                    //找到对应的ImageView显示图片
                    int index = 0;
                    while (index < mLodingImageList.size()) {
                        ImageView iv = mLodingImageList.get(index);
                        if (url.equals(iv.getTag())) {
                            iv.setImageBitmap(b);
                            mLodingImageList.remove(iv);
                        } else {
                            index++;
                        }
                    }
                    break;
                case MSG_FAIL:
                    //找到对应的ImageView显示图片
                    int i = 0;
                    while (i < mLodingImageList.size()) {
                        ImageView iv = mLodingImageList.get(i);
                        if (url.equals(iv.getTag())) {
//                            iv.setImageBitmap(b);  //显示默认图片
                            mLodingImageList.remove(iv);
                        } else {
                            i++;
                        }
                    }
                    break;
            }
        }
    }

    // http://asasas.jpg  -- Bitmap
    private Bitmap getFromCacheOrLocal(String url, int width, int height) {
        if (cache.get(url) != null) {
            //从内存中取出
            return cache.get(url);
        } else {
            //从本地取,http://192.168.18.250:8080/AskMeServer/photo/1496391122607.jpg -->
            // 路径 /mnt/sdcard/Android/data/应用包名/cache/photo1496391122607jpg
            String path = mLocalCachePath + "/" + getNameFromHttpUrl(url);
            File f = new File(path);
            if (f.exists()) {
                //从本地转换为Bitmap
                Bitmap b = BitmapUtil.decodeBitmap(path, width, height);
                if (null != b) {
                    //存到内存
                    cache.put(url, b);
                    return b;
                }
            }
            return null;
        }
    }

    //从网络地址中获取文件名（存储在本地缓存文件夹中的文件名）
    private String getNameFromHttpUrl(String url) {
        int index = url.indexOf("AskMeServer");
        String name = url.substring(index + 11); // /photo/1496391122607.jpg
        //替换特殊字符
        name = name.replaceAll("[^\\w]", ""); //  photo1496391122607jpg
        return name;
    }

    /**
     * 检测并创建缓存文件夹
     */
    private void checkCacheFile() {
        File f = new File(mLocalCachePath);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    //检测缩略图是否存在
    public boolean isThumbExist(String path) {
        if (cache.get(path) != null) {
            return true;
        } else {
            // /mnt/sdcard/abc.mp4  -->/mnt/sdcard/Android/包名/cache/mntsdcardabcmp4
            //检测是否是缓存路径，是则表示存在
            if (new File(path).getParent().equals(mLocalCachePath)) {
                return true;
            }
            String thumbPath = mLocalCachePath + "/" + getNameFromLocalPath(path);
            File f = new File(thumbPath);
            if (f.exists()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 保存缩略图
     *
     * @param path
     * @param thumb
     */
    public void saveLocalThumb(String path, Bitmap thumb) {
        String thumbPath = mLocalCachePath + "/" + getNameFromLocalPath(path);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(thumbPath);
            thumb.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            cache.put(path, thumb);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 显示本地文件缩略图
     *
     * @param path
     * @param iv
     */
    public void showLocalImage(String path, ImageView iv) {
        int w = iv.getMeasuredWidth();
        int h = iv.getMeasuredHeight();
        if (w <= 0) {
            w = 200;
        }
        if (h <= 0) {
            h = 200;
        }
        showLocalImage(path, iv, w, h);
    }

    /**
     * 显示本地文件缩略图（二级缓存）
     *
     * @param path   文件路径
     * @param iv     图片视图
     * @param width  最大宽度
     * @param height 最大高度
     */
    public void showLocalImage(String path, ImageView iv, int width, int height) {
        Bitmap bitmap = null;
        if (cache.get(path) != null) {
            //从内存中取出
            bitmap = cache.get(path);
        } else {
            String thumbPath;
            //检测原路径是否是缓存路径 //mnt/sdcard/temp/123.jpg
            if (new File(path).getParent().equals(mLocalCachePath)) {
                thumbPath = path;
            } else {
                //从本地取
                thumbPath = mLocalCachePath + "/" + getNameFromLocalPath(path);
            }
            File f = new File(thumbPath);
            if (f.exists()) {
                //从本地转换为Bitmap
                Bitmap b = BitmapUtil.decodeBitmap(thumbPath, width, height);
                if (null != b) {
                    //存到内存
                    cache.put(path, b);
                    bitmap = b;
                }
            }
        }
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        } else {
            iv.setImageResource(R.drawable.ic_launcher);
        }
    }


    /**
     * 从本地路径中获取临时文件名 //  /mnt/sdcard/abc.mp4  --> mntsdcardabcmp4
     *
     * @param path 本地文件路径
     * @return
     */
    private String getNameFromLocalPath(String path) {
        return path.replaceAll("[^\\w]", "");
    }

}
