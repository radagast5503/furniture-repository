package com.muebles.ra.server.config

import com.google.gson.FieldNamingPolicy
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.serialization() {
    install(ContentNegotiation) {
        gson {
            this.disableHtmlEscaping()
            this.setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        }
    }
}
