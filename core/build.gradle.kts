dependencies {

    implementation(project(":common"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")

    implementation("org.ktorm:ktorm-core:3.2.0")
    implementation("org.ktorm:ktorm-support-postgresql:3.2.0")
    testImplementation(project(":common", "testArtifacts"))
}
