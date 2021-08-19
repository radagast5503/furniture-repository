package com.muebles.ra

import com.muebles.ra.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty,8080, host = "0.0.0.0") {
        router()
        httpConfig()
        monitoring()
        serialization()
    }.start(wait = true)
}