package com.amplifyframework.datastore.sample;

import android.util.Log;

import com.amplifyframework.datastore.generated.model.Post;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

final class LocalPresenter implements LocalPresentation.Presenter {
    private final LocalPresentation.PostInteractor interactor;
    private final LocalPresentation.View view;
    private final CompositeDisposable ongoingOperations;

    LocalPresenter(LocalPresentation.PostInteractor interactor, LocalPresentation.View view) {
        this.interactor = interactor;
        this.view = view;
        this.ongoingOperations = new CompositeDisposable();
    }

    @Override
    public void createLocalItems() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.createRandom()
                .subscribe(createdPost -> view.displayLocalText("CR: " + toString(createdPost)))
        );
    }

    @Override
    public void updateLocalItems() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .flatMap(interactor::updateAll)
                .flatMapObservable(Observable::fromIterable)
                .subscribe(
                    updatedPost -> view.displayLocalText("UPD: " + toString(updatedPost))
                )
        );
    }

    @Override
    public void listLocalItems() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .flatMapObservable(Observable::fromIterable)
                .subscribe(foundPost -> view.displayLocalText("QRY: " + toString(foundPost)))
        );
    }

    @Override
    public void deleteLocalItems() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .flatMap(items -> interactor.deleteAll(items).toSingleDefault(items))
                .flatMapObservable(Observable::fromIterable)
                .subscribe(deletedPost -> view.displayLocalText("DEL: " + toString(deletedPost)))
        );
    }

    @Override
    public void startSubscription() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.subscribe()
                .doOnSubscribe(ignored -> view.displayLocalText("Subscription started."))
                .doOnDispose(() -> view.displayLocalText("Subscription disposed."))
                .subscribe(
                    nextData -> view.displayLocalText("Got subscription data = " + toString(nextData)),
                    failure -> view.displayLocalText("Subscription ended in failure."),
                    () -> view.displayLocalText("Subscription ended gracefully.")
                )
        );
    }

    @Override
    public void clearLocalLog() {
        view.clearLocalText();
    }

    @Override
    public void stopAllLocalActivities() {
        ongoingOperations.clear();
    }

    private String toString(Object anything) {
        if (anything instanceof Throwable) {
            return Log.getStackTraceString((Throwable) anything);
        } else if (anything instanceof Post) {
            return Posts.toString((Post) anything);
        }
        return String.valueOf(anything);
    }
}
