plugins {
    id("java-test-fixtures")
}

tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

dependencies {
    implementation(project(":commerce-domain"))
    testImplementation(testFixtures(project(":commerce-domain")))

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.testcontainers:mysql")

    testFixturesImplementation("org.testcontainers:mysql")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
