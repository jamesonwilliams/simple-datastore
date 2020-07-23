package com.amplifyframework.datastore.sample

import com.amplifyframework.datastore.DataStoreItemChange

internal enum class Modification {
    CREATE, UPDATE, DELETE;

    companion object {
        fun from(type: DataStoreItemChange.Type): Modification {
            return when (type) {
                DataStoreItemChange.Type.DELETE -> DELETE
                DataStoreItemChange.Type.CREATE -> CREATE
                DataStoreItemChange.Type.UPDATE -> UPDATE
                else -> throw IllegalStateException("No modification known for $type")
            }
        }
    }
}
