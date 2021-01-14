dependencies {

    implementation(project(":common"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation(Deps.SpringBoot.aop)
    implementation(Deps.SpringBoot.web)

    implementation(Deps.Apache.net)
    implementation(Deps.Apache.pool)
    implementation(Deps.Oss.minio)


    testImplementation(project(":common", "testArtifacts"))
    testImplementation(Deps.SpringBoot.test)


}
