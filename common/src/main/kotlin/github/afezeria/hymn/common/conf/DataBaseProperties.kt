package github.afezeria.hymn.common.conf

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author afezeria
 */
@Component
@ConfigurationProperties(prefix = "spring.db")
class DataBaseProperties {
    var admin: List<Properties> = mutableListOf()
    var user: Properties = Properties()
}