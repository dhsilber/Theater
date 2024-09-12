import org.w3c.dom.Element

abstract class CreateWithXmlElement<Klass : XmlElemental> {

  val instances = mutableListOf<Klass>()

  fun create(
    xmlElement: Element,
    parentEntity: XmlElemental?,
    factory: (Element, XmlElemental?) -> Klass
  ): Klass {
    val instance: Klass = factory(xmlElement, parentEntity)
    instances.add(instance)
    MessageMinder.Messages.addAll(instance.errors)
    return instance
  }

  fun clear() = instances.clear()

}
