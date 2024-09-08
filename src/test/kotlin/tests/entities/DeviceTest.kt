package tests.entities

import CreateWithXmlElement
import Startup
import TagRegistry
import Xml
import XmlElemental
import entities.Device
import entities.DeviceTemplate
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class DeviceTest {
  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "ID name")
    xmlElement.setAttribute("is", "type name")
    xmlElement.setAttribute("x", "2.3")
    xmlElement.setAttribute("y", "4.5")
    xmlElement.setAttribute("z", "6.7")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
    DeviceTemplate.clear()
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val device = Device.factory(xmlElement, null)

    assertIs<XmlElemental>(device)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Device>>(Device)
  }

  @Test
  fun `companion has tag`() {
    Assertions.assertThat(Device.Tag).isEqualTo("device")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    Assertions.assertThat(TagRegistry.tagToCallback).containsKey(Device.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Device>(Device.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val deviceTemplate = DeviceTemplate.factory(DeviceTemplateTest().minimalXml())

    val instance = Device.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.id).isEqualTo("ID name")
      assertThat(instance.isa).isEqualTo(deviceTemplate)
      assertThat(instance.x).isEqualTo(2.3f)
      assertThat(instance.y).isEqualTo(4.5f)
      assertThat(instance.z).isEqualTo(6.7f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    DeviceTemplate.factory(DeviceTemplateTest().minimalXml())
    val xmlElement = minimalXml()
    xmlElement.setAttribute("orientation", "30")
    xmlElement.setAttribute("layer", "layer name")

    val instance = Device.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.orientation).isEqualTo(30f)
      assertThat(instance.layer).isEqualTo("layer name")
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Device.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required id attribute",
        "Missing required is attribute",
        "Unable to find device-template \"\"",
        "Missing required x attribute",
        "Missing required y attribute",
        "Missing required z attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "id name")
    xmlElement.setAttribute("is", "undefined device")
    xmlElement.setAttribute("x", "Type value")
    xmlElement.setAttribute("y", "bogus17.6")
    xmlElement.setAttribute("z", "Owner name")

    val instance = Device.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to find device-template \"undefined device\"",
        "Unable to read floating-point number from x attribute",
        "Unable to read floating-point number from y attribute",
        "Unable to read floating-point number from z attribute",
      )
    }.assertAll()
  }


}
