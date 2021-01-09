dependencies {

    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":api"))
    implementation(project(":oss"))
    implementation(project(":script"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}
