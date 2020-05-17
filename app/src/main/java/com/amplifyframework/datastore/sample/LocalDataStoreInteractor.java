package com.amplifyframework.datastore.sample;

import android.util.Pair;

import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.rx.RxDataStoreCategoryBehavior;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

final class LocalDataStoreInteractor implements LocalPresentation.PostInteractor {
    private final RxDataStoreCategoryBehavior dataStore;

    LocalDataStoreInteractor(RxDataStoreCategoryBehavior dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Single<Post> createRandom() {
        Post post = Posts.random("Local");
        return dataStore.save(post).toSingleDefault(post);
    }

    @Override
    public Single<List<Post>> updateAll(List<Post> posts) {
        return Observable.fromIterable(posts)
            .map(Posts::update)
            .flatMapSingle(updatedPost -> dataStore.save(updatedPost).toSingleDefault(updatedPost))
            .toList();
    }

    @Override
    public Single<List<Post>> list() {
        return dataStore.query(Post.class).toList();
    }

    @Override
    public Completable deleteAll(List<Post> posts) {
        return Observable.fromIterable(posts)
            .flatMapCompletable(dataStore::delete);
    }

    @Override
    public Observable<Pair<Post, Modification>> subscribe() {
        return dataStore.observe(Post.class)
            .map(change -> {
                Modification modification = Modification.from(change.type());
                return Pair.create(change.item(), modification);
            });
    }
}
