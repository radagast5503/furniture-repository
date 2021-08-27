package com.muebles.ra.server.config

import com.muebles.ra.utils.Config
import io.ktor.application.*
import org.springframework.context.annotation.AnnotationConfigApplicationContext

fun Application.injectionManagement() {
    val ctx = AnnotationConfigApplicationContext("com.muebles.ra")
    val cfgManagement = ctx.getBean(Config::class.java)
    println(cfgManagement.bucketName())
    println(cfgManagement.privateKeyName())
    println(cfgManagement.urlDuration())
    println(cfgManagement.projectName())
}