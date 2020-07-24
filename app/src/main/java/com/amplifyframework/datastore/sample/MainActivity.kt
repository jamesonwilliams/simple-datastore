package com.amplifyframework.datastore.sample

import android.os.Bundle
import android.view.View.OnClickListener
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
        val auth = RxAmplifyFactory.auth(applicationContext)
        val localInteractor: PostInteractor = LocalDataStoreInteractor(auth, dataStore)
        localPresenter = LocalPresenter(localInteractor, localView, applicationContext.resources)

        val remoteView: RemotePresentation.View = this
        val appSync = RxAmplifyFactory.appSync(applicationContext)
        val remoteInteractor: ApiInteractor = RemoteApiInteractor(appSync)
        remotePresenter = RemotePresenter(remoteInteractor, remoteView)
    }

    private fun bindButtons() {
        val buttonActions = mutableMapOf(
            Pair(id.create_local, OnClickListener { localPresenter.createLocalItems() }),
            Pair(id.update_local, OnClickListener { localPresenter.updateLocalItems() }),
            Pair(id.delete_local, OnClickListener { localPresenter.deleteLocalItems() }),
            Pair(id.query_local, OnClickListener { localPresenter.listLocalItems() }),
            Pair(id.clear_local, OnClickListener { localPresenter.clearLocalLog() })
        )
        if (!Landscape.isLandscape(this)) {
            buttonActions.putAll(mapOf(
                Pair(id.stop_everything, OnClickListener { localPresenter.stopAllLocalActivities() }),
                Pair(id.begin_subscription, OnClickListener { localPresenter.startSubscription() }),
                Pair(id.sign_in, OnClickListener { localPresenter.signIn() })
            ))
        } else {
            buttonActions.putAll(mapOf(
                Pair(id.create_remote, OnClickListener { remotePresenter.createRemotePost() }),
                Pair(id.update_remote, OnClickListener { remotePresenter.updateRemotePosts() }),
                Pair(id.delete_remote, OnClickListener { remotePresenter.deleteRemotePosts() }),
                Pair(id.query_remote, OnClickListener { remotePresenter.listRemotePosts() }),
                Pair(id.clear_remote, OnClickListener { remotePresenter.clearRemoteLogs() })
            ))
        }
        for ((id, action) in buttonActions) {
            findViewById<android.view.View>(id).setOnClickListener(action)
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
