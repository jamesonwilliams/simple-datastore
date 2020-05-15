package com.amplifyframework.datastore.sample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.datastore.generated.model.PostStatus;
import com.amplifyframework.rx.RxAmplify;

import java.util.Random;
import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CompositeDisposable ongoingOperations;
    private TextView logView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.logView = findViewById(R.id.log_window);
        this.ongoingOperations = new CompositeDisposable();

        try {
            clearDatabase();
            RxAmplify.addPlugin(new AWSApiPlugin());
            RxAmplify.addPlugin(new AWSDataStorePlugin());
            RxAmplify.configure(getApplicationContext());
        } catch (AmplifyException configurationFailure) {
            throw new RuntimeException(configurationFailure);
        }

        findViewById(R.id.add_post_button).setOnClickListener(v -> createPost());
        findViewById(R.id.list_posts_button).setOnClickListener(v -> listPosts());
        findViewById(R.id.update_post_button).setOnClickListener(v -> updatePosts());
        findViewById(R.id.delete_all_posts).setOnClickListener(v -> deleteAll());
        findViewById(R.id.begin_subscription).setOnClickListener(v -> beginSubscription());
        findViewById(R.id.stop_everything).setOnClickListener(v -> stopEverything());
        findViewById(R.id.clear_log).setOnClickListener(v -> clearLogs());
    }

    private void clearDatabase() {
        appendLog("Clearing DataBase...");
        getApplicationContext().deleteDatabase("AmplifyDatabase.db");
    }

    private void appendLog(String log) {
        Log.i(TAG, log);
        runOnUiThread(() -> logView.append(log + "\n"));
    }

    private void clearLogs() {
        runOnUiThread(() -> logView.setText(R.string.empty));
        appendLog("Log cleared.");
    }

    private void beginSubscription() {
        ongoingOperations.add(
            RxAmplify.DataStore.observe(Post.class)
                .doOnSubscribe(disposable -> appendLog("Started subscription."))
                .doOnDispose(() -> appendLog("Subscription cancelled."))
                .subscribe(
                    postChange -> appendLog("Item changed: " + postChange.type() + ", " + postChange.item().getId()),
                    subscriptionFailure -> appendLog(Log.getStackTraceString(subscriptionFailure)),
                    () -> appendLog("Subscription completed.")
                )
        );
    }

    private void stopEverything() {
        if (ongoingOperations != null && !ongoingOperations.isDisposed()) {
            ongoingOperations.clear();
        }
    }

    private void deleteAll() {
        ongoingOperations.add(
            RxAmplify.DataStore.query(Post.class)
                .flatMapCompletable(RxAmplify.DataStore::delete)
                .subscribe(
                    () -> appendLog("Deleted all posts."),
                    failure -> appendLog("Failed to delete posts: " + Log.getStackTraceString(failure))
                )
        );
    }

    private void createPost() {
        ongoingOperations.add(
            RxAmplify.DataStore.save(Post.builder()
                .title(RandomTitle.nextTitle())
                .rating(RandomRating.nextRating())
                .status(PostStatus.ACTIVE)
                .build())
                .subscribe(
                    () -> appendLog("Created a post."),
                    failure -> appendLog("Failed to save a post: " + Log.getStackTraceString(failure))
                )
        );
    }

    private void updatePosts() {
        ongoingOperations.add(
            RxAmplify.DataStore.query(Post.class)
                .flatMapCompletable(post -> RxAmplify.DataStore.save(post.copyOfBuilder()
                    .rating(2)
                    .status(PostStatus.INACTIVE)
                    .title("Updated title.")
                    .build()))
                .subscribe(
                    () -> appendLog("Updated all posts."),
                    failure -> appendLog("Failed to update posts: " + Log.getStackTraceString(failure))
                )
        );
    }

    private void listPosts() {
        ongoingOperations.add(
            RxAmplify.DataStore.query(Post.class).subscribe(
                post -> appendLog(toString(post)),
                failure -> appendLog(Log.getStackTraceString(failure)),
                () -> appendLog("Finished listing posts.")
            )
        );
    }

    private String toString(Post post) {
        return "Post{\n" +
            "  title=" + post.getTitle() + "\n" +
            "  id=" + post.getId() + "\n" +
            "  rating=" + post.getRating() + "\n" +
            "  status=" + post.getStatus() + "\n" +
            "}";
    }

    private static class RandomRating {
        static int nextRating() {
            return new Random().nextInt(5);
        }
    }

    private static class RandomTitle {
        static String nextTitle() {
            return "Title " + UUID.randomUUID().toString();
        }
    }
}
