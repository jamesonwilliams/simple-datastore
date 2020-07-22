package com.amplifyframework.datastore.sample

import com.amplifyframework.datastore.appsync.ModelWithMetadata
import com.amplifyframework.datastore.generated.model.Post
import com.amplifyframework.datastore.sample.RemotePresentation.ApiInteractor

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class RemoteApiInteractor(private val appSync: RxAppSyncClient) : ApiInteractor {
    override fun createRandom(): Single<ModelWithMetadata<Post>> =
        appSync.create(Posts.random("Remote"))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    override fun update(post: ModelWithMetadata<Post>): Single<ModelWithMetadata<Post>> =
        appSync.update(Posts.update(post.model), post.syncMetadata.version ?: -1)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    override fun delete(post: ModelWithMetadata<Post>): Single<ModelWithMetadata<Post>> =
        appSync.delete(Post::class.java, post.model.id, post.syncMetadata.version ?: -1)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    override fun list(): Observable<ModelWithMetadata<Post>> =
        appSync.sync(Post::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
}
