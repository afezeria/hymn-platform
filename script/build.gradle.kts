dependencies {

    implementation(project(":common"))
    implementation(project(":core"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation(Deps.Graal.js)
    implementation(Deps.Graal.jsScriptEngine)
    implementation(Deps.Graal.sdk)
    implementation(Deps.Graal.truffleApi)

    implementation(Deps.Sql.jsqlparser)

    implementation(Deps.Cache.ehcache)

    implementation(Deps.Apache.pool)

    implementation(Deps.SpringBoot.web)

    implementation(Deps.Swagger.core)

    testImplementation(project(":common", "testRuntime"))
    testImplementation("com.ninja-squad:springmockk:3.0.1")

}
