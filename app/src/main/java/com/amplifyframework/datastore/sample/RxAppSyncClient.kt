package com.amplifyframework.datastore.sample

import com.amplifyframework.api.graphql.GraphQLBehavior
import com.amplifyframework.api.graphql.GraphQLResponse
import com.amplifyframework.core.model.Model
import com.amplifyframework.datastore.appsync.AppSync
import com.amplifyframework.datastore.appsync.AppSyncClient
import com.amplifyframework.datastore.appsync.ModelWithMetadata

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter

internal class RxAppSyncClient(graphQlBehavior: GraphQLBehavior) {
    private val appSyncClient: AppSync = AppSyncClient.via(graphQlBehavior)

    fun <T : Model> sync(clazz: Class<T>): Observable<ModelWithMetadata<T>> {
        return Observable.create { emitter: ObservableEmitter<GraphQLResponse<Iterable<ModelWithMetadata<T>>>> ->
            appSyncClient.sync(clazz, null,
                 { emitter.onNext(it) },
                 { emitter.onError(it) }
            )
        }
        .map { unwrapResponse(it) }
        .flatMap { Observable.fromIterable(it) }
    }

    fun <T : Model> create(item: T): Single<ModelWithMetadata<T>> {
        return Single.create { emitter: SingleEmitter<GraphQLResponse<ModelWithMetadata<T>>> ->
            appSyncClient.create(item,
                { emitter.onSuccess(it) },
                { emitter.onError(it) }
            )
        }
        .map { unwrapResponse(it) }
    }

    fun <T : Model> update(item: T, version: Int): Single<ModelWithMetadata<T>> {
        return Single.create { emitter: SingleEmitter<GraphQLResponse<ModelWithMetadata<T>>> ->
            appSyncClient.update(item, version,
                { emitter.onSuccess(it) },
                { emitter.onError(it) }
            )
        }
        .map {unwrapResponse(it) }
    }

    fun <T : Model> delete(clazz: Class<T>, itemId: String, version: Int): Single<ModelWithMetadata<T>> {
        return Single.create { emitter: SingleEmitter<GraphQLResponse<ModelWithMetadata<T>>> ->
            appSyncClient.delete(clazz, itemId, version,
                { emitter.onSuccess(it) },
                { emitter.onError(it) }
            )
        }
        .map { unwrapResponse(it) }
    }

    companion object {
        private fun <T> unwrapResponse(response: GraphQLResponse<T>): T {
            if (response.hasErrors()) {
                throw RuntimeException(response.errors.toString())
            } else if (!response.hasData()) {
                throw RuntimeException("No data!")
            }
            return response.data
        }
    }
}
