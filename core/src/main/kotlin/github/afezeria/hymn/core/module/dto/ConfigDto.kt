package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.Config
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ConfigDto(
    @ApiModelProperty(value = "键 ;; idx", required = true)
    var key: String,
    @ApiModelProperty(value = "", required = true)
    var value: String,
) {
    fun toEntity(): Config {
        return Config(
            key = key,
            value = value,
        )
    }

    fun update(entity: Config) {
        entity.also {
            it.key = key
            it.value = value
        }
    }
}
