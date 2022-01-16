package com.mobiletheatertech.plot

class Startup {
  fun startup() {
    TagRegistry.registerConsumer(Luminaire.Tag, Luminaire::factory)

    val filename = "tiny.xml"
    val pathName = this.javaClass.classLoader.getResource(filename).file  //.readText()
    val fileContent = this.javaClass.classLoader.getResource(filename).readText()
    println("$pathName: $fileContent")

    Read().input(pathName)
  }
}
