package com.amplifyframework.datastore.sample

import android.os.Bundle
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity

import com.amplifyframework.datastore.sample.LocalPresentation.PostInteractor
import com.amplifyframework.datastore.sample.LocalPresentation.Presenter
import com.amplifyframework.datastore.sample.LocalPresentation.View
import com.amplifyframework.datastore.sample.R.id
import com.amplifyframework.datastore.sample.R.layout
import com.amplifyframework.datastore.sample.RemotePresentation.ApiInteractor

class MainActivity : AppCompatActivity(), View, RemotePresentation.View {
    private lateinit var localPresenter: Presenter
    private lateinit var remotePresenter: RemotePresentation.Presenter
    private lateinit var localLogWindow: LogWindow
    private lateinit var remoteLogWindow: LogWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        bindButtons()
        bindLogWindows()

        val localView: View = this
        val dataStore = RxAmplifyFactory.dataStore(applicationContext)
        val localInteractor: PostInteractor = LocalDataStoreInteractor(dataStore)
        localPresenter = LocalPresenter(localInteractor, localView)

        val remoteView: RemotePresentation.View = this
        val appSync = RxAmplifyFactory.appSync(applicationContext)
        val remoteInteractor: ApiInteractor = RemoteApiInteractor(appSync)
        remotePresenter = RemotePresenter(remoteInteractor, remoteView)
    }

    private fun bindButtons() {
        findViewById<android.view.View>(id.create_local).setOnClickListener { localPresenter.createLocalItems() }
        findViewById<android.view.View>(id.update_local).setOnClickListener { localPresenter.updateLocalItems() }
        findViewById<android.view.View>(id.delete_local).setOnClickListener { localPresenter.deleteLocalItems() }
        findViewById<android.view.View>(id.query_local).setOnClickListener { localPresenter.listLocalItems() }
        findViewById<android.view.View>(id.clear_local).setOnClickListener { localPresenter.clearLocalLog() }
        if (!Landscape.isLandscape(this)) {
            findViewById<android.view.View>(id.stop_everything).setOnClickListener { localPresenter.stopAllLocalActivities() }
            findViewById<android.view.View>(id.begin_subscription).setOnClickListener { localPresenter.startSubscription() }
        } else {
            findViewById<android.view.View>(id.create_remote).setOnClickListener { remotePresenter.createRemotePost() }
            findViewById<android.view.View>(id.update_remote).setOnClickListener { remotePresenter.updateRemotePosts() }
            findViewById<android.view.View>(id.delete_remote).setOnClickListener { remotePresenter.deleteRemotePosts() }
            findViewById<android.view.View>(id.query_remote).setOnClickListener { remotePresenter.listRemotePosts() }
            findViewById<android.view.View>(id.clear_remote).setOnClickListener { remotePresenter.clearRemoteLogs() }
        }
    }

    private fun bindLogWindows() {
        val parent = findViewById<ViewGroup>(id.content)
        localLogWindow = LogWindow(parent, id.local_log)
        if (Landscape.isLandscape(this)) {
            remoteLogWindow = LogWindow(parent, id.remote_log)
        }
    }

    override fun displayLocalLogLine(logLine: LogLine) = runOnUiThread { localLogWindow.add(logLine) }

    override fun clearLocalLineItems() = runOnUiThread { localLogWindow.clear() }

    override fun displayRemoteLogLine(logLine: LogLine) = runOnUiThread { remoteLogWindow.add(logLine) }

    override fun clearRemoteLineItems() = runOnUiThread { remoteLogWindow.clear() }
}
