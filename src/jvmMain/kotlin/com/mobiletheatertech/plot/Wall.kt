package com.mobiletheatertech.plot

import org.w3c.dom.Element

class Wall (elementPassthrough: Element) : Elemental(elementPassthrough) {

  val x1 = getDoubleAttribute( "x1" )
  val y1 = getDoubleAttribute( "y1" )
  val x2 = getDoubleAttribute( "x2" )
  val y2 = getDoubleAttribute( "y2" )

//  var start = Point( x1, y1, 0.0 )
//  var end = Point( x2, y2, 0.0 )


  companion object : CreateWithElement<Wall>() {
    const val Tag = "wall"
    fun factory(element: Element): Wall = create(element, ::Wall)
  }
}