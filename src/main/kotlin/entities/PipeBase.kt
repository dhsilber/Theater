package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class PipeBase(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val origin = getStagePointAttribute("x", "y", "z")
  val id = getOptionalStringAttribute("id")
  val owner = getOptionalStringAttribute("owner")

  companion object : CreateWithXmlElement<PipeBase>() {
    const val Tag = "pipebase"
    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): PipeBase =
      PipeBase.create(xmlElement, parentEntity, ::PipeBase)
  }

}