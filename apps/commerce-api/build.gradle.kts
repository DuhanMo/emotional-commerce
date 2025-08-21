plugins {
    id("org.jetbrains.kotlin.plugin.jpa")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    // add-ons
    implementation(project(":modules:jpa"))
    implementation(project(":modules:redis"))
    implementation(project(":modules:clients:client-pg"))
    implementation(project(":supports:jackson"))
    implementation(project(":supports:logging"))
    implementation(project(":supports:monitoring"))

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.properties["springDocOpenApiVersion"]}")

    // jdsl
    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:${project.properties["jdslVersion"]}")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:${project.properties["jdslVersion"]}")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:${project.properties["jdslVersion"]}")

    // test-fixtures
    testImplementation(testFixtures(project(":modules:jpa")))
    testImplementation(testFixtures(project(":modules:redis")))

    // resilience4j
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("io.github.resilience4j:resilience4j-spring-boot3")
}
