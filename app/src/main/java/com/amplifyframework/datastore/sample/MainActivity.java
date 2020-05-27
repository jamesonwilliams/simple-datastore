package com.amplifyframework.datastore.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.rx.RxDataStoreCategoryBehavior;

public class MainActivity extends AppCompatActivity
        implements LocalPresentation.View, RemotePresentation.View {
    private LocalPresentation.Presenter localPresenter;
    private RemotePresentation.Presenter remotePresenter;
    private LogWindow localLogWindow;
    private LogWindow remoteLogWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindButtons();
        bindLogWindows();

        Context context = getApplicationContext();

        LocalPresentation.View localView = this;
        RxDataStoreCategoryBehavior dataStore = RxAmplifyFactory.dataStore(context);
        LocalPresentation.PostInteractor localInteractor = new LocalDataStoreInteractor(dataStore);
        this.localPresenter = new LocalPresenter(localInteractor, localView);

        RemotePresentation.View remoteView = this;
        RxAppSyncClient appSync = RxAmplifyFactory.appSync(context);
        RemotePresentation.ApiInteractor remoteInteractor = new RemoteApiInteractor(appSync);
        this.remotePresenter = new RemotePresenter(remoteInteractor, remoteView);
    }

    private void bindButtons() {
        findViewById(R.id.create_local).setOnClickListener(v -> localPresenter.createLocalItems());
        findViewById(R.id.update_local).setOnClickListener(v -> localPresenter.updateLocalItems());
        findViewById(R.id.delete_local).setOnClickListener(v -> localPresenter.deleteLocalItems());
        findViewById(R.id.query_local).setOnClickListener(v -> localPresenter.listLocalItems());
        findViewById(R.id.clear_local).setOnClickListener(v -> localPresenter.clearLocalLog());
        if (!Landscape.isLandscape(this)) {
            findViewById(R.id.stop_everything).setOnClickListener(v -> localPresenter.stopAllLocalActivities());
            findViewById(R.id.begin_subscription).setOnClickListener(v -> localPresenter.startSubscription());
        } else {
            findViewById(R.id.create_remote).setOnClickListener(v -> remotePresenter.createRemotePost());
            findViewById(R.id.update_remote).setOnClickListener(v -> remotePresenter.updateRemotePosts());
            findViewById(R.id.delete_remote).setOnClickListener(v -> remotePresenter.deleteRemotePosts());
            findViewById(R.id.query_remote).setOnClickListener(v -> remotePresenter.listRemotePosts());
            findViewById(R.id.clear_remote).setOnClickListener(v -> remotePresenter.clearRemoteLogs());
        }
    }

    private void bindLogWindows() {
        ViewGroup parent = findViewById(R.id.content);
        this.localLogWindow = new LogWindow(parent, R.id.local_log);
        if (Landscape.isLandscape(this)) {
            this.remoteLogWindow = new LogWindow(parent, R.id.remote_log);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void displayLocalLogLine(LogLine logLine) {
        runOnUiThread(() -> localLogWindow.add(logLine));
    }

    @Override
    public void clearLocalLineItems() {
        runOnUiThread(() -> localLogWindow.clear());
    }

    @Override
    public void displayRemoteLogLine(LogLine logLine) {
        runOnUiThread(() -> remoteLogWindow.add(logLine));
    }

    @Override
    public void clearRemoteLineItems() {
        runOnUiThread(() -> remoteLogWindow.clear());
    }
}
