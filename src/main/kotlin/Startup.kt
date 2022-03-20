package com.mobiletheatertech.plot

import Venue

class Startup {
  fun startup( pathName:String) {
    TagRegistry.registerConsumer(Venue.Tag, Venue::factory)
    TagRegistry.registerConsumer(Luminaire.Tag, Luminaire::factory)
    TagRegistry.registerConsumer(Wall.Tag, Wall::factory)


    println("Reading Theater Plot XML file at ${pathName}")
    Xml.read(pathName)
  }
}
