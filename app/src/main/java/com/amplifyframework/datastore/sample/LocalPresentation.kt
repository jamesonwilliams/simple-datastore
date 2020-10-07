package com.amplifyframework.datastore.sample

import android.util.Pair

import com.amplifyframework.datastore.generated.model.Post

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Suppress("unused") // The sub-interface are, though. Lint doesn't catch that.
internal interface LocalPresentation {
    interface View {
        fun displayLocalLogLine(logLine: LogLine)
        fun clearLocalLineItems()
    }

    interface Presenter {
        fun createLocalItems()
        fun updateLocalItems()
        fun listLocalItems()
        fun deleteLocalItems()
        fun startSubscription()
        fun clearLocalLog()
        fun stopAllLocalActivities()
        fun signIn()
    }

    interface PostInteractor {
        fun createRandom(): Single<Post>
        fun updateAll(posts: List<Post>): Single<List<Post>>
        fun list(): Single<List<Post>>
        fun deleteAll(posts: List<Post>): Completable
        fun subscribe(): Observable<Pair<Post, Modification>>
        fun signIn(username: String, password: String): Completable
    }
}
