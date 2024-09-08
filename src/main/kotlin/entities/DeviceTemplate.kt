package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class DeviceTemplate(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val type = getStringAttribute("type")
  val width = getPositiveFloatAttribute("width")
  val depth = getPositiveFloatAttribute("depth")
  val height = getPositiveFloatAttribute("height")
  val layer = getOptionalStringAttribute("layer")
  val color = getOptionalStringAttribute("color")

  companion object : CreateWithXmlElement<DeviceTemplate>() {
    const val Tag = "device-template"

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): DeviceTemplate =
      DeviceTemplate.create(xmlElement, parentEntity, ::DeviceTemplate)

    fun findByType(type: String): DeviceTemplate? {
      val candidates = DeviceTemplate.instances.filter { it.type == type }
      return if (candidates.isEmpty())
        null
      else
        candidates[0]
    }
  }
}
