import org.w3c.dom.Element

abstract class CreateWithXmlElement<Klass:XmlElemental> {

  val Instances = mutableListOf<Klass>()

  fun create(xmlElement: Element, factory: (Element) -> Klass): Klass {
    val instance: Klass = factory(xmlElement)
    Instances.add(instance)
    return instance
  }

}
