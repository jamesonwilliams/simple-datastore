package com.amplifyframework.datastore.sample;

import android.content.Context;

import androidx.annotation.NonNull;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.logging.AndroidLoggingPlugin;
import com.amplifyframework.logging.LogLevel;
import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxDataStoreCategoryBehavior;

final class RxAmplifyFactory {
    private static boolean initialized = false;
    private static RxDataStoreCategoryBehavior dataStore = null;
    private static RxAppSyncClient appSync = null;

    private RxAmplifyFactory() {}

    private synchronized static void initialize(@NonNull Context context) {
        try {
            context.deleteDatabase("AmplifyDatastore.db");
            RxAmplify.addPlugin(new AndroidLoggingPlugin(LogLevel.VERBOSE));
            RxAmplify.addPlugin(new AWSApiPlugin());
            RxAmplify.addPlugin(new AWSDataStorePlugin());
            RxAmplify.configure(context);
        } catch (AmplifyException configurationFailure) {
            throw new RuntimeException(configurationFailure);
        }
        dataStore = RxAmplify.DataStore;
        appSync = new RxAppSyncClient(Amplify.API);
        initialized = true;
    }

    static RxDataStoreCategoryBehavior dataStore(Context context) {
        if (!initialized) {
            initialize(context);
        }
        return dataStore;
    }

    static RxAppSyncClient appSync(Context context) {
        if (!initialized) {
            initialize(context);
        }
        return appSync;
    }
}
