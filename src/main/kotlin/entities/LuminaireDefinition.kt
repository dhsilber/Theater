package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class LuminaireDefinition(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val name = getStringAttribute("name")
  val weight = getPositiveFloatAttribute("weight")
  val complete = getBooleanAttribute("complete")
  val width = getOptionalPositiveFloatAttribute("width")
  val length = getOptionalPositiveFloatAttribute("length")
  val svgContent = getSvg()

  init {
  }

  override fun toString(): String {
    return "LuminaireDefinition $name - weight: $weight."
  }

  companion object : CreateWithXmlElement<LuminaireDefinition>() {
    const val Tag = "luminaire-definition"

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): LuminaireDefinition =
      create(xmlElement, parentEntity, ::LuminaireDefinition)

    fun findByName(name: String): LuminaireDefinition? {
      val candidates = instances.filter { it.name == name }
      return if (candidates.isEmpty())
        null
      else
        candidates[0]
    }
  }
}

