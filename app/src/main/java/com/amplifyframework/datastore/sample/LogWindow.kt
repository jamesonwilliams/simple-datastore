package com.amplifyframework.datastore.sample

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.amplifyframework.datastore.sample.LogWindow.LogLineAdapter.LineItemViewHolder
import com.amplifyframework.datastore.sample.R.id
import com.amplifyframework.datastore.sample.R.layout

internal class LogWindow(parent: ViewGroup, @IdRes resourceId: Int) {
    private val adapter: LogLineAdapter = LogLineAdapter()
    private val recyclerView: RecyclerView
    init {
        val linearLayoutManager = LinearLayoutManager(parent.context)
        linearLayoutManager.stackFromEnd = true
        recyclerView = parent.findViewById(resourceId)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    fun add(logLine: LogLine) {
        adapter.add(logLine)
        recyclerView.scrollToPosition(adapter.size() - 1)
    }

    fun clear(): Unit = adapter.clear()

    private class LogLineAdapter internal constructor() : Adapter<LineItemViewHolder>() {
        private val lines: MutableList<LogLine> = ArrayList()

        fun add(line: LogLine) {
            lines.add(line)
            super.notifyDataSetChanged()
        }

        fun clear() {
            lines.clear()
            super.notifyDataSetChanged()
        }

        fun size(): Int = lines.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineItemViewHolder {
            val lintItemView = LayoutInflater.from(parent.context)
                .inflate(layout.line_item, parent, false) as RelativeLayout
            return LineItemViewHolder(lintItemView)
        }

        override fun onBindViewHolder(holder: LineItemViewHolder, position: Int) = holder.setLine(lines[position])

        override fun getItemCount(): Int = lines.size

        private class LineItemViewHolder internal constructor(lintItemView: RelativeLayout) : ViewHolder(lintItemView) {
            private val detailsView: TextView = lintItemView.findViewById(id.details)
            private val typeView: TextView = lintItemView.findViewById(id.type)
            private val titleView: TextView = lintItemView.findViewById(id.title)
            init {
                titleView.setTypeface(null, Typeface.BOLD)
            }

            fun setLine(line: LogLine) {
                titleView.text = line.title
                detailsView.text = line.details
                typeView.text = line.eventType.text
            }
        }
    }
}
