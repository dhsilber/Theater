package tests

import com.mobiletheatertech.plot.Backup
import com.mobiletheatertech.plot.Configuration
import com.mobiletheatertech.plot.Luminaire
import com.mobiletheatertech.plot.Startup
import org.junit.Test
import java.io.File
import java.nio.file.Files
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntegrationTest {

  @Test
  fun `read XML with luminaire tag - read, change, write attributes`() {
    val pristineFile = File(this.javaClass.classLoader.getResource("pristineLuminaire.xml")!!.file)
    Backup.BackupDirectory = File(Configuration.backupDirectory)
    TagRegistry.registerConsumer(Luminaire.Tag, Luminaire.Companion::factory)
    val existingCount = Luminaire.Instances.size
    val filename = "luminaire.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    pristineFile.copyTo(File(pathName), overwrite = true)
    Startup().startup(pathName)

    assertEquals(3 + existingCount, Luminaire.Instances.size)
    val zero = Luminaire.Instances[0 + existingCount]
    val one = Luminaire.Instances[1 + existingCount]
    val two = Luminaire.Instances[2 + existingCount]
    assertFalse(zero.hasError)
    assertTrue(one.hasError)
    assertTrue(two.hasError)
    val sourceContents = Files.readString(File(pathName).toPath())
    assertTrue(
      sourceContents.contains(
        "<luminaire type=\"bozzle\" address=\"321\"/>"
      ),
      "$pathName as source contains:\n$sourceContents"
    )

    zero.address = 12

    val resultingContents = Files.readString(File(pathName).toPath())
    assertTrue(
      resultingContents.contains(
        "<luminaire address=\"12\" type=\"bozzle\"/>"
      ),
      "$pathName as result contains:\n$resultingContents"
    )

  }

}