import coordinates.VenuePoint
import entities.*

class Startup {

  companion object {

    fun startup(pathName: String) {
      TagRegistry.registerTagProcessor(LuminaireDefinition.Tag, LuminaireDefinition::factory)
      TagRegistry.registerTagProcessor(Venue.Tag, Venue::factory)
      TagRegistry.registerTagProcessor(Proscenium.Tag, Proscenium::factory)
      TagRegistry.registerTagProcessor(Floor.Tag, Floor::factory)
      TagRegistry.registerTagProcessor(Wall.Tag, Wall::factory)
      TagRegistry.registerTagProcessor(Stair.Tag, Stair::factory)
      TagRegistry.registerTagProcessor(Pipe.Tag, Pipe::factory)
      TagRegistry.registerTagProcessor(Luminaire.Tag, Luminaire::factory)
      TagRegistry.registerTagProcessor(Event.Tag, Event::factory)
      TagRegistry.registerTagProcessor(PipeBase.Tag, PipeBase::factory)
      TagRegistry.registerTagProcessor(Raceway.Tag, Raceway::factory)
      TagRegistry.registerTagProcessor(Setpiece.Tag, Setpiece::factory)
      TagRegistry.registerTagProcessor(Flat.Tag, Flat::factory)
      TagRegistry.registerTagProcessor(SetPlatform.Tag, SetPlatform::factory)
      TagRegistry.registerTagProcessor(Shape.Tag, Shape::factory)
      TagRegistry.registerTagProcessor(Drawing.Tag, Drawing::factory)

      println("Reading Theater Plot XML file at $pathName")
      Xml.read(pathName)

      Pipe.postParsingCleanup()
    }

    fun clear() {
      TagRegistry.tagToCallback.clear()

      LuminaireDefinition.clear()
      Venue.clear()
      Floor.clear()
      Proscenium.clear()
      Wall.clear()
      Stair.clear()
      Pipe.clear()
      Luminaire.clear()
      Event.clear()
      PipeBase.clear()
      Raceway.clear()
      Setpiece.clear()
      Flat.clear()
      SetPlatform.clear()
      Shape.clear()
      Drawing.clear()

      VenuePoint.clear()
    }
  }
}
