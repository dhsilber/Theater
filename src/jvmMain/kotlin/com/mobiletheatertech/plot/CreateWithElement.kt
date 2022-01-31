package com.mobiletheatertech.plot

import org.w3c.dom.Element

abstract class CreateWithElement<Klass:Elemental> {

  val Instances = mutableListOf<Klass>()

  fun create(element: Element, factory: (Element) -> Klass): Klass {
    val instance: Klass = factory(element)
    Instances.add(instance)
    return instance
  }

}