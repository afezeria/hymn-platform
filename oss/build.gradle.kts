dependencies {

    compile(project(":common"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation(project(":common", "testRuntime"))
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }

    implementation("com.qiniu:qiniu-java-sdk:7.4.+")
    implementation("io.minio:minio:8.0.3")

}
