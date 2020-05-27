package com.amplifyframework.datastore.sample;

import com.amplifyframework.datastore.appsync.ModelWithMetadata;
import com.amplifyframework.datastore.generated.model.Post;

import io.reactivex.Observable;
import io.reactivex.Single;

interface RemotePresentation {
    interface View {
        void displayRemoteLogLine(LogLine logLine);
        void clearRemoteLineItems();
    }

    interface Presenter {
        void createRemotePost();
        void updateRemotePosts();
        void deleteRemotePosts();
        void listRemotePosts();
        void clearRemoteLogs();
    }

    interface ApiInteractor {
        Single<ModelWithMetadata<Post>> createRandom();
        Single<ModelWithMetadata<Post>> update(ModelWithMetadata<Post> post);
        Single<ModelWithMetadata<Post>> delete(ModelWithMetadata<Post> post);
        Observable<ModelWithMetadata<Post>> list();
    }
}
