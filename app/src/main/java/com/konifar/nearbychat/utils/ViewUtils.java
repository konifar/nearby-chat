package com.konifar.nearbychat.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ViewUtils {

    private static final int IMAGE_FADE_DURATION_MILLS = 200;

    private static ViewUtils instance;

    private ViewUtils() {
    }

    public static ViewUtils getInstance() {
        if (instance == null) {
            instance = new ViewUtils();
        }
        return instance;
    }

    public float dpToPx(Context context, int dimenResId) {
        float dp = context.getResources().getDimension(dimenResId);
        return dpToPx(context, dp);
    }

    private float dpToPx(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, metrics);
    }

}
