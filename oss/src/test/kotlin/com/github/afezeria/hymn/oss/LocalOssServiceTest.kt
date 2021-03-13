package com.github.afezeria.hymn.oss

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.util.randomUUIDStr
import com.github.afezeria.hymn.oss.local.LocalConfig
import com.github.afezeria.hymn.oss.local.LocalOssService
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import mu.KLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**
 * @author afezeria
 */
class LocalOssServiceTest {
    companion object : KLogging() {
        lateinit var config: LocalConfig
        lateinit var service: StorageService
        lateinit var rootDir: String

        @JvmStatic
        @BeforeAll
        fun before() {
            rootDir = System.getProperty("user.dir") + "/test-dir/"
            Files.createDirectories(Path.of(rootDir))
            config = LocalConfig()
            config.rootDir = rootDir

            service = LocalOssService(config)
        }

        @AfterAll
        @JvmStatic
        fun after() {
            Files.walk(Path.of(rootDir)).use { walk ->
                walk.sorted(Comparator.reverseOrder())
                    .map { obj: Path -> obj.toFile() }
                    .peek { println("delete $it") }
                    .forEach(File::delete)
            }
        }
    }

    @Test
    fun upload() {
        val str = "abc".toByteArray()
        service.putFile(
            bucket = "abc",
            objectName = "abc.txt",
            inputStream = ByteArrayInputStream(str),
            contentType = "text/plain"
        )
        fileExist("abc/abc.txt") shouldBe true
        fileContent("abc/abc.txt") shouldBe str
    }

    @Test
    fun `upload with path`() {
        val str = "abc".toByteArray()
        service.putFile(
            bucket = "abc",
            objectName = "/2020/02/01/abc.txt",
            inputStream = ByteArrayInputStream(str),
            contentType = "text/plain"
        )
        fileExist("/abc/2020/02/01/abc.txt") shouldBe true
        fileContent("/abc/2020/02/01/abc.txt") shouldBe str
    }

    @Test
    fun download() {
        val path = "dow/abc.txt"
        val byteArray = createFile(path)
        var array: ByteArray? = null
        service.getFile("dow", "abc.txt") {
            array = it.readAllBytes()
        }
        byteArray shouldBe array
    }

    @Test
    fun `move file in the same bucket`() {
        val path = "move/abc.txt"
        val byteArray = createFile(path)
        val bucket = "move"
        service.moveFile(bucket, "bcd.txt", bucket, "abc.txt")
        fileExist("move/abc.txt") shouldBe false
        fileExist("move/bcd.txt") shouldBe true
        fileContent("move/bcd.txt") shouldBe byteArray
    }

    @Test
    fun `move file between different bucket`() {
        val fa = randomUUIDStr()
        val fb = randomUUIDStr()
        val pa = "move/$fa"
        val pb = "move2/$fb"
        val byteArray = createFile(pb)
        service.moveFile("move", fa, "move2", fb)
        fileExist(pb) shouldBe false
        fileExist(pa) shouldBe true
        fileContent(pa) shouldBe byteArray
    }

    @Test
    fun `copy file in the same bucket`() {

        val fa = randomUUIDStr()
        val fb = randomUUIDStr()
        val pa = "copy/$fa"
        val pb = "copy/$fb"
        val byteArray = createFile(pb)
        service.copyFile("copy", fa, "copy", fb)
        fileExist(pb) shouldBe true
        fileExist(pa) shouldBe true
        fileContent(pa) shouldBe byteArray
    }

    @Test
    fun `copy file between different bucket`() {
        val fa = randomUUIDStr()
        val fb = randomUUIDStr()
        val pa = "copy/$fa"
        val pb = "copy2/$fb"
        val byteArray = createFile(pb)
        service.copyFile("copy", fa, "copy2", fb)
        fileExist(pb) shouldBe true
        fileExist(pa) shouldBe true
        fileContent(pa) shouldBe byteArray

    }

    @Test
    fun remove() {
        val fa = randomUUIDStr()
        val pa = "copy/$fa"
        createFile(pa)
        service.removeFile("copy", fa)
        fileExist(pa) shouldBe false
    }

    @Test
    fun fileExist() {
        val bucket = "fileexist"
        val file = "abc"
        createFile("fileexist/abc")
        shouldNotThrow<BusinessException> {
            service.fileExist(bucket, file)
        }
        shouldThrow<BusinessException> {
            service.getFile("abc242", "abc", {})
        }.apply {
            message shouldBe "文件不存在"
        }

    }

    @Test
    fun `get a file that does not exist`() {
        shouldThrow<BusinessException> {
            service.getFile("abc", "abc", {})
        }.apply {
            message shouldBe "文件不存在"
        }
    }

    @Test
    fun `move nonexistent files`() {
        shouldThrow<BusinessException> {
            service.moveFile("abc", "abc", "bcd", "bcd")
        }.apply {
            message shouldBe "文件不存在"
        }
    }

    @Test
    fun `copy nonexistent files`() {
        shouldThrow<BusinessException> {
            service.copyFile("abc", "abc", "bcd", "bcd")
        }.apply {
            message shouldBe "文件不存在"
        }
    }

    private fun fileExist(path: String): Boolean {
        val p = Path.of(rootDir + path.trim('/'))
        return Files.exists(p)
    }

    private fun fileContent(path: String): ByteArray {
        return FileInputStream(Path.of(rootDir + path.trim('/')).toFile())
            .readAllBytes()
    }

    fun createFile(path: String, length: Int = 1000): ByteArray {
        val array = ByteArray(length).apply { Random().nextBytes(this) }
        val p = Path.of(rootDir + path.trim('/'))
        val parent = p.parent
        if (Files.exists(p.parent).not()) {
            Files.createDirectories(parent)
        }
        FileOutputStream(p.toFile()).use {
            it.write(array)
        }
        return array
    }


}