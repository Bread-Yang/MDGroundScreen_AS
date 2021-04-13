package org.mdground.api.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.mdground.api.BuildConfig;

public class DebugUtil {

    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void toast(Context context, String content) {

        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

    }



    public static void v(String tag,String msg) {

        if (DEBUG) {

            Log.v(tag, msg);

        }

    }



    public static void d(String tag,String msg) {

        if (DEBUG) {

            Log.d(tag, msg);

        }

    }



    public static void i(String tag,String msg) {

        if (DEBUG) {

            Log.i(tag, msg);

        }

    }



    public static void w(String tag,String msg) {

        if (DEBUG) {

            Log.w(tag, msg);

        }

    }



    public static void e(String tag,String msg) {

        if (DEBUG) {

            Log.e(tag, msg);

        }

    }

}
