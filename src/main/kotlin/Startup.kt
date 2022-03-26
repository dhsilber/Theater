package com.mobiletheatertech.plot

import entities.Venue
import entities.Luminaire
import entities.Pipe
import entities.Proscenium
import entities.Wall

class Startup {
  fun startup( pathName:String) {
    TagRegistry.registerConsumer(Venue.Tag, Venue::factory)
    TagRegistry.registerConsumer(Proscenium.Tag, Proscenium::factory)
    TagRegistry.registerConsumer(Luminaire.Tag, Luminaire::factory)
    TagRegistry.registerConsumer(Wall.Tag, Wall::factory)
    TagRegistry.registerConsumer(Pipe.Tag, Pipe::factory)

    println("Reading Theater Plot XML file at $pathName")
    Xml.read(pathName)

    if (Proscenium.inUse()) {
      Pipe.reorientForProsceniumOrigin()
    }
  }
}
