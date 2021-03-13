dependencies {

    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":biz"))
    implementation(project(":oss"))
    implementation(project(":script"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation(Deps.SpringBoot.web)
}
