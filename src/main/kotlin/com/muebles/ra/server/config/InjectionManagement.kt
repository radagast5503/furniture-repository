package com.muebles.ra.server.config

import io.ktor.application.*
import org.springframework.context.annotation.AnnotationConfigApplicationContext

fun Application.injectionManagement(): AnnotationConfigApplicationContext {
    return AnnotationConfigApplicationContext("com.muebles.ra")
}