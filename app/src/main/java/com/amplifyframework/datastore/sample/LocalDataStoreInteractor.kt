package com.amplifyframework.datastore.sample

import android.util.Pair

import com.amplifyframework.datastore.generated.model.Post
import com.amplifyframework.datastore.sample.LocalPresentation.PostInteractor
import com.amplifyframework.rx.RxDataStoreCategoryBehavior

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

internal class LocalDataStoreInteractor(private val dataStore: RxDataStoreCategoryBehavior) : PostInteractor {
    override fun createRandom(): Single<Post> {
        val post = Posts.random("Local")
        return dataStore.save(post).toSingleDefault(post)
    }

    override fun updateAll(posts: List<Post>): Single<List<Post>> =
        Observable.fromIterable(posts)
            .map { Posts.update(it) }
            .flatMapSingle { dataStore.save(it).toSingleDefault(it) }
            .toList()

    override fun list(): Single<List<Post>> = dataStore.query(Post::class.java).toList()

    override fun deleteAll(posts: List<Post>): Completable =
        Observable.fromIterable(posts).flatMapCompletable { dataStore.delete(it) }

    override fun subscribe(): Observable<Pair<Post, Modification>> =
        dataStore.observe(Post::class.java).map {
            Pair.create(it.item(), Modification.from(it.type()))
        }
}
