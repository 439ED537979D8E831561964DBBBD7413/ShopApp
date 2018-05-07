package com.yj.shopapp.ui.activity;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * Created by LK on 2017/10/15.
 */

public class ShowLog {
    static String className;//文件名
    static String methodName;//方法名
    static int lineNumber;//行数

    //   static string threadName  = Thread.currentThread().getName();//线程名字，能判断主线程还是子线程
    public ShowLog() {

    }

    private static Boolean isDebug = null;

    public static boolean isDebuggable() {
        return isDebug == null ? false : isDebug.booleanValue();
    }

    public static void syncIsDebug(Context context) {
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    //各种Log打印
    public static void e(String msg) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(msg));

    }

    public static void i(String msg) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(msg));

    }

    public static void d(String msg) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(msg));

    }

    public static void w(String msg) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(msg));

    }

    public static void wtf(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

}
