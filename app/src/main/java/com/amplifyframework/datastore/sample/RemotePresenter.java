package com.amplifyframework.datastore.sample;

import android.util.Log;

import com.amplifyframework.datastore.appsync.ModelWithMetadata;
import com.amplifyframework.datastore.generated.model.Post;

import io.reactivex.disposables.CompositeDisposable;

final class RemotePresenter implements RemotePresentation.Presenter {
    private final RemotePresentation.ApiInteractor interactor;
    private final RemotePresentation.View view;
    private final CompositeDisposable ongoingOperations;

    RemotePresenter(RemotePresentation.ApiInteractor interactor, RemotePresentation.View view) {
        this.interactor = interactor;
        this.view = view;
        this.ongoingOperations = new CompositeDisposable();
    }

    @Override
    public void createRemotePost() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.createRandom().subscribe(
                createdItem ->  view.displayRemoteText("CR: " + toString(createdItem)),
                failure ->  view.displayRemoteText("Failed to create a post = " + toString(failure))
            )
        );
    }

    @Override
    public void updateRemotePosts() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .filter(mwm -> !Boolean.TRUE.equals(mwm.getSyncMetadata().isDeleted()))
                .flatMapSingle(interactor::update)
                .subscribe(
                    updatedItem -> view.displayRemoteText("UPD: " + toString(updatedItem)),
                    failure -> view.displayRemoteText("Failed to update = " + toString(failure))
                )
        );
    }

    @Override
    public void deleteRemotePosts() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .filter(mwm -> !Boolean.TRUE.equals(mwm.getSyncMetadata().isDeleted()))
                .flatMapSingle(interactor::delete)
                .subscribe(
                    deletedItem -> view.displayRemoteText("DEL: " + toString(deletedItem)),
                    failure -> view.displayRemoteText("Failed to delete = " + toString(failure))
                )
        );
    }

    @Override
    public void listRemotePosts() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .filter(mwm -> !Boolean.TRUE.equals(mwm.getSyncMetadata().isDeleted()))
                .subscribe(
                    foundItem -> view.displayRemoteText("QRY: " + toString(foundItem)),
                    failure -> view.displayRemoteText("Failure to list items = " + toString(failure))
                )
        );
    }

    @Override
    public void clearRemoteLogs() {
        view.clearRemoteText();
    }

    private String toString(Object anything) {
        if (anything instanceof Throwable) {
            return Log.getStackTraceString((Throwable) anything);
        } else if (anything instanceof ModelWithMetadata) {
            //noinspection unchecked
            return Posts.toString((ModelWithMetadata<Post>) anything);
        }
        return String.valueOf(anything);
    }
}
