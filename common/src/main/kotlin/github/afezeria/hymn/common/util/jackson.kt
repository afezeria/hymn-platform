package github.afezeria.hymn.common.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule

/**
 * @author afezeria
 */
val yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")!!

class ClassicLocalDateTimeDeserializer : StdDeserializer<LocalDateTime?>(LocalDate::class.java) {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): LocalDateTime {
        return LocalDateTime.parse(jp.readValueAs(String::class.java), yyyyMMddHHmmss)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

class ClassicLocalDateTimeSerializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {

    companion object {
        private const val serialVersionUID = 1L
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.format(yyyyMMddHHmmss))
    }
}

object ClassicLocalDateTimeModule : SimpleModule() {
    init {
        addSerializer(ClassicLocalDateTimeSerializer())
        addDeserializer(LocalDateTime::class.java, ClassicLocalDateTimeDeserializer())
    }

}

val mapper = ObjectMapper()
    .registerModule(Jdk8Module())
    .registerModule(ParameterNamesModule())
    .registerModule(JavaTimeModule())
    .registerModule(KotlinModule())
    .registerModule(ClassicLocalDateTimeModule)


fun Any.toJson(): String {
    return mapper.writeValueAsString(this)
}


