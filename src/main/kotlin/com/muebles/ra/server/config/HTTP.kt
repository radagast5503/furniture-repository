package com.muebles.ra.server.config

import com.muebles.ra.utils.Version
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.httpConfig() {
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }
    install(DefaultHeaders) {
        header("Server-Version", Version.getVersion()) // will send this header with each response
    }
}
