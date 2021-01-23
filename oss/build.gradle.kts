dependencies {

    implementation(project(":common"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation(Deps.SpringBoot.aop)
    implementation(Deps.SpringBoot.web)

    implementation(Deps.Apache.net)
    implementation(Deps.Apache.pool)
    implementation(Deps.Oss.minio)

    implementation("io.springfox:springfox-swagger2:2.9.2")

    implementation("org.ktorm:ktorm-core:3.2.0")
    implementation("org.ktorm:ktorm-support-postgresql:3.2.0")

    testImplementation(project(":common", "testArtifacts"))
    testImplementation(Deps.SpringBoot.test)


}
