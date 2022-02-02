package com.mobiletheatertech.plot

import kotlinx.html.dom.append
import org.w3c.dom.Document
import org.w3c.dom.Element

class Wall (elementPassthrough: Element) : XmlElemental(elementPassthrough) {

  val x1 = getDoubleAttribute( "x1" )
  val y1 = getDoubleAttribute( "y1" )
  val x2 = getDoubleAttribute( "x2" )
  val y2 = getDoubleAttribute( "y2" )

//  var start = Point( x1, y1, 0.0 )
//  var end = Point( x2, y2, 0.0 )

  fun svg(svgDocuemnt: Document, svgNamespace: String, parentElement: Element) {
    println("Drawing the wall from $x1,$y1 to $x2,$y2.")

    val svgElement = svgDocuemnt.createElementNS( svgNamespace, "line" )
    svgElement.setAttribute("x1", x1.toString())
    svgElement.setAttribute("y1", y1.toString())
    svgElement.setAttribute("x2", x2.toString())
    svgElement.setAttribute("y2", y2.toString())
    svgElement.setAttribute("stroke", "black")
    svgElement.setAttribute( "stroke-width", "2" );
    parentElement.appendChild(svgElement)
  }

  companion object : CreateWithXmlElement<Wall>() {
    const val Tag = "wall"
    fun factory(xmlElement: Element): Wall = create(xmlElement, ::Wall)
  }

}
