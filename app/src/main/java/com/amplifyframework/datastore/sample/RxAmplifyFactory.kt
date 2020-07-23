package com.amplifyframework.datastore.sample

import android.content.Context

import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.amplifyframework.logging.AndroidLoggingPlugin
import com.amplifyframework.logging.LogLevel.VERBOSE
import com.amplifyframework.rx.RxAmplify
import com.amplifyframework.rx.RxDataStoreCategoryBehavior

internal object RxAmplifyFactory {
    private var initialized: Boolean = false
    private lateinit var dataStore: RxDataStoreCategoryBehavior
    private lateinit var appSync: RxAppSyncClient

    @Synchronized
    private fun initialize(context: Context) {
        try {
            context.deleteDatabase("AmplifyDatastore.db")
            RxAmplify.addPlugin(AndroidLoggingPlugin(VERBOSE))
            RxAmplify.addPlugin(AWSApiPlugin())
            RxAmplify.addPlugin(AWSDataStorePlugin())
            RxAmplify.configure(context)
        } catch (configurationFailure: AmplifyException) {
            throw RuntimeException(configurationFailure)
        }
        dataStore = RxAmplify.DataStore
        appSync = RxAppSyncClient(Amplify.API)
        initialized = true
    }

    fun dataStore(context: Context): RxDataStoreCategoryBehavior {
        if (!initialized) {
            initialize(context)
        }
        return dataStore
    }

    fun appSync(context: Context): RxAppSyncClient {
        if (!initialized) {
            initialize(context)
        }
        return appSync
    }
}
