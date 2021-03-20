package com.github.afezeria.hymn.script

import org.apache.commons.pool2.impl.GenericObjectPool

/**
 * @author afezeria
 */
class ContextWrapperPool(factory: ContextWrapperFactory) :
    GenericObjectPool<ContextWrapper>(factory) {
    init {
        testOnCreate = true
        testOnBorrow = true
        testOnReturn = true
        maxWaitMillis = 30 * 1000
    }
}
