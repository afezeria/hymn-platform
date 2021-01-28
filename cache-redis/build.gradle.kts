dependencies {

    implementation(project(":common"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(Deps.SpringBoot.redis)

    testImplementation(project(":common", "testArtifacts"))

}

