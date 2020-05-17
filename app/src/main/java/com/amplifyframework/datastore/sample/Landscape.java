package com.amplifyframework.datastore.sample;

import android.content.Context;
import android.content.res.Configuration;

final class Landscape {
    private Landscape() {}

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
