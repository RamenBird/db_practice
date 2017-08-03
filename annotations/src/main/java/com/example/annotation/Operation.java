package com.example.annotation;

/**
 * Created by RamenBird on 2017/6/23.
 */

public enum Operation {
    all,
    find,
    insert,
    /**
     * Only stable{@link Column} and primary/unique{@link Column} columns can have this operation.
     */
    update
}
