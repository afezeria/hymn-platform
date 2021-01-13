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

    api(Deps.Apache.io)

    api(Deps.Redssion.spring)

    implementation(Deps.SpringBoot.aop)
    implementation(Deps.Servlet.api)

    implementation(Deps.Ktorm.core)

    testImplementation(Deps.SpringBoot.test)
    testCompile(Deps.Jdbc.postgresql)
    testCompile(Deps.DataSource.hikariCP)
    testCompile(Deps.Kotest.assertions)
    testCompile(Deps.Mockk.spring)

    testCompile(platform(Deps.Junit.bom))
    testCompile(Deps.Junit.jupiter)

    testCompile(platform(Deps.TestContainers.bom))
    testCompile(Deps.TestContainers.junit)


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

