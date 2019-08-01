package com.shine.seriablebedlib;

import android.content.Context;

/**
 * Created by louisgeek on 2018/9/20.
 */
public class LibraryProvider {
    private static Context mAppContext;
    private static boolean mDebug;
    public static void init(Context context, boolean debug) {
        mAppContext = context.getApplicationContext();
        mDebug = debug;
    }

    public static Context provideAppContext() {
        return mAppContext;
    }

    public static boolean provideDebug() {
        return mDebug;
    }

}
