tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":commerce-core"))
    implementation(project(":commerce-infrastructure:db"))

    implementation(project(":supports:jackson"))
    implementation(project(":supports:logging"))
    implementation(project(":supports:monitoring"))

    testImplementation(testFixtures(project(":commerce-core")))

    // test-fixtures
    testImplementation(testFixtures(project(":commerce-infrastructure:db")))
}
