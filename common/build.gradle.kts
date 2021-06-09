dependencies {
    implementation(Deps.SpringBoot.web)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    api(Deps.Jackson.core)
    api(Deps.Jackson.databind)
    api(Deps.Jackson.annotations)
    api(Deps.Jackson.datatypeJdk8)
    api(Deps.Jackson.datatypeJsr310)
    api(Deps.Jackson.parameterNames)
    api(Deps.Jackson.kotlin)

    api(Deps.Log.kotlinLogging)
    api(Deps.Log.slf4jApi)
    api(Deps.Log.logback)

    api(Deps.Jwt.api)
    runtimeOnly(Deps.Jwt.impl)
    implementation(Deps.Jwt.jackson)

    api(Deps.Apache.io)

    api(Deps.OkHttp3.okhttp)

    api(Deps.SpringBoot.redis)
//    api(Deps.Redssion.spring)

    implementation(Deps.SpringBoot.aop)
    implementation(Deps.Servlet.api)

    implementation(Deps.Jdbc.hikariCP)
    implementation(Deps.Jdbc.p6spy)

    implementation(Deps.Ktorm.core)
    implementation(Deps.Ktorm.postgresql)

    testApi(Deps.SpringBoot.test)
    testApi(Deps.Jdbc.postgresql)
    testApi(Deps.Jdbc.hikariCP)
    testApi(Deps.Kotest.assertions)
    testApi(Deps.Mockk.spring)

    testApi(platform(Deps.Junit.bom))
    testApi(Deps.Junit.jupiter)

    testApi(platform(Deps.TestContainers.bom))
    testApi(Deps.TestContainers.junit)


}


val testConfig = configurations.create("testArtifacts") {
    extendsFrom(configurations["testApi"])
}
tasks.register<Jar>("testJar") {
    dependsOn("testClasses")
    archiveClassifier.set("test")
    from(sourceSets["test"].output)

}
artifacts {
    add("testArtifacts", tasks.named<Jar>("testJar"))
}

