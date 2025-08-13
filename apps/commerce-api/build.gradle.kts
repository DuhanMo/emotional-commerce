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

    // retry
    implementation("org.springframework.retry:spring-retry")

    // test-fixtures
    testImplementation(testFixtures(project(":modules:jpa")))
}
