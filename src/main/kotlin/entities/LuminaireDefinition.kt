package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class LuminaireDefinition(elementPassthrough: Element) : XmlElemental(elementPassthrough) {
  var name = getStringAttribute("name")
  var weight = getPositiveFloatAttribute("weight")
  var complete = getBooleanAttribute("complete")
  var width = getOptionalPositiveFloatAttribute("width")
  var length = getOptionalPositiveFloatAttribute("length")

  init {
    println("New ${this.toString()}")
    println("Errors: $errors")
  }

  override fun toString(): String {
    return "LuminaireDefinition $name - weight: $weight."
  }

  companion object : CreateWithXmlElement<LuminaireDefinition>() {
    const val Tag = "luminaire-definition"
    const val Diameter = 2f

    fun factory(xmlElement: Element): LuminaireDefinition = create(xmlElement, ::LuminaireDefinition)
  }
}

