package com.muebles.ra.server.config

import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*


//example
fun Application.router() {
    install(AutoHeadResponse)

    routing {
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }

    routing {
        get("/health-check") {
            call.respond(HttpStatusCode.OK, mapOf("hello" to "world"))
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
