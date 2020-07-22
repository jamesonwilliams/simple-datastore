package com.amplifyframework.datastore.sample

import com.amplifyframework.datastore.sample.LogLine.EventType.CREATE
import com.amplifyframework.datastore.sample.LogLine.EventType.DELETE
import com.amplifyframework.datastore.sample.LogLine.EventType.ERROR
import com.amplifyframework.datastore.sample.LogLine.EventType.QUERY
import com.amplifyframework.datastore.sample.LogLine.EventType.SUBSCRIPTION
import com.amplifyframework.datastore.sample.LogLine.EventType.UPDATE

data class LogLine constructor(val eventType: EventType, val title: String, val details: String?) {
    enum class EventType(val text: String) {
        QUERY("QRY"),
        SUBSCRIPTION("SUB"),
        DELETE("DEL"),
        CREATE("NEW"),
        UPDATE("UPD"),
        ERROR("ERR");
    }

    companion object {
        private fun create(eventType: EventType, title: String, details: String): LogLine {
            return LogLine(eventType, title, details)
        }

        fun query(title: String, details: String): LogLine {
            return create(QUERY, title, details)
        }

        fun subscription(title: String, details: String): LogLine {
            return create(SUBSCRIPTION, title, details)
        }

        fun delete(title: String, details: String): LogLine {
            return create(DELETE, title, details)
        }

        fun create(title: String, details: String): LogLine {
            return create(CREATE, title, details)
        }

        fun update(title: String, details: String): LogLine {
            return create(UPDATE, title, details)
        }

        fun error(title: String, details: String): LogLine {
            return create(ERROR, title, details)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogLine

        if (eventType != other.eventType) return false
        if (title != other.title) return false
        if (details != other.details) return false

        return true
    }

    override fun hashCode(): Int {
        var result = eventType.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (details?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "LogLine(eventType=$eventType, title='$title', details=$details)"
    }
}
