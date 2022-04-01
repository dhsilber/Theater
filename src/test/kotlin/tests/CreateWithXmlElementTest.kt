package tests

import CreateWithXmlElement
import XmlElemental
import org.junit.Test
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertSame

class CreateWithXmlElementTest {

  class FakeElemental(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
    companion object : CreateWithXmlElement<FakeElemental>() {
      fun factory(xmlElement: Element, parentEntity: XmlElemental?): FakeElemental =
        create(xmlElement, parentEntity, CreateWithXmlElementTest::FakeElemental)
    }
  }

  @Test
  fun `factory instantiates instance with element`() {
    val xmlElement = IIOMetadataNode()
    val existingCount = FakeElemental.Instances.size
    val instance = FakeElemental.factory(xmlElement, null)
    assertContains(FakeElemental.Instances, instance)
    assertEquals(1 + existingCount, FakeElemental.Instances.size)
    assertSame(xmlElement, instance.xmlElement)
  }

}