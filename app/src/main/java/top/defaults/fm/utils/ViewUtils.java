package top.defaults.fm.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.Locale;

/**
 * @author duanhong
 * @version 1.0, 9/9/16 11:36 AM
 */
public class ViewUtils {
    private static Toast toast;

    // Show a short toast shortcut.
    public static void showToast(Context c, int message) {
        if (c == null) return;
        showToast(c, c.getString(message));
    }

    // Show a short toast shortcut.
    public static void showToast(Context c, String message) {
        if (c == null) return;
        if (toast == null) {
            toast = Toast.makeText(c, message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private static final int ONE_HOUR = 60 * 60 * 1000;
    private static final int ONE_MIN = 60 * 1000;
    private static final int ONE_SECOND = 1000;

    public static String formatTime(long ms) {
        StringBuilder sb = new StringBuilder();
        int hour = (int) (ms / ONE_HOUR);
        int min = (int) ((ms % ONE_HOUR) / ONE_MIN);
        int sec = (int) (ms % ONE_MIN) / ONE_SECOND;
        if (hour != 0) {
            if (hour < 10) {
                sb.append("0").append(hour).append(":");
            } else {
                sb.append(hour).append(":");
            }
        }

        if (min == 0) {
            sb.append("00:");
        } else if (min < 10) {
            sb.append("0").append(min).append(":");
        } else {
            sb.append(min).append(":");
        }
        if (sec == 0) {
            sb.append("00");
        } else if (sec < 10) {
            sb.append("0").append(sec);
        } else {
            sb.append(sec);
        }
        return sb.toString();
    }

    public static String formatCount(long count) {
        if (count > 100000) {
            String intPart = "" + (count / 10000);
            String decimal = "" + ((count % 10000) / 1000);
            return String.format(Locale.US, "%s.%s万", intPart, decimal);
        }

        return "" + count;
    }
}
