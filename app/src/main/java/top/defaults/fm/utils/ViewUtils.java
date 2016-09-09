package top.defaults.fm.utils;

import android.content.Context;
import android.widget.Toast;

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
}
