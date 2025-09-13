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
    implementation(project(":commerce-core"))
    testImplementation(testFixtures(project(":commerce-core")))

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.testcontainers:mysql")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testFixturesImplementation("org.testcontainers:mysql")
}
