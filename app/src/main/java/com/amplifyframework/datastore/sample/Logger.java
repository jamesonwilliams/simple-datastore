package com.amplifyframework.datastore.sample;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

final class Logger {
    private final String tag;
    private final TextView textView;
    private final CompositeDisposable disposables;

    Logger(String tag, TextView textView) {
        this.tag = tag;
        this.textView = textView;
        this.disposables = new CompositeDisposable();
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setSingleLine(false);
    }

    void log(String string) {
        disposables.add(Observable.just(string)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(it -> {
                textView.append(string + System.lineSeparator());
                Log.i(tag, string);
            }));
    }

    void clear() {
        textView.setText("");
        Log.i(tag, "Cleared log.");
    }
}
