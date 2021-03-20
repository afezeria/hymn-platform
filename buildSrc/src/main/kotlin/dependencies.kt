object Deps {

    object Ktorm {
        const val core = "org.ktorm:ktorm-core:3.3.0"
        const val postgresql = "org.ktorm:ktorm-support-postgresql:3.3.0"
    }

    object Apache {
        const val net = "commons-net:commons-net:3.7.2"
        const val pool = "org.apache.commons:commons-pool2:2.9.0"
        const val io = "commons-io:commons-io:2.8.0"
    }

    object Jdbc {
        const val postgresql = "org.postgresql:postgresql:42.2.10"
        const val p6spy = "p6spy:p6spy:3.9.1"
        const val hikariCP = "com.zaxxer:HikariCP:3.4.5"
    }

    object Sql {
        const val jsqlparser = "com.github.jsqlparser:jsqlparser:4.0"
    }

    object Jackson {
        const val core = "com.fasterxml.jackson.core:jackson-core"
        const val databind = "com.fasterxml.jackson.core:jackson-databind"
        const val annotations = "com.fasterxml.jackson.core:jackson-annotations"
        const val datatypeJdk8 = "com.fasterxml.jackson.datatype:jackson-datatype-jdk8"
        const val datatypeJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        const val parameterNames = "com.fasterxml.jackson.module:jackson-module-parameter-names"
        const val kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"
    }

    object Jwt {
        const val api = "io.jsonwebtoken:jjwt-api:0.11.2"
        const val impl = "io.jsonwebtoken:jjwt-impl:0.11.2"
        const val jackson = "io.jsonwebtoken:jjwt-jackson:0.11.2"
    }

    object Log {
        const val slf4jApi = "org.slf4j:slf4j-api:1.7.30"
        const val logback = "ch.qos.logback:logback-classic:1.2.3"
        const val kotlinLogging = "io.github.microutils:kotlin-logging:1.12.0"
    }

    object Job {
        const val quartz = "org.quartz-scheduler:quartz:2.3.2"
    }


    object OkHttp3 {
        const val okhttp = "com.squareup.okhttp3:okhttp:4.9.1"

    }

    object SpringBoot {
        const val web = "org.springframework.boot:spring-boot-starter-web"
        const val aop = "org.springframework.boot:spring-boot-starter-aop"
        const val test = "org.springframework.boot:spring-boot-starter-test"
        const val redis = "org.springframework.boot:spring-boot-starter-data-redis"
    }

    object Servlet {
        const val api = "javax.servlet:javax.servlet-api:4.0.1"
    }

    object Graal {
        const val js = "org.graalvm.js:js:21.0.0.2"
        const val jsScriptEngine = "org.graalvm.js:js-scriptengine:21.0.0.2"
        const val sdk = "org.graalvm.sdk:graal-sdk:21.0.0.2"
        const val truffleApi = "org.graalvm.truffle:truffle-api:21.0.0.2"
    }

    object Kotest {
        const val assertions = "io.kotest:kotest-assertions-core-jvm:4.3.1"
    }

    object Mockk {
        const val spring = "com.ninja-squad:springmockk:3.0.1"
    }

    object Redssion {
        const val spring = "org.redisson:redisson-spring-boot-starter:3.14.1"
    }

    object Junit {
        const val bom = "org.junit:junit-bom:5.7.0"
        const val jupiter = "org.junit.jupiter:junit-jupiter"
    }

    object Oss {
        const val minio = "io.minio:minio:8.0.3"
    }

    object Swagger {
        const val core = "io.springfox:springfox-swagger2:2.9.2"
        const val ui = "io.springfox:springfox-swagger-ui:2.9.2"
    }

    object TestContainers {
        const val bom = "org.testcontainers:testcontainers-bom:1.15.1"
        const val junit = "org.testcontainers:junit-jupiter"
    }
}