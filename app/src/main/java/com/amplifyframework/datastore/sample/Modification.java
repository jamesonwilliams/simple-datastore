package com.amplifyframework.datastore.sample;

import com.amplifyframework.datastore.DataStoreItemChange;

enum Modification {
    CREATE, UPDATE, DELETE;

    static Modification from(DataStoreItemChange.Type type) {
        final Modification modification;
        switch (type) {
            case DELETE:
                return Modification.DELETE;
            case CREATE:
                return Modification.CREATE;
            case UPDATE:
                return Modification.UPDATE;
            default:
                throw new IllegalStateException("No modification known for " + type);
        }
    }
}
