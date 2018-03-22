package com.yj.shopapp.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * Author: raheel.arif@confiz.com Date: 10/30/13
 */
public class FileUtils {



    /**
     * 判断是否有SD卡
     * @return 有：true 没有：false
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取sd卡路径 双sd卡时，获得的是外置sd卡
     *
     * @return 外置sd卡路径
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        BufferedInputStream in = null;
        BufferedReader inBr = null;
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            in = new BufferedInputStream(p.getInputStream());
            inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息

                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                }
            }
        } catch (Exception e) {

            // return Environment.getExternalStorageDirectory().getPath();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                if (inBr != null) {
                    inBr.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 计算空间方法csize
     * */
    public static String filesize(String spath) {

        File path = new File(spath);
        StatFs statfs = new StatFs(path.getPath());
        //获取block的SIZE
        long blocSize = statfs.getBlockSize();
        //获取BLOCK数量
        long totalBlocks = statfs.getBlockCount();
        //空闲的Block的数量
        long availaBlock = statfs.getAvailableBlocks();

        //计算总空间大小和空闲的空间大小
        //string[] total = filesize(totalBlocks * blocSize);
        String availale = filesize(availaBlock * blocSize);
        return "";
    }

    /**
     * 计算空间方法csize
     * */
    public static String filesize(long size) {
        String str = "";
        if (size >= 1024) {
            str = "KB";
            size /= 1024;
            if (size >= 1024) {
                str = "MB";
                size /= 1024;
            }
        }
        DecimalFormat formatter = new DecimalFormat();
        formatter.setGroupingSize(3);
        return formatter.format(size)+str;
    }

    public static boolean Availfilesize(String spath) {
        boolean b = false;
        File path = new File(spath);
        StatFs statfs = new StatFs(path.getPath());
        //获取block的SIZE
        long blocSize = statfs.getBlockSize();
        //获取BLOCK数量
        long totalBlocks = statfs.getBlockCount();
        //空闲的Block的数量
        long availaBlock = statfs.getAvailableBlocks();

        long size = availaBlock * blocSize;
        if (size >= (1024*1024*1)) {
            b = true;
        }else{
            b = false;
        }
        return b;
    }


    /**
     * 复制文件
     *
     * @param sourceLocation 源文件
     * @param targetLocation 目标文件
     * @throws IOException
     *             Create at 2014-8-21 上午10:47:55
     */
    public static void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectory(new File(sourceLocation, children[i]), new File(
                        targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }

    /**
     * 删除指定文件夹下所有文件 param path 文件夹完整绝对路径
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件

                File myFilePath = new File(path + "/" + tempList[i]);
                myFilePath.delete(); // 再删除空文件夹

                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除指定路径的文件
     */
    public static void  deleteFile(String path)
    {
        if(path.trim().length()==0)
            return;
        try{
            File file = new File(path);
            if(getSDCardPath()!=null)
            {
                if (file.exists())
                {
                    file.delete();
                }
            }
        }catch(Exception e){}
    }

    public static void save(String fileName, String content) throws Exception {

        // 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }

        byte[] buf = fileName.getBytes("iso8859-1");


        fileName = new String(buf,"utf-8");

        // Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
        // Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
        // Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
        // MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
        // 如果希望文件被其他应用读和写，可以传入：
        // openFileOutput("output.txt", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);

        FileOutputStream fos =  new FileOutputStream(fileName);
        fos.write(content.getBytes());
        fos.close();
    }


    public static String createSDFile(String fileName)  {
        File file = null;
        try {
            file = new File(getSDCardPath() + "//" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        }catch (Exception ex){}
        return file.getPath();
    }

    /**
     * 写入内容到SD卡中的txt文本中
     * str为内容
     */
    public static void writeFileSdcard(String fileName, String message) {
        try {
            // FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}