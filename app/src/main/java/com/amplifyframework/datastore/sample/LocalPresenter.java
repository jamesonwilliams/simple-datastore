package com.amplifyframework.datastore.sample;

import android.util.Pair;

import com.amplifyframework.datastore.generated.model.Post;

import java.util.Locale;

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
            interactor.createRandom().subscribe(createdPost ->
                view.displayLocalLogLine(LogLine.create(title(createdPost), details(createdPost)))
            )
        );
    }

    @Override
    public void updateLocalItems() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .flatMap(interactor::updateAll)
                .flatMapObservable(Observable::fromIterable)
                .subscribe(updatedPost ->
                    view.displayLocalLogLine(LogLine.update(title(updatedPost), details(updatedPost)))
                )
        );
    }

    @Override
    public void listLocalItems() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .flatMapObservable(Observable::fromIterable)
                .subscribe(foundPost ->
                    view.displayLocalLogLine(LogLine.query(title(foundPost), details(foundPost)))
                )
        );
    }

    @Override
    public void deleteLocalItems() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.list()
                .flatMap(items -> interactor.deleteAll(items).toSingleDefault(items))
                .flatMapObservable(Observable::fromIterable)
                .subscribe(deletedPost ->
                    view.displayLocalLogLine(LogLine.delete(title(deletedPost), details(deletedPost)))
                )
        );
    }

    @Override
    public void startSubscription() {
        ongoingOperations.clear();
        ongoingOperations.add(
            interactor.subscribe()
                .doOnSubscribe(token -> view.displayLocalLogLine(LogLine.subscription("Started.", token.toString())))
                .doOnDispose(() -> view.displayLocalLogLine(LogLine.subscription("Disposed.", "")))
                .subscribe(
                    nextData -> view.displayLocalLogLine(LogLine.subscription(title(nextData), details(nextData))),
                    failure -> view.displayLocalLogLine(LogLine.subscription("Failed.", failure.getMessage())),
                    () -> view.displayLocalLogLine(LogLine.subscription("Ended gracefully.", ""))
                )
        );
    }

    @Override
    public void clearLocalLog() {
        view.clearLocalLineItems();
    }

    @Override
    public void stopAllLocalActivities() {
        ongoingOperations.clear();
    }

    private static String title(Post post) {
        return post.getTitle();
    }

    private static String details(Post post) {
        return post.getId().substring(0, 7);
    }

    private static String title(Pair<Post, Modification> pair) {
        return pair.first.getTitle();
    }

    private static String details(Pair<Post, Modification> pair) {
        return String.format(Locale.US, "%s, %s", pair.second.name(), pair.first.getId());
    }
}
