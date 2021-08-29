package com.muebles.ra.server.config

import com.muebles.ra.secrets.URLObtainer
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Service
import javax.inject.Inject

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

fun AnnotationConfigApplicationContext.getUrlObtainer(): URLObtainer = getBean(URLObtainer::class.java)

fun Application.router(ctx: AnnotationConfigApplicationContext) {
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

        furnitureRoutes(ctx)
    }
}

fun Route.furnitureRoutes(ctx: AnnotationConfigApplicationContext) {

    get("/furniture/{name}/uploader") {
        ctx.getUrlObtainer().uploadUrl(call.parameters["name"])
            .fold({ url -> call.respond(mapOf("upload_url" to url.toString())) }) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to it.toString()))
            }
    }

    get("/furniture/{name}/downloader") {
        ctx.getUrlObtainer().downloadUrl(call.parameters["name"])
            .fold({ url ->
                run {
                    call.respond(mapOf("download_url" to url.toString()))
                }
            }) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to it.toString()))
            }
    }
}