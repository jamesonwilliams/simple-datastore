package com.amplifyframework.datastore.sample;

import android.util.Pair;

import com.amplifyframework.datastore.generated.model.Post;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

interface LocalPresentation {
    interface View {
        void displayLocalLogLine(LogLine logLine);
        void clearLocalLineItems();
    }

    interface Presenter {
        void createLocalItems();
        void updateLocalItems();
        void listLocalItems();
        void deleteLocalItems();
        void startSubscription();
        void clearLocalLog();
        void stopAllLocalActivities();
    }

    interface PostInteractor {
        Single<Post> createRandom();
        Single<List<Post>> updateAll(List<Post> posts);
        Single<List<Post>> list();
        Completable deleteAll(List<Post> posts);
        Observable<Pair<Post, Modification>> subscribe();
    }
}
