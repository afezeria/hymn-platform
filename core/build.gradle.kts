dependencies {

    implementation(project(":common"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation(Deps.SpringBoot.aop)
    implementation(Deps.SpringBoot.web)

    implementation(Deps.Swagger.core)

    implementation(Deps.Sql.jsqlparser)

    implementation(Deps.Ktorm.core)
    implementation(Deps.Ktorm.postgresql)
    testImplementation(project(":common", "testArtifacts"))
}
