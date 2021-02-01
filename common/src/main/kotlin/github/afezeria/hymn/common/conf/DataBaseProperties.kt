package github.afezeria.hymn.common.conf

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.*

/**
 * @author afezeria
 */
@ConfigurationProperties(prefix = "spring.db")
class DataBaseProperties {
    var admin: List<Properties> = mutableListOf()
    var user: Properties = Properties()
}