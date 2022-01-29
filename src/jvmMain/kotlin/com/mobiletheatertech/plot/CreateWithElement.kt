package com.mobiletheatertech.plot

import org.w3c.dom.Element

abstract class CreateWithElement<Type:Elemental> {

  val Instances = mutableListOf<Type>()

  fun create(element: Element, factory: (Element) -> Type): Type {
    val instance: Type = factory(element)
    Instances.add(instance)
    return instance
  }

}