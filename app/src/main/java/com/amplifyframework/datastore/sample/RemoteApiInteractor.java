package com.amplifyframework.datastore.sample;

import com.amplifyframework.datastore.appsync.ModelMetadata;
import com.amplifyframework.datastore.appsync.ModelWithMetadata;
import com.amplifyframework.datastore.generated.model.Post;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

final class RemoteApiInteractor implements RemotePresentation.ApiInteractor {
    private final RxAppSyncClient appSync;

    RemoteApiInteractor(RxAppSyncClient appSync) {
        this.appSync = appSync;
    }

    @Override
    public Single<ModelWithMetadata<Post>> createRandom() {
        return appSync.create(Posts.random("Remote"))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io());
    }

    @Override
    public Single<ModelWithMetadata<Post>> update(ModelWithMetadata<Post> mwm) {
        Post updatedItem = Posts.update(mwm.getModel());
        int version = extractVersion(mwm);
        return appSync.update(updatedItem, version)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io());
    }

    @Override
    public Single<ModelWithMetadata<Post>> delete(ModelWithMetadata<Post> mwm) {
        String itemId = mwm.getModel().getId();
        int version = extractVersion(mwm);
        return appSync.delete(Post.class, itemId, version)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io());
    }

    @Override
    public Observable<ModelWithMetadata<Post>> list() {
        return appSync.sync(Post.class)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io());
    }

    private int extractVersion(ModelWithMetadata<Post> mwm) {
        ModelMetadata syncMetadata = mwm.getSyncMetadata();
        Integer version = syncMetadata.getVersion();
        return version == null ? -1 : version;
    }
}
