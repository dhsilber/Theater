package com.mobiletheatertech.plot

import entities.Venue
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import entities.Proscenium
import entities.Wall

class Startup {
  fun startup(pathName: String) {
    TagRegistry.registerTagProcessor(LuminaireDefinition.Tag, LuminaireDefinition::factory)
    TagRegistry.registerTagProcessor(Venue.Tag, Venue::factory)
    TagRegistry.registerTagProcessor(Proscenium.Tag, Proscenium::factory)
    TagRegistry.registerTagProcessor(Wall.Tag, Wall::factory)
    TagRegistry.registerTagProcessor(Pipe.Tag, Pipe::factory)
    TagRegistry.registerTagProcessor(Luminaire.Tag, Luminaire::factory)

    println("Reading Theater Plot XML file at $pathName")
    Xml.read(pathName)

    Pipe.postParsingCleanup()
  }
}
