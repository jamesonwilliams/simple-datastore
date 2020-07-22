package com.amplifyframework.datastore.sample

import android.content.Context
import android.content.res.Configuration

internal object Landscape {
    fun isLandscape(context: Context): Boolean {
        return when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> true
            Configuration.ORIENTATION_PORTRAIT, Configuration.ORIENTATION_UNDEFINED -> false
            else -> false
        }
    }
}
