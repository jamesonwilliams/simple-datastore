package com.amplifyframework.datastore.sample;

import com.amplifyframework.datastore.appsync.ModelWithMetadata;
import com.amplifyframework.datastore.generated.model.Post;

import java.util.Locale;

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
            interactor.createRandom()
                .subscribe(
                createdItem ->
                    view.displayRemoteLogLine(LogLine.create(title(createdItem), details(createdItem))),
                failure ->  view.displayRemoteLogLine(LogLine.error("Failed to create a post.", failure.getMessage()))
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
                    updatedItem -> view.displayRemoteLogLine(LogLine.update(title(updatedItem), details(updatedItem))),
                    failure -> view.displayRemoteLogLine(LogLine.error("Failed to update a post.", failure.getMessage()))
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
                    deletedItem -> view.displayRemoteLogLine(LogLine.delete(title(deletedItem), details(deletedItem))),
                    failure -> view.displayRemoteLogLine(LogLine.error("Failed to delete a post.", failure.getMessage()))
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
                    foundItem -> view.displayRemoteLogLine(LogLine.query(title(foundItem), details(foundItem))),
                    failure -> view.displayRemoteLogLine(LogLine.error("Failure to list items.", failure.getMessage()))
                )
        );
    }

    @Override
    public void clearRemoteLogs() {
        view.clearRemoteLineItems();
    }

    private static String title(ModelWithMetadata<Post> modelWithMetadta) {
        return modelWithMetadta.getModel().getTitle();
    }

    private static String details(ModelWithMetadata<Post> modelWithMetadata) {
        String hash = modelWithMetadata.getSyncMetadata().getId().substring(0, 7);
        String version = String.valueOf(modelWithMetadata.getSyncMetadata().getVersion());
        Boolean deleted = modelWithMetadata.getSyncMetadata().isDeleted();
        return String.format(Locale.US, "%s, %s, %b", hash, version, deleted);
    }
}
