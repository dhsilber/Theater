package com.mobiletheatertech.plot

import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import kotlinx.html.*

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/Theater.js") {}
    }
}

fun main() {
    Startup().startup()
    server()
}

class Startup {
    fun startup() {
        val filename = "tiny.xml"
        val pathName = this.javaClass.classLoader.getResource(filename).file  //.readText()
        val fileContent = this.javaClass.classLoader.getResource(filename).readText()
        println("$pathName: $fileContent")
        Read().input(pathName)
    }
}

fun server(){
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}