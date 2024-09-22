import org.w3c.dom.Element

class TagRegistry {

  companion object {
    val tagToCallback: MutableMap<String, (Element, XmlElemental?) -> XmlElemental> = mutableMapOf()

    fun registerProvider(tag: String, xmlElement: Element, parentEntity: XmlElemental?)
        : XmlElemental? {
      val processor = tagToCallback[tag]
      return processor?.invoke(xmlElement, parentEntity)
    }

    fun registerTagProcessor(tag: String, entityFactory: (Element, XmlElemental?) -> XmlElemental) {
      tagToCallback[tag] = entityFactory
    }

  }
}

interface XmlCompanion {
  val xmlElement: Element
}
