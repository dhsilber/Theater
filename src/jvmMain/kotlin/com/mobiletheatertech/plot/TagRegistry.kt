package com.mobiletheatertech.plot

import org.w3c.dom.Element

class TagRegistry {

  companion object {
    val tagToCallback: MutableMap<String, (Element) -> Unit> = mutableMapOf()

    fun registerProvider(tag: String, element: Element) {
      tagToCallback[tag]?.invoke(element)
    }

    fun registerConsumer(tag: String, callback: (Element) -> Unit) {
      tagToCallback.put(tag, callback)
    }

  }
}

interface XmlCompanion {
  val element: Element
}