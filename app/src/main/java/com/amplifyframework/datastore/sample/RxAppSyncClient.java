package com.amplifyframework.datastore.sample;

import com.amplifyframework.api.graphql.GraphQLBehavior;
import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.appsync.AppSync;
import com.amplifyframework.datastore.appsync.AppSyncClient;
import com.amplifyframework.datastore.appsync.ModelWithMetadata;

import io.reactivex.Observable;
import io.reactivex.Single;

@SuppressWarnings("SameParameterValue")
final class RxAppSyncClient {
    private AppSync appSyncClient;

    RxAppSyncClient(GraphQLBehavior graphQlBehavior) {
        this.appSyncClient = AppSyncClient.via(graphQlBehavior);
    }

    <T extends Model> Observable<ModelWithMetadata<T>> sync(Class<T> clazz) {
        return Observable.<GraphQLResponse<Iterable<ModelWithMetadata<T>>>>create(emitter ->
            appSyncClient.sync(clazz, null,
                emitter::onNext,
                emitter::onError
            )
        )
        .map(RxAppSyncClient::unwrapResponse)
        .flatMap(Observable::fromIterable);
    }

    <T extends Model> Single<ModelWithMetadata<T>> create(T item) {
        return Single.<GraphQLResponse<ModelWithMetadata<T>>>create(emitter ->
            appSyncClient.create(item, emitter::onSuccess, emitter::onError)
        )
        .map(RxAppSyncClient::unwrapResponse);
    }

    <T extends Model> Single<ModelWithMetadata<T>> update(T item, int version) {
        return Single.<GraphQLResponse<ModelWithMetadata<T>>>create(emitter ->
            appSyncClient.update(item, version, emitter::onSuccess, emitter::onError)
        )
        .map(RxAppSyncClient::unwrapResponse);
    }

    <T extends Model> Single<ModelWithMetadata<T>> delete(Class<T> clazz, String itemId, int version) {
        return Single.<GraphQLResponse<ModelWithMetadata<T>>>create(emitter ->
            appSyncClient.delete(clazz, itemId, version, emitter::onSuccess, emitter::onError)
        )
        .map(RxAppSyncClient::unwrapResponse);
    }

    private static <T> T unwrapResponse(GraphQLResponse<T> response) {
        if (response.hasErrors()) {
            throw new RuntimeException(response.getErrors().toString());
        } else if (!response.hasData()) {
            throw new RuntimeException("No data!");
        }
        return response.getData();
    }
}
