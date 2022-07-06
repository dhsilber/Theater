package tests

import display.drawRectangle
import display.drawSymbol
import display.drawUse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test
import org.w3c.dom.Element
import org.w3c.dom.Node
import startSvg
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.fail

class SvgDrawingToolsTest {

  @Test
  fun `drawRectangle draws svg rect`() {
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    drawRectangle(svgDocument, 1f, 2f, 3f, 4f)

    val rectangles = svgDocument.root.getElementsByTagName("rect")
    assertThat(rectangles.length).isEqualTo(1)
    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
  }

  @Test
  fun `drawRectangle provides created element and space occupied`() {
    val svgDocument = startSvg()

    val result = drawRectangle(svgDocument, 1f, 2f, 3f, 5f)

    assertThat(result.element.tagName).isEqualTo("rect")
    val boundary = result.boundary
    assertThat(boundary.xMin).isEqualTo(1f)
    assertThat(boundary.yMin).isEqualTo(2f)
    assertThat(boundary.xMax).isEqualTo(4f)
    assertThat(boundary.yMax).isEqualTo(7f)
  }

  @Test
  fun `drawSymbol adds symbol element to document`() {
    val svgDocument = startSvg()
    val initialSymbolElementCount = svgDocument.root.getElementsByTagName("symbol").length

    drawSymbol(svgDocument, "name", IIOMetadataNode("svg"))

    assertThat(svgDocument.root.getElementsByTagName("symbol").length).isEqualTo(initialSymbolElementCount + 1)
  }

  @Test
  fun `drawUse adds use element to document`(){
    val svgDocument = startSvg()
    val initialUseElementCount = svgDocument.root.getElementsByTagName("use").length

    drawUse(svgDocument, "type name", 1f, 2f)

    assertThat(svgDocument.root.getElementsByTagName("symbol").length).isEqualTo(initialUseElementCount + 1)
    fail()
  }
}
