dependencies {

    compile(project(":common"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation(Deps.SpringBoot.aop)
    implementation(Deps.SpringBoot.web)

    compile(Deps.Swagger.core)
    compile(Deps.Swagger.ui)

    compile(Deps.Ktorm.core)
    compile(Deps.Ktorm.postgresql)
    testImplementation(project(":common", "testArtifacts"))
}
