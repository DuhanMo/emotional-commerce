tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":commerce-core"))

    implementation(project(":supports:jackson"))
    implementation(project(":supports:logging"))
    implementation(project(":supports:monitoring"))

    runtimeOnly(project(":commerce-infrastructure:db"))

    // test-fixtures
    testImplementation(testFixtures(project(":commerce-core")))
    testImplementation(testFixtures(project(":commerce-infrastructure:db")))
}
