package com.mobiletheatertech.plot

class Startup {
  fun startup( pathName:String) {
    TagRegistry.registerConsumer(Luminaire.Tag, Luminaire::factory)
    TagRegistry.registerConsumer(Wall.Tag, Wall::factory)


    println("Reading Theater Plot XML file at ${pathName}")
    Xml.read(pathName)
  }
}
