package github.afezeria.hymn.common

import org.junit.jupiter.api.Test

/**
 * @author afezeria
 */
abstract class AbstractContainerTest {
    companion object {
        private val container: KGenericContainer

        init {
            container = KGenericContainer("redis:5.0.3-alpine")
                .withExposedPorts(6379)
            container.start()
            println("=======================abc")
            println("redis://${container.host}:${container.firstMappedPort}")
        }
    }
}

class ttTest : AbstractContainerTest() {
    @Test
    fun bc() {
        println()
    }
}