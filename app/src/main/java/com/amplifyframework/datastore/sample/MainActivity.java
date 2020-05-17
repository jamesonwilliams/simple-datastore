package com.amplifyframework.datastore.sample;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.rx.RxDataStoreCategoryBehavior;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MainActivity extends AppCompatActivity
        implements LocalPresentation.View, RemotePresentation.View {
    private LocalPresentation.Presenter localPresenter;
    private RemotePresentation.Presenter remotePresenter;
    private Logger localLog;
    private Logger remoteLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();

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

    private void bindViews() {
        setContentView(R.layout.activity_main);
        this.localLog = new Logger(Tag.LOCAL, findViewById(R.id.local_log));
        findViewById(R.id.create_local).setOnClickListener(v -> localPresenter.createLocalItems());
        findViewById(R.id.update_local).setOnClickListener(v -> localPresenter.updateLocalItems());
        findViewById(R.id.delete_local).setOnClickListener(v -> localPresenter.deleteLocalItems());
        findViewById(R.id.query_local).setOnClickListener(v -> localPresenter.listLocalItems());
        findViewById(R.id.clear_local).setOnClickListener(v -> localPresenter.clearLocalLog());

        if (!Landscape.isLandscape(this)) {
            Fullscreen.showSystemUI(getWindow());
            findViewById(R.id.stop_everything).setOnClickListener(v -> localPresenter.stopAllLocalActivities());
            findViewById(R.id.begin_subscription).setOnClickListener(v -> localPresenter.startSubscription());
        } else {
            Fullscreen.hideSystemUI(getWindow());
            this.remoteLog = new Logger(Tag.REMOTE, findViewById(R.id.remote_log));
            findViewById(R.id.create_remote).setOnClickListener(v -> remotePresenter.createRemotePost());
            findViewById(R.id.update_remote).setOnClickListener(v -> remotePresenter.updateRemotePosts());
            findViewById(R.id.delete_remote).setOnClickListener(v -> remotePresenter.deleteRemotePosts());
            findViewById(R.id.query_remote).setOnClickListener(v -> remotePresenter.listRemotePosts());
            findViewById(R.id.clear_remote).setOnClickListener(v -> remotePresenter.clearRemoteLogs());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void displayLocalText(String text) {
        localLog.log(text);
    }

    @Override
    public void clearLocalText() {
        localLog.clear();
    }

    @Override
    public void displayRemoteText(String text) {
        remoteLog.log(text);
    }

    @Override
    public void clearRemoteText() {
        remoteLog.clear();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Tag {
        String REMOTE = "datastore-demo:remote";
        String LOCAL = "datastore-demo:local";
    }
}
