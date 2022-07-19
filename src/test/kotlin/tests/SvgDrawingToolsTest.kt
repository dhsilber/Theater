package tests

import display.drawCircle
import display.drawRectangle
import display.drawSymbol
import display.drawUse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import startSvg
import javax.imageio.metadata.IIOMetadataNode

class SvgDrawingToolsTest {

  @Test
  fun `drawCircle draws svg circle`() {
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    drawCircle(svgDocument, 1f, 2f, 3f)

    val circles = svgDocument.root.getElementsByTagName("circle")
    assertThat(circles.length).isEqualTo(1)
    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val attributes = circles.item(0).attributes
    SoftAssertions().apply {
      assertThat(attributes.getNamedItem("cx").textContent.toFloat()).isEqualTo(1f)
      assertThat(attributes.getNamedItem("cy").textContent.toFloat()).isEqualTo(2f)
      assertThat(attributes.getNamedItem("r").textContent.toFloat()).isEqualTo(3f)
      assertThat(attributes.getNamedItem("stroke").textContent).isEqualTo("black")
      assertThat(attributes.getNamedItem("stroke-width").textContent).isEqualTo("2")
      assertThat(attributes.getNamedItem("fill").textContent).isEqualTo("none")
    }.assertAll()
  }

  @Test
  fun `drawCircle provides created element and space occupied`() {
    val svgDocument = startSvg()

    val result = drawCircle(svgDocument, 1f, 2f, 3f)

    assertThat(result.element.tagName).isEqualTo("circle")
    val boundary = result.boundary
    SoftAssertions().apply {
      assertThat(boundary.xMin).isEqualTo(-2f)
      assertThat(boundary.yMin).isEqualTo(-1f)
      assertThat(boundary.xMax).isEqualTo(4f)
      assertThat(boundary.yMax).isEqualTo(5f)
    }.assertAll()
  }

  @Test
  fun `drawRectangle draws svg rect`() {
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    drawRectangle(svgDocument, 1f, 2f, 3f, 4f)

    val rectangles = svgDocument.root.getElementsByTagName("rect")
    assertThat(rectangles.length).isEqualTo(1)
    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val attributes = rectangles.item(0).attributes
    SoftAssertions().apply {
      assertThat(attributes.getNamedItem("x").textContent.toFloat()).isEqualTo(1f)
      assertThat(attributes.getNamedItem("y").textContent.toFloat()).isEqualTo(2f)
      assertThat(attributes.getNamedItem("width").textContent.toFloat()).isEqualTo(3f)
      assertThat(attributes.getNamedItem("height").textContent.toFloat()).isEqualTo(4f)
      assertThat(attributes.getNamedItem("fill").textContent).isEqualTo("white")
    }.assertAll()
  }

  @Test
  fun `drawRectangle draws svg rect with specified fill color`() {
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    drawRectangle(svgDocument, 1f, 2f, 3f, 4f, "cyan")

    val rectangles = svgDocument.root.getElementsByTagName("rect")
    assertThat(rectangles.length).isEqualTo(1)
    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val attributes = rectangles.item(0).attributes
    assertThat(attributes.getNamedItem("fill").textContent).isEqualTo("cyan")
  }

  @Test
  fun `drawRectangle provides created element and space occupied`() {
    val svgDocument = startSvg()

    val result = drawRectangle(svgDocument, 1f, 2f, 3f, 5f)

    assertThat(result.element.tagName).isEqualTo("rect")
    val boundary = result.boundary
    SoftAssertions().apply {
      assertThat(boundary.xMin).isEqualTo(1f)
      assertThat(boundary.yMin).isEqualTo(2f)
      assertThat(boundary.xMax).isEqualTo(4f)
      assertThat(boundary.yMax).isEqualTo(7f)
    }.assertAll()
  }

  @Test
  fun `drawSymbol adds symbol element to document`() {
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    drawSymbol(svgDocument, "name", IIOMetadataNode("svg"))

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val symbolElements = svgDocument.root.getElementsByTagName("symbol")
    assertThat(symbolElements.length).isEqualTo(1)
    val element = symbolElements.item(0)
    val attributes = element.attributes
    assertThat(element.childNodes.length).isEqualTo(1)
    val childElement = element.childNodes.item(0)
    SoftAssertions().apply {
      assertThat(attributes.getNamedItem("id").textContent).isEqualTo("name")
      assertThat(attributes.getNamedItem("overflow").textContent).isEqualTo("visible")
      assertThat(childElement.nodeName).isEqualTo("svg")
    }.assertAll()
  }

  @Test
  fun `drawUse adds use element to document`() {
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    drawUse(svgDocument, "type name", 1f, 2f)

    val useElements = svgDocument.root.getElementsByTagName("use")
    assertThat(useElements.length).isEqualTo(1)
    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val attributes = useElements.item(0).attributes
    SoftAssertions().apply {
      assertThat(attributes.getNamedItem("xlink:href").textContent).isEqualTo("#type name")
      assertThat(attributes.getNamedItem("x").textContent.toFloat()).isEqualTo(1f)
      assertThat(attributes.getNamedItem("y").textContent.toFloat()).isEqualTo(2f)
    }.assertAll()
  }
}
