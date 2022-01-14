package com.mobiletheatertech.plot.exception

import java.lang.Exception

/**
 * Complain about invalid XML.
 *
 * @author dhs
 * @since 0.0.2
 */
class InvalidXMLException : Exception {
  /**
   * Constructs an instance of `InvalidXMLException` with the message specified.
   *
   * @param message Message to display for this exception
   */
  constructor(message: String?) : super(message) {}

  /**
   * Constructs an instance of `InvalidXMLException` with a message built with
   * the details provided.
   *
   * @param tag       XML element tag
   * @param id        Identification of a specific XML element - may be `null`
   * @param message   Message to present
   * @since 0.0.24
   */
  constructor(tag: String, id: String?, message: String) : super(
    tag + " "
        + (if (null == id || id.isEmpty()) "instance" else "($id)")
        + " " + message + "."
  ) {
  }
}