package com.muebles.ra.server.config

import com.muebles.ra.api.Furniture
import com.muebles.ra.api.FurnitureURL
import com.muebles.ra.secrets.URLObtainer
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import org.springframework.context.annotation.AnnotationConfigApplicationContext

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

    post("/furniture/uploader") {
        val furniture = call.receive<Furniture>()
        if (furniture.valid()) {
            ctx.getUrlObtainer().uploadUrl(furniture)
                .fold({ url -> call.respond(FurnitureURL(url.toString())) }) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to it.toString()))
                }
        } else {
            call.respond(HttpStatusCode.BadRequest,"A field of the furniture is null or empty")
        }
    }

    post("/furniture/downloader") {
        ctx.getUrlObtainer().downloadUrl(call.receive<Furniture>())
            .fold({ url ->
                run {
                    call.respond(FurnitureURL(url.toString()))
                }
            }) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to it.toString()))
            }
    }
}