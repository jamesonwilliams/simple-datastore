package com.amplifyframework.datastore.sample

import android.util.Pair

import com.amplifyframework.datastore.generated.model.Post
import com.amplifyframework.datastore.sample.LocalPresentation.PostInteractor
import com.amplifyframework.datastore.sample.LocalPresentation.Presenter
import com.amplifyframework.datastore.sample.LocalPresentation.View

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

internal class LocalPresenter(private val interactor: PostInteractor, private val view: View) : Presenter {
    private val ongoingOperations: CompositeDisposable = CompositeDisposable()

    override fun createLocalItems() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.createRandom().subscribe(
            Consumer { view.displayLocalLogLine(LogLine.create(title(it), details(it))) }
        ))
    }

    override fun updateLocalItems() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.list()
            .flatMap { interactor.updateAll(it) }
            .flatMapObservable { Observable.fromIterable(it) }
            .subscribe { view.displayLocalLogLine(LogLine.update(title(it), details(it))) }
        )
    }

    override fun listLocalItems() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.list()
            .flatMapObservable { Observable.fromIterable(it) }
            .subscribe { view.displayLocalLogLine(LogLine.query(title(it), details(it))) }
        )
    }

    override fun deleteLocalItems() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.list()
            .flatMap { interactor.deleteAll(it).toSingleDefault(it) }
            .flatMapObservable { Observable.fromIterable(it) }
            .subscribe { view.displayLocalLogLine(LogLine.delete(title(it), details(it))) }
        )
    }

    override fun startSubscription() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.subscribe()
            .doOnSubscribe { view.displayLocalLogLine(LogLine.subscription("Started", it.toString())) }
            .doOnDispose { view.displayLocalLogLine(LogLine.subscription("Disposed.", "")) }
            .subscribe(
                { view.displayLocalLogLine(LogLine.subscription(title(it), details(it))) },
                { view.displayLocalLogLine(LogLine.subscription("Failed.", it.message!!)) },
                { view.displayLocalLogLine(LogLine.subscription("Ended gracefully.", "")) }
            ))
    }

    override fun clearLocalLog() = view.clearLocalLineItems()

    override fun stopAllLocalActivities() = ongoingOperations.clear()

    companion object {
        private fun title(post: Post): String = post.title

        private fun details(post: Post): String = post.id.substring(0, 7)

        private fun title(pair: Pair<Post, Modification>): String = pair.first!!.title

        private fun details(pair: Pair<Post, Modification>): String =
            "${pair.second!!.name}, ${pair.first!!.id}"
    }
}
