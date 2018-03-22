package com.yj.shopapp.ui.activity.ImgUtil;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {

	/**
	 * 解码图像
	 *
	 * @param source
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap decodeBitmap(byte[] source, int maxWidth, int maxHeight) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		//先取宽高
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(source, 0, source.length, opt);
		configOption(opt, maxWidth, maxHeight);
		//解码图像，要获取像素点
		opt.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeByteArray(source, 0, source.length, opt);
		return bitmap;
	}

	private static void configOption(BitmapFactory.Options opt, int maxWidth, int maxHeight) {
		int oWidth = opt.outWidth;
		int oHeight = opt.outHeight;
		int scW = (int) Math.ceil(oWidth / maxWidth);
		int scH = (int) Math.ceil(oHeight / maxHeight);
		if (scW > 1 || scH > 1) {
			if (scH > scW) {
				opt.inSampleSize = scH;  //必须要大于1，必须是整数,最终显示的是1/opt.inSampleSize
			} else {
				opt.inSampleSize = scW;
			}
		}
	}


	/**
	 * 从本地加载
	 *
	 * @param path
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap decodeBitmap(String path, int maxWidth, int maxHeight) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		//先取宽高
		opt.inJustDecodeBounds = true;
		//解析宽高(解析出的bitmap为null)
		BitmapFactory.decodeFile(path, opt);
		configOption(opt, maxWidth, maxHeight);
		//解码图像，要获取像素点
		opt.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, opt);
		return bitmap;
	}

	/**
	 * 将输入流转为byte数组
	 *
	 * @param in
	 * @return
	 */
	public static byte[] getBytes(InputStream in) {
		byte[] result = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			int num;
			while ((num = in.read(buffer)) != -1) {
				out.write(buffer, 0, num);
			}
			out.flush();
			result = out.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 *
	 * @param bitmap
	 * @param path
	 * /mnt/sdcard/small.jpg
	 */
	public static boolean saveBitmap(Bitmap bitmap, String path) {
		FileOutputStream out = null;
		boolean result = false;
		try {
			out = new FileOutputStream(path);
			bitmap.compress(CompressFormat.JPEG, 100, out);
			out.flush();
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
