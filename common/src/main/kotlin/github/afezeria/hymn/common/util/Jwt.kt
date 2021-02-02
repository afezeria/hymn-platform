package github.afezeria.hymn.common.util

import github.afezeria.hymn.common.exception.BusinessException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey


/**
 * @author afezeria
 */

object Jwt {
    val key: SecretKey = Keys.hmacShaKeyFor(
        Decoders.BASE64URL.decode(
            PathMatchingResourcePatternResolver(this.javaClass.classLoader)
                .getResource("classpath:/jwt-key")
                .inputStream
                .readAllBytes()
                .decodeToString()
        )
    )

    val zoneId = ZoneId.systemDefault()


    private val parser = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()

    private val serializer = JacksonSerializer<Map<String, Any?>>(mapper)

    fun createJwtToken(claims: Map<String, Any?>, expiry: Long = 1800): String {
        return createJwtToken(claims, LocalDateTime.now().plusSeconds(expiry))
    }

    fun createJwtToken(claims: Map<String, Any?>, expiry: LocalDateTime): String {
        return Jwts.builder()
            .serializeToJsonWith(serializer)
            .setClaims(claims)
            .setId(randomUUIDStr())
            .setExpiration(Date.from(expiry.atZone(zoneId).toInstant()))
            .signWith(key)
            .compact()
    }

    fun parseToken(token: String): Map<String, Any?> {
        return try {
            parser.parseClaimsJws(token).body
        } catch (e: ExpiredJwtException) {
            throw BusinessException(
                "token已于 ${
                    e.message!!.substringBefore(".").substringAfter("at ")
                } 过期"
            )
        } catch (e: Exception) {
            throw BusinessException("无效的JWT", e)
        }
    }

    fun createKey(): String {
        val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512)!!
        return Encoders.BASE64URL.encode(key.encoded)
    }
}

fun main() {
    println(Jwt.createKey())
}

