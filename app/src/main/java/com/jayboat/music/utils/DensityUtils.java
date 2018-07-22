package com.jayboat.music.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Hosigus on 2018/7/20.
 */

public class DensityUtils {

    private static int screenWidth  = 0;
    private static int screenHeight = 0;

    public static void translucentStatusBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            Point size = new Point();
            if (wm != null) {
                display = wm.getDefaultDisplay();
                display.getSize(size);
            }
            screenHeight = size.y;
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            Point size = new Point();
            if (wm != null) {
                display = wm.getDefaultDisplay();
                display.getSize(size);
            }
            screenWidth = size.x;
        }
        return screenWidth;
    }
}
