package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class PipeBase(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val origin = getStagePointAttribute("x", "y", "z")
  val id = getOptionalStringAttribute("id")
  val owner = getOptionalStringAttribute("owner")

  var upright: Pipe? = null

  fun mount(pipe: Pipe) {
    upright = pipe
  }

  companion object : CreateWithXmlElement<PipeBase>() {
    const val Tag = "pipebase"
    const val Radius = 18f
    const val Height = 3f

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): PipeBase =
      PipeBase.create(xmlElement, parentEntity, ::PipeBase)

    fun queryById(id: String): PipeBase? {
      PipeBase.instances.forEach {
        if (id == it.id) {
          return it
        }
      }
      return null
    }

  }

}