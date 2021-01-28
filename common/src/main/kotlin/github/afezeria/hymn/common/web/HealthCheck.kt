package github.afezeria.hymn.common.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author afezeria
 */
@RestController
@RequestMapping("common/public")
class HealthCheck {
    @GetMapping
    fun ok(): String {
        return "ok"
    }
}