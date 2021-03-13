package com.github.afezeria.hymn.oss.ftp

import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.pool2.impl.GenericObjectPool

/**
 * @author afezeria
 */
class FTPClientPool(factory: FTPClientFactory) : GenericObjectPool<FTPClient>(factory) {
    init {
        testOnCreate = true
        testOnBorrow = true
        maxWaitMillis = 30 * 1000
    }
}