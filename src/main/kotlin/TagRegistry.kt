import org.w3c.dom.Element

class TagRegistry {

  companion object {
    val tagToCallback: MutableMap<String, (Element) -> Unit> = mutableMapOf()

    fun registerProvider(tag: String, xmlElement: Element) {
      println("registerProvider: $tag, $xmlElement, $tagToCallback")
      println("--> in registerProvider: $tag, ${tagToCallback[tag]}")
      tagToCallback[tag]?.invoke(xmlElement)
    }

    fun registerConsumer(tag: String, xmlElement: (Element) -> Unit) {
      println("registerConsumer: $tag, $xmlElement")
      tagToCallback.put(tag, xmlElement)
    }

  }
}

interface XmlCompanion {
  val xmlElement: Element
}