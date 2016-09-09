package top.defaults.fm.utils;


import android.util.Log;


import java.util.Locale;

import top.defaults.fm.BuildConfig;

public final class LogUtils {
    public static String TAG = "LogUtils";
    private static boolean printLog = true;

    static {
        printLog = BuildConfig.DEBUG;
    }

    private LogUtils() {
    }

    @SuppressWarnings("unused")
    public static void setTag(String tag) {
        d("Changing log tag to %s", tag);
        TAG = tag;
    }

    public static void v(String format, Object... args) {
        if (!printLog) {
            return;
        }
        Log.v(TAG, buildMessage(format, args));
    }

    public static void d(String format, Object... args) {
        if (!printLog) {
            return;
        }
        Log.d(TAG, buildMessage(format, args));
    }

    public static void i(String format, Object... args) {
        if (!printLog) {
            return;
        }
        Log.i(TAG, buildMessage(format, args));
    }

    @SuppressWarnings("unused")
    public static void e(String format, Object... args) {
        if (!printLog) {
            return;
        }
        Log.e(TAG, buildMessage(format, args));
    }

    @SuppressWarnings("unused")
    public static void e(Throwable err, String format, Object... args) {
        if (!printLog) {
            return;
        }
        Log.e(TAG, buildMessage(format, args), err);
    }

    @SuppressWarnings("unused")
    public static void wtf(String format, Object... args) {
        if (!printLog) {
            return;
        }
        Log.wtf(TAG, buildMessage(format, args));
    }

    @SuppressWarnings("unused")
    public static void wtf(Throwable err, String format, Object... args) {
        if (!printLog) {
            return;
        }
        Log.wtf(TAG, buildMessage(format, args), err);
    }


    private static String buildMessage(String format, Object[] args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().getStackTrace();

        String caller = "<unknown>";
        String fileName;
        String methodName;
        int lineNumber;

        for (int i = 2; i < trace.length; ++i) {
            Class<? extends StackTraceElement> clazz = trace[i].getClass();
            if (!(clazz.equals(LogUtils.class))) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf(46) + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf(36) + 1);

                fileName = trace[i].getFileName();
                lineNumber = trace[i].getLineNumber();
                methodName = trace[i].getMethodName();

                caller = callingClass + "." + methodName + " (" + fileName + ":" + lineNumber + ") ";
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg
        );
    }
}

