package com.mobiletheatertech.plot

import org.w3c.dom.Element

class Wall private constructor(val element: Element) : Elemental() {

  init {
    println("New Wall.")
  }

  companion object : CreateWithElement<Wall>() {
    const val Tag = "wall"
    fun factory(element: Element): Wall = create(element, ::Wall)
  }
}