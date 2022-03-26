package tests

import TagRegistry
import Xml
import com.mobiletheatertech.plot.Startup
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import entities.Proscenium
import entities.Venue
import entities.Wall
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertEquals

class StartupTest {
  @Test
  fun `registers Plot objects to get their XML elements`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup().startup("foo")

    assertThat(TagRegistry.tagToCallback).containsOnlyKeys(
      LuminaireDefinition.Tag,
      Venue.Tag,
      Proscenium.Tag,
      Wall.Tag,
      Pipe.Tag,
      Luminaire.Tag,
    )
  }

  @Test
  fun `invokes Xml read with provided pathname`() {
    val pathnameSlot = slot<String>()
    mockkObject(Xml)
    every { Xml.read(pathName = capture(pathnameSlot)) } returns Unit

    Startup().startup("foo")

    verify(exactly = 1) { Xml.read(pathName = "foo") }
  }

  @Test
  fun `reorders pipe coordinates when we have a proscenium`() {
    val prosceniumElement = IIOMetadataNode()
    prosceniumElement.setAttribute("x", "1.2")
    prosceniumElement.setAttribute("y", "2.3")
    prosceniumElement.setAttribute("z", "3.4")
    prosceniumElement.setAttribute("height", "4.5")
    prosceniumElement.setAttribute("width", "5.6")
    prosceniumElement.setAttribute("depth", "6.7")
    assertEquals(false, Proscenium.inUse())

    mockkObject(Xml)
    mockkObject(Pipe)
    every { Xml.read(any()) } answers {
      Proscenium.factory(prosceniumElement)
    }
    every { Pipe.reorientForProsceniumOrigin()} returns Unit

    Startup().startup("foo")

    verify(exactly = 1) { Pipe.reorientForProsceniumOrigin() }
  }

  @Test
  fun `does not reorder pipe coordinates when we have no proscenium`() {
    Proscenium.Instances.clear()
    mockkObject(Xml)
    mockkObject(Pipe)
    every { Xml.read(any()) } returns Unit
    every { Pipe.reorientForProsceniumOrigin()} returns Unit

    Startup().startup("foo")

    verify(exactly = 0) { Pipe.reorientForProsceniumOrigin() }
  }
}
