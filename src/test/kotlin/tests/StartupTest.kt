package tests

import TagRegistry
import Xml
import Startup
import entities.Drawing
import entities.Event
import entities.Floor
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import entities.PipeBase
import entities.Proscenium
import entities.Setpiece
import entities.SetPlatform
import entities.Shape
import entities.Venue
import entities.Wall
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.assertEquals

class StartupTest {
  @AfterTest
  fun cleanup(){
    clearAllMocks()
  }

  @Test
  fun `registers Plot objects to get their XML elements`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsOnlyKeys(
      Venue.Tag,
      Wall.Tag,
      Floor.Tag,
      Proscenium.Tag,
      Pipe.Tag,
      PipeBase.Tag,
      LuminaireDefinition.Tag,
      Luminaire.Tag,
      Event.Tag,
      Setpiece.Tag,
      SetPlatform.Tag,
      Shape.Tag,
      Drawing.Tag,
    )
  }

  @Test
  fun `invokes Xml read with provided pathname`() {
    val pathnameSlot = slot<String>()
    mockkObject(Xml)
    every { Xml.read(pathName = capture(pathnameSlot)) } returns Unit

    Startup.startup("foo")

    verify(exactly = 1) { Xml.read(pathName = "foo") }
  }

  @Test
  fun `pipes are given an opportunity to perform post-parsing work`() {
    Proscenium.instances.clear()
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
      Proscenium.factory(prosceniumElement, null)
    }
    every { Pipe.postParsingCleanup()} returns Unit

    Startup.startup("foo")

    verify(exactly = 1) { Pipe.postParsingCleanup() }
  }

}
