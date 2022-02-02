package com.mobiletheatertech.plot

import io.ktor.application.Application
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing

//class Control {

  fun Route.writeSvg() {
    println("Registering writeSvg route")
    route("/svg") {
      get() {
//      Generate Svg & return it
        println("Generating SVG.")
        Svg.write()
      }
    }
  }

  fun Application.writeSvg() {
    routing {
      writeSvg()
    }
  }

//}
