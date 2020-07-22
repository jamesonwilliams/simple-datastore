package com.amplifyframework.datastore.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;

final class Landscape {
    private Landscape() {}

    @SuppressLint("SwitchIntDef") // It's for a deprecated value 🙄.
    static boolean isLandscape(Context context) {
        switch (context.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                return true;
            case Configuration.ORIENTATION_PORTRAIT:
            case Configuration.ORIENTATION_UNDEFINED:
            default:
                return false;
        }
    }
}
