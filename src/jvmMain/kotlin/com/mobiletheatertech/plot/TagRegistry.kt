package com.mobiletheatertech.plot

import org.w3c.dom.Element

class TagRegistry {

  companion object {
    val tagToCallback: MutableMap<String, (Element) -> Unit> = mutableMapOf()

    fun registerProvider(tag: String, xmlElement: Element) {
      tagToCallback[tag]?.invoke(xmlElement)
    }

    fun registerConsumer(tag: String, xmlElement: (Element) -> Unit) {
      tagToCallback.put(tag, xmlElement)
    }

  }
}

interface XmlCompanion {
  val xmlElement: Element
}