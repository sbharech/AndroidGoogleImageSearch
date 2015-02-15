package com.example.suraj.androidgoogleimagesearch;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by suraj on 09/02/15.
 */
public class Utility {
    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
}
