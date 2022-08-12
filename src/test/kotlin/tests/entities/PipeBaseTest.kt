package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import coordinates.StagePoint
import entities.Pipe
import entities.PipeBase
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

internal class PipeBaseTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "0.1")
    xmlElement.setAttribute("y", "0.2")
    xmlElement.setAttribute("z", "0")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode()

    val instance = PipeBase.factory(xmlElement, null)

    assertIs<XmlElemental>(instance)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<PipeBase>>(PipeBase)
  }

  @Test
  fun `companion has tag`() {
    assertThat(PipeBase.Tag).isEqualTo("pipebase")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(PipeBase.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<PipeBase>(PipeBase.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = PipeBase.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.origin).isEqualTo(StagePoint(0.1F, 0.2f, 0f))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = minimalXml()
    xmlElement.setAttribute("id", "id name")
    xmlElement.setAttribute("owner", "owner name")

    val instance = PipeBase.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.id).isEqualTo("id name")
      assertThat(instance.owner).isEqualTo("owner name")
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = PipeBase.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required x attribute",
        "Missing required y attribute",
        "Missing required z attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")
    xmlElement.setAttribute("z", "zee")

    val instance = PipeBase.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read floating-point number from x attribute",
        "Unable to read floating-point number from y attribute",
        "Unable to read floating-point number from z attribute",
      )
    }.assertAll()
  }

  @Test
  fun `find pipe base by id`() {
    PipeBase.instances.clear()
    val xmlElement = minimalXml()
    xmlElement.setAttribute("id", "name")
    val instance = PipeBase.factory(xmlElement, null)

    val foundPipeBase = PipeBase.queryById("name")

    assertThat(foundPipeBase).isSameAs(instance)
  }

  @Test
  fun `do not find pipe base by id`() {
    PipeBase.instances.clear()

    val foundPipeBase = PipeBase.queryById("name")

    assertThat(foundPipeBase).isNull()
  }

  @Test
  fun `keep a reference to its child`(){
    val instance = PipeBase.factory(minimalXml(), null)
    val pipeInstance = Pipe.factory(PipeTest().minimalXmlWithPipeBaseParent(), instance)

    instance.mount(pipeInstance)

    assertThat(instance.upright).isSameAs(pipeInstance)
  }
}
