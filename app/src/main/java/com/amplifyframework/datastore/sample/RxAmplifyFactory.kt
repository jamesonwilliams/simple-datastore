package com.amplifyframework.datastore.sample

import android.annotation.SuppressLint
import android.content.Context

import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.amplifyframework.logging.AndroidLoggingPlugin
import com.amplifyframework.logging.LogLevel.VERBOSE
import com.amplifyframework.rx.RxAmplify
import com.amplifyframework.rx.RxAuthCategoryBehavior
import com.amplifyframework.rx.RxDataStoreCategoryBehavior

internal object RxAmplifyFactory {
    private var initialized: Boolean = false
    private lateinit var dataStore: RxDataStoreCategoryBehavior
    private lateinit var auth: RxAuthCategoryBehavior
    private lateinit var appSync: RxAppSyncClient

    @SuppressLint("CheckResult")
    @Synchronized
    private fun initialize(context: Context) {
        context.deleteDatabase("AmplifyDatastore.db")
        RxAmplify.addPlugin(AndroidLoggingPlugin(VERBOSE))
        RxAmplify.addPlugin(AWSApiPlugin())
        RxAmplify.addPlugin(AWSDataStorePlugin())
        RxAmplify.addPlugin(AWSCognitoAuthPlugin())
        RxAmplify.configure(context)
        dataStore = RxAmplify.DataStore
        auth = RxAmplify.Auth
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

    fun auth(context: Context): RxAuthCategoryBehavior {
        if (!initialized) {
            initialize(context)
        }
        return auth
    }
}

