package com.amplifyframework.datastore.sample;

import com.amplifyframework.datastore.appsync.ModelWithMetadata;
import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.datastore.generated.model.PostStatus;

import java.security.SecureRandom;
import java.util.Locale;

final class Posts {
    private Posts() {}

    static Post random(String title) {
        SecureRandom random = new SecureRandom();
        return Post.builder()
            .title(String.format(Locale.US, "%s %d", title, random.nextInt(9999)))
            .rating(random.nextInt(5))
            .status(random.nextBoolean() ? PostStatus.ACTIVE : PostStatus.INACTIVE)
            .build();
    }

    static Post update(Post post) {
        return post.copyOfBuilder()
            .rating(post.getRating() + 5)
            .status(PostStatus.ACTIVE.equals(post.getStatus()) ? PostStatus.INACTIVE : PostStatus.ACTIVE)
            .title("Updated " + post.getTitle())
            .build();
    }

    static String toString(ModelWithMetadata<Post> modelWithMetadata) {
        Post model = modelWithMetadata.getModel();
        Integer version = modelWithMetadata.getSyncMetadata().getVersion();
        String truncatedId = model.getId().substring(0, 7);
        return String.format(Locale.US,
            "%s@%d, \'%s\', \'%s\', \'%d\'.",
            truncatedId, version, model.getTitle(), model.getStatus(), model.getRating()
        );
    }

    static String toString(Post post) {
        String truncatedId = post.getId().substring(0, 7);
        return String.format(Locale.US,
            "%s, \'%s\', \'%s\', \'%d\'.",
            truncatedId, post.getTitle(), post.getStatus(), post.getRating()
        );
    }
}
