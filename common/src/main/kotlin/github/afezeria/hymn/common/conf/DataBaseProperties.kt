package github.afezeria.hymn.common.conf

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.*

/**
 * @author afezeria
 */
@ConfigurationProperties(prefix = "spring")
class DataBaseProperties {
    var db: List<Properties> = mutableListOf()
}