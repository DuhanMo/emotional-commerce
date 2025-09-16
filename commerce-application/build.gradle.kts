tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

dependencies {
    implementation(project(":commerce-domain"))
    api("org.springframework.boot:spring-boot-starter-web")

    // test-fixtures
    testImplementation(testFixtures(project(":commerce-domain")))
}
