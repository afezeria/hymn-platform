package github.afezeria.hymn.common.util

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*

/**
 * @author afezeria
 */
class StringExtensionTest {
    val invalidCharSet = setOf('\\', '?', '*', '<', '"', ':', '>', '/')
    val chineseSeq = sequence {
        val random = Random()
        yield((random.nextInt(0x51FF) + 0x4E00).toChar())
    }
    val asciiCharList = (' '..'~').filter { !invalidCharSet.contains(it) }

    @Test
    fun fileNameTest() {
        assertSoftly {
            for (c in asciiCharList) {
                val str = c + chineseSeq.take(10).joinToString()
                str.isValidFileName() shouldBe true
            }

            listOf(
                *(0x00..0x1f).map { it.toChar() }.toTypedArray(),
                0x7f.toChar(),
                '\\', '?', '*', '<', '"', ':', '>', '/'
            ).map { "abc$it" }
                .forEach {
                    shouldThrow<BusinessException> {
                        it.throwIfFileNameInvalid()
                    }
                }
            "a".repeat(300).isValidFileName() shouldBe false
        }
    }

    @Test
    fun bucketNameTest() {
        listOf(
            "", "/", "?", "a_oeunh", "Abcd",
            "a", "a".repeat(50)
        ).forEach { it.isValidBucketName() shouldBe false }
        "test".isValidBucketName() shouldBe true

    }
}