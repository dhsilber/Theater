import org.w3c.dom.Element

abstract class CreateWithXmlElement<Klass : XmlElemental> {

  val Instances = mutableListOf<Klass>()

  fun create(
    xmlElement: Element,
    parentEntity: XmlElemental?,
    factory: (Element, XmlElemental?) -> Klass
  ): Klass {
    val instance: Klass = factory(xmlElement, parentEntity)
    Instances.add(instance)
    return instance
  }

}
