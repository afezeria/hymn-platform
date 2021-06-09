import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import  org.springframework.boot.gradle.tasks.bundling.BootJar


plugins {
    id("org.springframework.boot") version "2.4.6" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.5.10" apply false
    kotlin("plugin.spring") version "1.5.10" apply false
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


subprojects {
    apply {
        plugin("org.springframework.boot")
        plugin("kotlin")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("io.spring.dependency-management")
        plugin(SerialTestPlugin::class)
    }

    configurations {
        all {
            exclude(module = "spring-boot-starter-logging")
        }
    }


    repositories {
        mavenCentral()
        maven(url = "https://repo.spring.io/libs-release")
    }


    group = "github.afezeria.hymn"
    version = "0.0.1"

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }


    tasks.withType<Test> {
        useJUnitPlatform()
    }


    tasks.getByName<BootJar>("bootJar") {
        enabled = false
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }


}
allprojects {
    tasks {
        val listrepos by registering {
            doLast {
                project.repositories
                    .map { it as MavenArtifactRepository }
                    .forEach {
                        println("name: ${it.name}, url: ${it.url}")
                    }
            }
        }
    }
}
