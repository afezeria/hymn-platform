package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.Config
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ConfigDto(
    @ApiModelProperty(value = "键", required = true)
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
