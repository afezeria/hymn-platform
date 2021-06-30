package com.github.afezeria.hymn.common.context

import javax.servlet.ServletRequestEvent
import javax.servlet.ServletRequestListener
import javax.servlet.annotation.WebListener
import javax.servlet.http.HttpServletRequest

/**
 * @author afezeria
 */
@WebListener
class ContextListener : ServletRequestListener {
    override fun requestDestroyed(sre: ServletRequestEvent?) {
        val req = sre?.servletRequest
        if (req is HttpServletRequest) {

        }

        super.requestDestroyed(sre)
    }

    override fun requestInitialized(sre: ServletRequestEvent?) {
        super.requestInitialized(sre)
    }
}