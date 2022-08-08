import entities.Drawing
import entities.Event
import entities.Floor
import entities.Venue
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import entities.PipeBase
import entities.Proscenium
import entities.Setpiece
import entities.SetPlatform
import entities.Shape
import entities.Wall

class Startup {
  fun startup(pathName: String) {
    TagRegistry.registerTagProcessor(LuminaireDefinition.Tag, LuminaireDefinition::factory)
    TagRegistry.registerTagProcessor(Venue.Tag, Venue::factory)
    TagRegistry.registerTagProcessor(Floor.Tag, Floor::factory)
    TagRegistry.registerTagProcessor(Proscenium.Tag, Proscenium::factory)
    TagRegistry.registerTagProcessor(Wall.Tag, Wall::factory)
    TagRegistry.registerTagProcessor(Pipe.Tag, Pipe::factory)
    TagRegistry.registerTagProcessor(Luminaire.Tag, Luminaire::factory)
    TagRegistry.registerTagProcessor(Setpiece.Tag, Setpiece::factory)
    TagRegistry.registerTagProcessor(SetPlatform.Tag, SetPlatform::factory)
    TagRegistry.registerTagProcessor(Shape.Tag, Shape::factory)
    TagRegistry.registerTagProcessor(Drawing.Tag, Drawing::factory)
    TagRegistry.registerTagProcessor(Event.Tag, Event::factory)
    TagRegistry.registerTagProcessor(PipeBase.Tag, PipeBase::factory)

    println("Reading Theater Plot XML file at $pathName")
    Xml.read(pathName)

    Pipe.postParsingCleanup()
  }
}
