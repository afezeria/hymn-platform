package com.github.afezeria.hymn.common.mockbean

import com.github.afezeria.hymn.common.platform.PermService
import io.mockk.spyk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Proxy

/**
 * @author afezeria
 */
@Configuration
class TestPermServiceMock {
//    val service = object : PermService {
//        override fun hasFunctionPerm(name: String): Boolean = true
//        override fun hasDataPerm(
//            accountId: String,
//            objectId: String,
//            dataId: String,
//            read: Boolean,
//            update: Boolean,
//            delete: Boolean,
//            share: Boolean,
//            owner: Boolean
//        ): Boolean {
//        }
//
////        override fun hasDataPerm(
////            objectId: String,
////            dataId: String,
////            read: Boolean?,
////            update: Boolean?,
////            share: Boolean?,
////            owner: Boolean?
////        ): Boolean =
////            !(read == null && update == null && share == null && owner == null)
//
//        override fun hasObjectPerm(
//            objectId: String,
//            query: Boolean?,
//            create: Boolean?,
//            update: Boolean?,
//            delete: Boolean?
//        ): Boolean = !(query == null && update == null && create == null && delete == null)
//
//        override fun hasFieldPerm(
//            roleId: String,
//            fieldId: String,
//            read: Boolean?,
//            edit: Boolean?
//        ): Boolean = !(read == null && edit == null)
//
//        override fun hasFunctionPerm(roleId: String, name: String): Boolean = true
//
//        override fun hasDataPerm(
//            accountId: String,
//            objectId: String,
//            dataId: String,
//            read: Boolean?,
//            update: Boolean?,
//            share: Boolean?,
//            owner: Boolean?
//        ): Boolean =
//            !(read == null && update == null && share == null && owner == null)
//
//        override fun hasObjectPerm(
//            roleId: String,
//            objectId: String,
//            query: Boolean?,
//            create: Boolean?,
//            update: Boolean?,
//            delete: Boolean?
//        ): Boolean = !(query == null && update == null && create == null && delete == null)
//
//        override fun hasFieldPerm(
//            fieldId: String, read: Boolean?, edit: Boolean?
//        ): Boolean = !(read == null && edit == null)
//
//    }

    @Bean
    fun permService(): PermService {
        val proxy = Proxy.newProxyInstance(
            this::class.java.classLoader,
            arrayOf(PermService::class.java)
        ) { _, _, _ ->
            true
        } as PermService
        return spyk(proxy)
    }
}