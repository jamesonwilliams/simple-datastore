package com.amplifyframework.datastore.sample

import com.amplifyframework.datastore.appsync.ModelWithMetadata
import com.amplifyframework.datastore.generated.model.Post
import com.amplifyframework.datastore.generated.model.PostStatus.ACTIVE
import com.amplifyframework.datastore.generated.model.PostStatus.INACTIVE

import java.security.SecureRandom
import java.util.Locale

internal object Posts {
    fun random(title: String?): Post {
        val random = SecureRandom()
        return Post.builder()
            .title("$title %${random.nextInt(9999)}")
            .rating(random.nextInt(5))
            .status(if (random.nextBoolean()) ACTIVE else INACTIVE)
            .build()
    }

    fun update(post: Post): Post =
        post.copyOfBuilder()
            .rating(post.rating + 5)
            .status(if (ACTIVE == post.status) INACTIVE else ACTIVE)
            .title("Updated " + post.title)
            .build()

    @Suppress("unused") // Useful for debugging?
    fun toString(modelWithMetadata: ModelWithMetadata<Post?>): String {
        val model = modelWithMetadata.model
        val version = modelWithMetadata.syncMetadata.version
        val truncatedId = model.id.substring(0, 7)
        return "$truncatedId@$version, '${model.title}', '${model.status}', '${model.rating}'."
    }

    @Suppress("unused") // Useful for debugging?
    fun toString(post: Post): String {
        val truncatedId = post.id.substring(0, 7)
        return "$truncatedId, '${post.title}', '${post.status}', '${post.rating}'."
    }
}
