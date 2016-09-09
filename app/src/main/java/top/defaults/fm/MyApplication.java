package top.defaults.fm;

import android.app.Application;

import top.defaults.fm.utils.ImageUtils;

/**
 * @author duanhong
 * @version 1.0, 9/9/16 11:14 AM
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageUtils.initializeFresco(getApplicationContext());
    }
}
