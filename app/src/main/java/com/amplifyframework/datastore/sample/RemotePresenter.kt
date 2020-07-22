package com.amplifyframework.datastore.sample

import com.amplifyframework.datastore.appsync.ModelWithMetadata
import com.amplifyframework.datastore.generated.model.Post
import com.amplifyframework.datastore.sample.RemotePresentation.ApiInteractor
import com.amplifyframework.datastore.sample.RemotePresentation.Presenter
import com.amplifyframework.datastore.sample.RemotePresentation.View

import io.reactivex.disposables.CompositeDisposable

import java.util.Locale

internal class RemotePresenter(private val interactor: ApiInteractor, private val view: View) : Presenter {
    private val ongoingOperations: CompositeDisposable = CompositeDisposable()

    override fun createRemotePost() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.createRandom().subscribe(
            { view.displayRemoteLogLine(LogLine.create(title(it), details(it))) },
            { view.displayRemoteLogLine(LogLine.error("Failed to create a post.", it.message!!)) }
        ))
    }

    override fun updateRemotePosts() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.list()
            .filter { true != it.syncMetadata.isDeleted }
            .flatMapSingle { interactor.update(it) }
            .subscribe(
                { view.displayRemoteLogLine(LogLine.update(title(it), details(it))) },
                { view.displayRemoteLogLine(LogLine.error("Failed to update a post.", it.message!!)) }
            )
        )
    }

    override fun deleteRemotePosts() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.list()
            .filter { true != it.syncMetadata.isDeleted }
            .flatMapSingle { interactor.delete(it) }
            .subscribe(
                { view.displayRemoteLogLine(LogLine.delete(title(it), details(it))) },
                { view.displayRemoteLogLine(LogLine.error("Failed to delete a post.", it.message!!)) }
            )
        )
    }

    override fun listRemotePosts() {
        ongoingOperations.clear()
        ongoingOperations.add(interactor.list()
            .filter { true != it.syncMetadata.isDeleted }
            .subscribe(
                { view.displayRemoteLogLine(LogLine.query(title(it), details(it))) },
                { view.displayRemoteLogLine(LogLine.error("Failure to list items.", it.message!!)) }
            )
        )
    }

    override fun clearRemoteLogs() {
        view.clearRemoteLineItems()
    }

    companion object {
        private fun title(modelWithMetadata: ModelWithMetadata<Post>): String {
            return modelWithMetadata.model.title
        }

        private fun details(modelWithMetadata: ModelWithMetadata<Post>): String {
            val hash = modelWithMetadata.syncMetadata.id.substring(0, 7)
            val version = modelWithMetadata.syncMetadata.version.toString()
            val deleted = modelWithMetadata.syncMetadata.isDeleted
            return String.format(Locale.US, "%s, %s, %b", hash, version, deleted)
        }
    }
}
