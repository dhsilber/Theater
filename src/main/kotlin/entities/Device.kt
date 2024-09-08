package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Device(elementPassthrough: Element, val parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val id = getStringAttribute("id")
  val isa = queryDeviceTemplate(getStringAttribute("is"))
  val x = getFloatAttribute("x")
  val y = getFloatAttribute("y")
  val z = getFloatAttribute("z")
  val orientation = getOptionalFloatAttribute("orientation")
  val layer = getOptionalStringAttribute("layer")

  private fun queryDeviceTemplate(type: String) : DeviceTemplate? {
    val template = DeviceTemplate.findByType(type)
    if (null === template) {
      errors.add("Unable to find device-template \"$type\"")
    }
    return template
  }

  companion object : CreateWithXmlElement<Device>() {
    const val Tag = "device"

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Device =
      Device.create(xmlElement, parentEntity, ::Device)
  }
}
