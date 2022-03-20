package tests

import CreateWithXmlElement
import XmlElemental
import org.junit.Test
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertSame

class CreateWithElementTest {

  class FakeElemental(elementPassthrough: Element) : XmlElemental(elementPassthrough) {
    companion object : CreateWithXmlElement<FakeElemental>() {
      fun factory(xmlElement: Element): FakeElemental = create(xmlElement, CreateWithElementTest::FakeElemental)
    }
  }

  @Test
  fun `factory instantiates instance with element`() {
    val xmlElement = IIOMetadataNode()
    val existingcount = FakeElemental.Instances.size
    val instance = FakeElemental.factory(xmlElement)
    assertContains(FakeElemental.Instances, instance)
    assertEquals(1 + existingcount, FakeElemental.Instances.size)
    assertSame(xmlElement, instance.xmlElement)
  }

}