package com.soyoung.grad

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GradApplication

fun main(args: Array<String>) {
    runApplication<GradApplication>(*args)
}
