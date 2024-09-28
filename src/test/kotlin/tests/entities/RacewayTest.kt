package tests.entities

import CreateWithXmlElement
import Hangable
import Startup
import TagRegistry
import Xml
import XmlElemental
import coordinates.StagePoint
import coordinates.VenuePoint
import entities.Locator
import entities.Luminaire
import entities.Proscenium
import entities.Raceway
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class RacewayTest {

  fun minimalXmlWithNoParent(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("raceway")
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "1.2")
    xmlElement.setAttribute("y", "2.3")
    xmlElement.setAttribute("z", "3.4")
    xmlElement.setAttribute("length", "4.5")
    return xmlElement
  }

  fun minimalXmlWithRacewayBaseParent(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("raceway")
    xmlElement.setAttribute("length", "47.5")
    return xmlElement
  }

  private fun minimalXmlWithVerticalRacewayParent(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("raceway")
    xmlElement.setAttribute("length", "22.5")
    xmlElement.setAttribute("location", "38")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode("raceway")

    val raceway = Raceway.factory(xmlElement, null)

    assertIs<XmlElemental>(raceway)
  }

  @Test
  fun `is hangable`() {
    val xmlElement = IIOMetadataNode("raceway")

    val raceway = Raceway.factory(xmlElement, null)

    assertIs<Hangable>(raceway)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Raceway>>(Raceway)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Raceway.Tag).isEqualTo("raceway")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Raceway.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Raceway>(Raceway.factory(minimalXmlWithNoParent(), null))
  }

  @Test
  fun `has required attributes when there is no parent`() {
    val instance = Raceway.factory(minimalXmlWithNoParent(), null)

    SoftAssertions().apply {
      assertThat(instance.id).isEqualTo("name")
      assertThat(instance.origin).isEqualTo(StagePoint(1.2f, 2.3f, 3.4f))
      assertThat(instance.length).isEqualTo(4.5f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

//  @Test
//  fun `has required attributes when raceway base is parent`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//    val baseOrigin = racewayBase.origin
//    val expectedOrigin = StagePoint(baseOrigin.x, baseOrigin.y, baseOrigin.z + 2f)
//
//    val instance = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//
//    SoftAssertions().apply {
//      assertThat(instance.origin).isEqualTo(expectedOrigin)
//      assertThat(instance.length).isEqualTo(47.5f)
//      assertThat(instance.hasError).isFalse
//    }.assertAll()
//  }

//  @Test
//  fun `has required attributes when vertical raceway is parent`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//    val verticalRaceway = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//    val baseOrigin = racewayBase.origin
//    val expectedOrigin = StagePoint(baseOrigin.x - 11.25f, baseOrigin.y, baseOrigin.z + 40f)
//
//    val instance = Raceway.factory(minimalXmlWithVerticalRacewayParent(), verticalRaceway)
//
//    SoftAssertions().apply {
//      assertThat(instance.origin).isEqualTo(expectedOrigin)
//      assertThat(instance.length).isEqualTo(22.5f)
//      assertThat(instance.hasError).isFalse
//    }.assertAll()
//  }

//  @Test
//  fun `registers optional attributes when vertical raceway is parent`() {
//    val xmlElement = minimalXmlWithVerticalRacewayParent()
//    xmlElement.setAttribute("offsety", "30")
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//    val verticalRaceway = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//
//    val instance = Raceway.factory(xmlElement, verticalRaceway)
//
//    SoftAssertions().apply {
//      assertThat(instance.offsety).isEqualTo(30f)
//      assertThat(instance.hasError).isFalse
//    }.assertAll()
//  }

  @Test
  fun `notes error for missing required attributes when there is no parent`() {
    val xmlElement = IIOMetadataNode("raceway")

    val instance = Raceway.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsOnly(
        "raceway missing required id attribute",
        "raceway missing required x attribute",
        "raceway missing required y attribute",
        "raceway missing required z attribute",
        "raceway missing required length attribute",
      )
    }.assertAll()
  }

//  @Test
//  fun `notes error for missing required attributes when raceway base is parent`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//    val xmlElement = IIOMetadataNode("raceway")
//
//    val instance = Raceway.factory(xmlElement, racewayBase)
//
//    SoftAssertions().apply {
//      assertThat(instance.hasError).isTrue
//      assertThat(instance.errors).containsExactly(
//        "raceway missing required length attribute",
//      )
//    }.assertAll()
//  }

//  @Test
//  fun `notes error for missing required attributes when vertical raceway is parent`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//    val verticalRaceway = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//    val xmlElement = IIOMetadataNode("raceway")
//
//    val instance = Raceway.factory(xmlElement, verticalRaceway)
//
//    SoftAssertions().apply {
//      assertThat(instance.hasError).isTrue
//      assertThat(instance.errors).containsExactly(
//        "raceway missing required length attribute",
//        "raceway missing required location attribute",
//      )
//    }.assertAll()
//  }

//  @Test
//  fun `repositions origin due to offset attribute when vertical raceway is parent`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//    val verticalRaceway = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//    val baseOrigin = racewayBase.origin
//    val expectedOrigin = StagePoint(baseOrigin.x - 11.25f + 3f, baseOrigin.y, baseOrigin.z + 40f)
//    val xmlElement = minimalXmlWithVerticalRacewayParent()
//    xmlElement.setAttribute("offset", "3")
//
//    val instance = Raceway.factory(xmlElement, verticalRaceway)
//
//    SoftAssertions().apply {
//      assertThat(instance.origin).isEqualTo(expectedOrigin)
//      assertThat(instance.length).isEqualTo(22.5f)
//      assertThat(instance.hasError).isFalse
//    }.assertAll()
//  }

  @Test
  fun `notes error for attributes when vertical raceway is not parent`() {
    val xmlElement = minimalXmlWithNoParent()
    xmlElement.setAttribute("offset", "3")
    xmlElement.setAttribute("offsety", "30")

    val instance = Raceway.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsOnly(
        "raceway without a vertical raceway parent, the offset attribute is not allowed",
        "raceway unable to offset drawing of raceway when it is not a child of a vertical raceway",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("raceway")
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")
    xmlElement.setAttribute("z", "bogus.3")
    xmlElement.setAttribute("length", "bogus.4")

    val instance = Raceway.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsOnly(
        "raceway unable to read floating-point number from x attribute",
        "raceway unable to read floating-point number from y attribute",
        "raceway unable to read floating-point number from z attribute",
        "raceway unable to read positive floating-point number from length attribute",
      )
    }.assertAll()
  }

//  @Test
//  fun `notes error for badly specified attributes when vertical raceway is parent`() {
//    val xmlElement = minimalXmlWithVerticalRacewayParent()
//    xmlElement.setAttribute("offset", "three")
//    xmlElement.setAttribute("offsety", "thirty")
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//
//    val instance = Raceway.factory(xmlElement, racewayBase)
//
//    SoftAssertions().apply {
//      assertThat(instance.hasError).isTrue
//      assertThat(instance.errors).containsOnly(
//        "raceway unable to read optional floating-point number from offset attribute",
//        "raceway unable to read optional floating-point number from offsety attribute",
//      )
//    }.assertAll()
//  }

//  @Test
//  fun `tracks vertical status when there is no parent`() {
//    val instance = Raceway.factory(minimalXmlWithNoParent(), null)
//
//    assertThat(instance.vertical).isFalse
//  }

//  @Test
//  fun `tracks vertical status when raceway base is parent`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//
//    val instance = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//
//    assertThat(instance.vertical).isTrue
//  }

//  @Test
//  fun `tracks vertical status when vertical raceway is parent`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//    val verticalRaceway = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//
//    val instance = Raceway.factory(minimalXmlWithVerticalRacewayParent(), verticalRaceway)
//
//    assertThat(instance.vertical).isFalse
//  }

//  @Test
//  fun `vertical raceway registers self with raceway base parent`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//
//    val instance = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//
//    assertThat(racewayBase.upright).isSameAs(instance)
//  }

//  @Test
//  fun `vertical raceway keeps references to dependent raceways`() {
//    val racewayBase = RacewayBase.factory(RacewayBaseTest().minimalXml(), null)
//    val verticalRaceway = Raceway.factory(minimalXmlWithRacewayBaseParent(), racewayBase)
//
//    val instance = Raceway.factory(minimalXmlWithVerticalRacewayParent(), verticalRaceway)
//
//    assertThat(verticalRaceway.dependents).contains(
//      Locator( 38f, instance)
//    )
//  }

  // TODO Does this test really belong with Raceway?
  @Test
  fun `reorient using proscenium coordinates`() {
    Proscenium.instances.clear()
    val prosceniumElement = IIOMetadataNode("raceway")
    prosceniumElement.setAttribute("x", "120")
    prosceniumElement.setAttribute("y", "230")
    prosceniumElement.setAttribute("z", "34")
    prosceniumElement.setAttribute("height", "4.5")
    prosceniumElement.setAttribute("width", "5.6")
    prosceniumElement.setAttribute("depth", "6.7")
    assertThat(Proscenium.inUse()).isFalse
    Proscenium.factory(prosceniumElement, null)
    assertThat(Proscenium.inUse()).isTrue
    val xmlElement = IIOMetadataNode("raceway")
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "10")
    xmlElement.setAttribute("y", "20")
    xmlElement.setAttribute("z", "300")
    xmlElement.setAttribute("length", "4.5")
    val instance = Raceway.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.origin).isNotEqualTo(StagePoint(130f, 210f, 334f))
      assertThat(instance.origin.venue).isEqualTo(VenuePoint(130f, 210f, 334f))
    }.assertAll()
  }

  @Test
  fun `find raceway by id`() {
    Raceway.instances.clear()
    val xmlElement = IIOMetadataNode("raceway")
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "10")
    xmlElement.setAttribute("y", "20")
    xmlElement.setAttribute("z", "300")
    xmlElement.setAttribute("length", "4.5")
    val instance = Raceway.factory(xmlElement, null)

    val foundRaceway = Raceway.queryById("name")

    assertThat(foundRaceway).isSameAs(instance)
  }

  @Test
  fun `do not find raceway by id`() {
    Raceway.instances.clear()

    val foundRaceway = Raceway.queryById("name")

    assertThat(foundRaceway).isNull()
  }

  @Test
  fun `find raceway by xml element`() {
    Raceway.instances.clear()
    Luminaire.instances.clear()
    val filename = "racewayAndLuminaire.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Startup.startup(pathName)
    val luminaireXml = Luminaire.instances[0].xmlElement
    val racewayXml = Raceway.instances[0].xmlElement

    val foundRaceway = Raceway.queryByXmlElement(luminaireXml.parentNode as Element)

    SoftAssertions().apply {
      assertThat(foundRaceway).isNotNull
      assertThat(foundRaceway?.xmlElement).isSameAs(racewayXml)
    }.assertAll()
  }

  @Test
  fun `do not find raceway by xml element`() {
    Raceway.instances.clear()
    Luminaire.instances.clear()
    val filename = "luminaire.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Startup.startup(pathName)
    val luminaireXml = Luminaire.instances[0].xmlElement

    val foundRaceway = Raceway.queryByXmlElement(luminaireXml)

    assertThat(foundRaceway).isNull()
  }

  @Test
  fun `keeps track of child entities according to location on raceway`() {
    Raceway.instances.clear()
    val racewayElement = IIOMetadataNode("raceway")
    racewayElement.setAttribute("id", "name")
    racewayElement.setAttribute("x", "10")
    racewayElement.setAttribute("y", "20")
    racewayElement.setAttribute("z", "300")
    racewayElement.setAttribute("length", "4.5")
    val raceway = Raceway.factory(racewayElement, null)
    val luminaireElement = IIOMetadataNode("raceway")
    luminaireElement.setAttribute("type", "Type value")
    luminaireElement.setAttribute("location", "17.6")
    luminaireElement.setAttribute("owner", "Owner name")
    luminaireElement.setAttribute("address", "124")
    val luminaire = Luminaire.factory(luminaireElement, null)
    val luminaireElement2 = IIOMetadataNode("raceway")
    luminaireElement2.setAttribute("type", "Type value")
    luminaireElement2.setAttribute("location", "-17.6")
    luminaireElement2.setAttribute("owner", "Owner name")
    luminaireElement2.setAttribute("address", "124")
    val luminaire2 = Luminaire.factory(luminaireElement2, null)

    raceway.hang(luminaire)
    raceway.hang(luminaire2)

    Raceway.sortDependents()

    assertThat(raceway.dependents).containsExactly(
      Locator(-17.6f, luminaire2),
      Locator(17.6f, luminaire)
    )

  }
}
