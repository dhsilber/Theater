package com.mobiletheatertech.plot

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files.readString
import java.time.Instant
import java.util.UUID
import java.util.regex.Pattern
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNotEquals

class BackupTest {
  lateinit var randomFileName: String

  fun setup() {
    randomFileName = UUID.randomUUID().toString()
    Configuration.backupDirectory = "/tmp/$randomFileName/backup"
  }

  @AfterEach
  fun cleanUp() {
    File("/tmp/$randomFileName").delete()
  }

  @Test
  fun `creates backup directory if it does not exist`() {
    setup()
    Backup.BackupDirectory = File(Configuration.backupDirectory)
    assertFalse(Backup.BackupDirectory.exists())

    Backup.backup("")

    assertTrue(Backup.BackupDirectory.isDirectory)
  }

  @Test
  fun `creates targeted backup directory for specified file if it does not exist`() {
    setup()
    Backup.BackupDirectory = File(Configuration.backupDirectory)
    val filename = "tiny.xml"
    val targetedBackupDirectory = File("${Configuration.backupDirectory}/$filename".removeSuffix(".xml"))
    assertFalse(targetedBackupDirectory.exists())
    val sourcePathName = this.javaClass.classLoader.getResource(filename)!!.file

    Backup.backup(sourcePathName)

    assertTrue(targetedBackupDirectory.isDirectory)
  }

  @Test
  fun `copies file into targeted backup directory with name being incrementally larger numbers plus xml suffix`() {
    setup()
    Backup.BackupDirectory = File(Configuration.backupDirectory)
    val filename = "larger.xml"
    val targetedBackupDirectory = File("${Configuration.backupDirectory}/$filename".removeSuffix(".xml"))
    val sourcePathName = this.javaClass.classLoader.getResource(filename)!!.file

    Backup.backup(sourcePathName)

    val now = Instant.now()
    assertEquals(1, targetedBackupDirectory.list().size)
    val generatedFilename = targetedBackupDirectory.list()[0]
    val nowPattern = now.toString().split('.')[0] + "\\.\\d{6}Z\\.xml"
    assertTrue(Pattern.matches(nowPattern, generatedFilename), "Unable to match $nowPattern")
    val generatedFile = File("$targetedBackupDirectory/$generatedFilename")
    val sourceContents = readString(File(sourcePathName).toPath())
    assertEquals(sourceContents, readString(generatedFile.toPath()))
  }

}


