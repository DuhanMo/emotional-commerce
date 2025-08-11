package com.loopers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class CommerceApiApplication {

    /*    @PostConstruct
        fun started() {
            // set timezone
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
        }*/
}

fun main(args: Array<String>) {
    runApplication<CommerceApiApplication>(*args)
}
