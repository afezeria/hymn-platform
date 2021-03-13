package com.github.afezeria.hymn.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author afezeria
 */
@SpringBootApplication(scanBasePackages = ["com.github.afezeria.hymn"])
class HymnApplication

fun main(args: Array<String>) {
    runApplication<HymnApplication>(*args)
}
