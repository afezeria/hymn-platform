package com.github.afezeria.hymn.common.db

/**
 *
 * date 2021/6/28
 */
class Page<E>(val total: Long, val data: MutableList<E>) : MutableList<E> by data {
    companion object {
        fun <E> empty(): Page<E> {
            return Page(0, ArrayList())
        }
    }
}