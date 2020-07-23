package com.amplifyframework.datastore.sample

import com.amplifyframework.datastore.appsync.ModelWithMetadata
import com.amplifyframework.datastore.generated.model.Post

import io.reactivex.Observable
import io.reactivex.Single

internal interface RemotePresentation {
    interface View {
        fun displayRemoteLogLine(logLine: LogLine)
        fun clearRemoteLineItems()
    }

    interface Presenter {
        fun createRemotePost()
        fun updateRemotePosts()
        fun deleteRemotePosts()
        fun listRemotePosts()
        fun clearRemoteLogs()
    }

    interface ApiInteractor {
        fun createRandom(): Single<ModelWithMetadata<Post>>
        fun update(post: ModelWithMetadata<Post>): Single<ModelWithMetadata<Post>>
        fun delete(post: ModelWithMetadata<Post>): Single<ModelWithMetadata<Post>>
        fun list(): Observable<ModelWithMetadata<Post>>
    }
}
