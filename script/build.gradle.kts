dependencies {

    implementation(project(":common"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.graalvm.js:js:20.1.0")
    implementation("org.graalvm.js:js-scriptengine:20.1.0")
    implementation("org.graalvm.sdk:graal-sdk:20.1.0")

    implementation("org.springframework.boot:spring-boot-starter-aop")

    testImplementation(project(":common", "testRuntime"))
    testImplementation("com.ninja-squad:springmockk:3.0.1")

}
