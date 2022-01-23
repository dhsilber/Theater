package com.mobiletheatertech.plot

import com.mobiletheatertech.plot.exception.ConfigurationException
import java.io.File
import java.nio.file.StandardCopyOption.COPY_ATTRIBUTES
import java.nio.file.Files.copy
import java.time.Instant
import kotlin.io.path.Path

class Backup {
  companion object {
    lateinit var BackupDirectory: File
    fun backup(sourcePathName: String) {
      if (!::BackupDirectory.isInitialized) {
        BackupDirectory = File(Configuration.backupDirectory)
        throw ConfigurationException("Backup directory is not set")
      }

      if (!BackupDirectory.isDirectory) BackupDirectory.mkdirs()

      val basename = sourcePathName.split('/').last().removeSuffix(".xml")
      val directoryName = "$BackupDirectory/$basename"
      val directory = File(directoryName)
      directory.mkdir()

      val sourcePath = Path(sourcePathName)
      val now = Instant.now()
      val backupPath = Path("$directoryName/$now.xml")
      copy(sourcePath, backupPath, COPY_ATTRIBUTES)
    }
  }
}