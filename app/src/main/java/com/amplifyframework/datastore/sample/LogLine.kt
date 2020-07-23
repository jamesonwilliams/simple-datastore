package com.amplifyframework.datastore.sample

import com.amplifyframework.datastore.sample.LogLine.EventType.CREATE
import com.amplifyframework.datastore.sample.LogLine.EventType.DELETE
import com.amplifyframework.datastore.sample.LogLine.EventType.ERROR
import com.amplifyframework.datastore.sample.LogLine.EventType.MESSAGE
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
        MESSAGE("MSG"),
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

        fun message(title: String, details: String): LogLine {
            return create(MESSAGE, title, details)
        }

        fun error(title: String, details: String): LogLine {
            return create(ERROR, title, details)
        }
    }
}
