package com.github.afezeria.hymn.script

import mu.KLogging
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import java.io.IOException

/**
 * @author afezeria
 */
class ContextWrapperFactory : BasePooledObjectFactory<ContextWrapper>() {
    companion object : KLogging()

    override fun create(): ContextWrapper {
        return ContextWrapper()
    }

    override fun wrap(obj: ContextWrapper?): PooledObject<ContextWrapper> {
        return DefaultPooledObject(obj)
    }

    override fun destroyObject(p: PooledObject<ContextWrapper>) {
        val wrapper = p.`object`
        wrapper.destroy()
    }

    override fun validateObject(p: PooledObject<ContextWrapper>): Boolean {
        return try {
            val wrapper = p.`object`
            wrapper.available()
        } catch (e: IOException) {
            false
        }
    }
}