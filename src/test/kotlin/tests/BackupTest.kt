package tests

import com.mobiletheatertech.plot.Backup
import com.mobiletheatertech.plot.Configuration
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.time.Instant
import java.util.*
import java.util.regex.Pattern
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BackupTest {
  lateinit var randomFileName: String

  fun setup() {
    randomFileName = UUID.randomUUID().toString()
    println( "randomFileName: $randomFileName")
    Configuration.backupDirectory = "/tmp/$randomFileName/backup"
    Backup.BackupDirectory = File(Configuration.backupDirectory)

  }

  @After
  fun cleanUp() {
    File("/tmp/$randomFileName").delete()
  }

  @Test
  fun `creates backup directory if it does not exist`() {
    setup()
//    Backup.BackupDirectory = File(Configuration.backupDirectory)
    assertFalse(Backup.BackupDirectory.exists())

    Backup.backup("")

    assertTrue(Backup.BackupDirectory.isDirectory)
  }

  @Test
  fun `creates targeted backup directory for specified file if it does not exist`() {
    setup()
//    Backup.BackupDirectory = File(Configuration.backupDirectory)
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
//    Backup.BackupDirectory = File(Configuration.backupDirectory)
    val filename = "larger.xml"
    val targetedBackupDirectory = File("${Configuration.backupDirectory}/$filename".removeSuffix(".xml"))
    println( "targetedBackupDirectory: $targetedBackupDirectory")
    val sourcePathName = this.javaClass.classLoader.getResource(filename)!!.file

    Backup.backup(sourcePathName)

    val now = Instant.now()
    assertEquals(1, targetedBackupDirectory.list().size)
    val generatedFilename = targetedBackupDirectory.list()[0]
    val nowPattern = now.toString().split('.')[0] + "\\.\\d{6}Z\\.xml"
    println(generatedFilename.toString())
    assertTrue(Pattern.matches(nowPattern, generatedFilename), "Unable to match $nowPattern")
    val generatedFile = File("$targetedBackupDirectory/$generatedFilename")
    val sourceContents = Files.readString(File(sourcePathName).toPath())
    assertEquals(sourceContents, Files.readString(generatedFile.toPath()))
  }

}