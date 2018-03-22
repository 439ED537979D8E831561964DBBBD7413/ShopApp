package com.yj.shopapp.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.widget.Toast;

import com.yj.shopapp.config.Contants;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

public class PhotoUtil {

    public static String rootDir = Environment.getExternalStorageDirectory()
            + File.separator + "yjApp" + File.separator;

    public static String fileName = "";

    // 调用系统照相机的方法
    public static File camera(Activity ac) {
        /*
		 * Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
		 * startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
		 */
        File f = null;
        String name = "";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                name = callTime();
                File dir = new File(rootDir);
                if (!dir.exists())
                    dir.mkdirs();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(ac.getPackageManager()) != null) {
                    f = new File(dir, name + ".jpeg");// localTempImgDir和localTempImageFileName是自己定义的名字
                    Uri u = null;
                    if (f != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            u = FileProvider.getUriForFile(ac, "com.yj.shopapp.fileProvider", f);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }else {
                            u = Uri.fromFile(f);
                        }
                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);

                    }
                }

                fileName = f.getAbsolutePath();
                ac.startActivityForResult(intent, Contants.Photo.REQUEST_PHOTO_CODE);
            } catch (ActivityNotFoundException e) {
                // TODO Auto-generated catch block
                Toast.makeText(ac, "没有找到储存目录", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ac, "没有储存卡", Toast.LENGTH_SHORT).show();
        }
        return f;
    }

    public static String callTime() {

        long backTime = new Date().getTime();

        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date(backTime));

        int year = cal.get(Calendar.YEAR);

        int month = cal.get(Calendar.MONTH) + 1;

        int date = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        int minute = cal.get(Calendar.MINUTE);

        int second = cal.get(Calendar.SECOND);

        String time = "" + year + month + date + hour + minute + second;

        return time;
    }


    /**
     * 打开本地图库
     */
    public static void OpenFinder(Activity ac) {
        Intent innerIntent = null;
        Intent wrapperIntent = null;
        if (Build.VERSION.SDK_INT < 19) {
            innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            innerIntent.setType("image/*");
            wrapperIntent = Intent.createChooser(innerIntent, "选择图片");
        } else {
            innerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            innerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            wrapperIntent = Intent.createChooser(innerIntent, "选择图片");
        }
        ac.startActivityForResult(wrapperIntent, Contants.Photo.REQUEST_FILE_CODE);

    }

    public static String getPhotoPath(Activity ac, Intent data) {
        String path = null;
        Uri selectedImage = data.getData();
        if (null == selectedImage) return null;

        final String scheme = selectedImage.getScheme();


        if (scheme == null)
            path = selectedImage.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = selectedImage.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = ac.getContentResolver().query(selectedImage, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        path = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return path;
    }

}
