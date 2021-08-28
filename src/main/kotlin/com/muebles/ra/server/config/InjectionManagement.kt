package com.muebles.ra.server.config

import com.muebles.ra.utils.Config
import io.ktor.application.*
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Configuration

fun Application.injectionManagement() {
    val ctx = AnnotationConfigApplicationContext("com.muebles.ra")
}