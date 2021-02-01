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

    implementation(Deps.Ktorm.core)
    implementation(Deps.Ktorm.postgresql)

    testImplementation(project(":common", "testArtifacts"))
    testImplementation(project(":cache-redis"))
    testImplementation(Deps.SpringBoot.test)


}
