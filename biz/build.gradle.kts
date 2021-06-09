dependencies {

    api(project(":common"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation(Deps.SpringBoot.aop)
    implementation(Deps.SpringBoot.web)

    api(Deps.Swagger.core)
    api(Deps.Swagger.ui)

    api(Deps.Ktorm.core)
    api(Deps.Ktorm.postgresql)
    testImplementation(project(":common", "testArtifacts"))
}
