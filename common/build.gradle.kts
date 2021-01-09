dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    api("com.fasterxml.jackson.core:jackson-core")
    api("com.fasterxml.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.core:jackson-annotations")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("com.fasterxml.jackson.module:jackson-module-parameter-names")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    api("io.github.microutils:kotlin-logging:1.12.0")

    api("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("javax.servlet:javax.servlet-api:4.0.1")

    implementation("org.ktorm:ktorm-core:3.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompile("ch.qos.logback:logback-classic:1.2.3")
    testCompile("org.postgresql:postgresql:42.2.10")
    testCompile("com.zaxxer:HikariCP:3.4.5")
    testCompile("io.kotest:kotest-assertions-core-jvm:4.3.1")
    testCompile("com.ninja-squad:springmockk:3.0.1")

    testCompile(platform("org.junit:junit-bom:5.7.0"))
    testCompile("org.junit.jupiter:junit-jupiter")

}


val testConfig = configurations.create("testArtifacts") {
    extendsFrom(configurations["testCompile"])
}
tasks.register<Jar>("testJar") {
    dependsOn("testClasses")
    archiveClassifier.set(project.name + "test")
    from(sourceSets["test"].output.classesDirs)
}
artifacts {
    add("testArtifacts", tasks.named<Jar>("testJar"))
}

