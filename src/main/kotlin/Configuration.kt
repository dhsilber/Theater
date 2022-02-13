package com.mobiletheatertech.plot

import java.io.File

class Configuration {
  companion object {
    var plotDirectory = System.getenv("THEATER_PLOT_DIRECTORY")
      ?: (System.getProperty("user.home") + "/TheaterPlot")
    var backupDirectory = "$plotDirectory/backups"
    var plotFilename = System.getenv("THEATER_PLOT_FILENAME") ?: "plotfile.xml"

    init {
      val file = File(plotDirectory)
      if (!file.exists()) {
        println("$plotDirectory does not exist")
      }
    }
  }
}