package com.amplifyframework.datastore.sample;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

final class LogWindow {
    private LogLineAdapter adapter;
    private RecyclerView recyclerView;

    LogWindow(ViewGroup parent, @IdRes int resourceId) {
        this.adapter = new LogLineAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(parent.getContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = parent.findViewById(resourceId);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    void add(LogLine logLine) {
        adapter.add(logLine);
        recyclerView.scrollToPosition(this.adapter.size() - 1);
    }

    void clear() {
        adapter.clear();
    }

    private static final class LogLineAdapter extends RecyclerView.Adapter<LogLineAdapter.LineItemViewHolder> {
        private final List<LogLine> lines;

        LogLineAdapter() {
            this.lines = new ArrayList<>();
        }

        void add(LogLine line) {
            lines.add(line);
            super.notifyDataSetChanged();
        }

        void clear() {
            lines.clear();
            super.notifyDataSetChanged();
        }

        int size() {
            return lines.size();
        }

        @NonNull
        @Override
        public LineItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RelativeLayout lintItemView = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_item, parent, false);
            return new LineItemViewHolder(lintItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LineItemViewHolder holder, int position) {
            LogLine line = lines.get(position);
            holder.setLine(line);
        }

        @Override
        public int getItemCount() {
            return lines.size();
        }

        private static class LineItemViewHolder extends RecyclerView.ViewHolder {
            private final TextView titleView;
            private final TextView detailsView;
            private final TextView typeView;

            LineItemViewHolder(RelativeLayout lintItemView) {
                super(lintItemView);
                titleView = lintItemView.findViewById(R.id.title);
                titleView.setTypeface(null, Typeface.BOLD);

                detailsView = lintItemView.findViewById(R.id.details);
                typeView = lintItemView.findViewById(R.id.type);
            }

            void setLine(LogLine line) {
                titleView.setText(line.getTitle());
                detailsView.setText(line.getDetails());
                typeView.setText(line.getEventType().getText());
            }
        }
    }
}
