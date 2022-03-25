package display

import entities.Proscenium
import entities.Wall
import org.w3c.dom.Document
import org.w3c.dom.Element

fun drawSvgContent(svgDocuemnt: Document, svgNamespace: String, parentElement: Element) {
  for (instance in Proscenium.Instances) {
    instance.drawSvg(svgDocuemnt, svgNamespace, parentElement)
  }
  for (instance in Wall.Instances) {
    instance.drawSvg(svgDocuemnt, svgNamespace, parentElement)
  }
}

fun Proscenium.drawSvg(svgDocuemnt: Document, svgNamespace: String, parentElement: Element) {
//  println("Drawing the proscenium.")
  drawLine(svgDocuemnt, svgNamespace, parentElement, x - 17f, y - 17f, x + 17f, y + 17f)
    .addAttribute("stroke", "cyan")
  drawLine(svgDocuemnt, svgNamespace, parentElement, x + 17f, y - 17f, x - 17f, y + 17f)
    .addAttribute("stroke", "cyan")
  drawCircle(svgDocuemnt, svgNamespace, parentElement, x, y, 17f)
    .addAttribute("stroke", "cyan")

  val startX = x - width / 2
  val startY = y
  val endX = x + width / 2
  val endY = y + depth
  // SR end of proscenium arch
  drawLine(svgDocuemnt, svgNamespace, parentElement, startX, startY, startX, endY)
  // SL end of proscenium arch
  drawLine(svgDocuemnt, svgNamespace, parentElement, endX, startY, endX, endY)
  // US side of proscenium arch
  drawLine(svgDocuemnt, svgNamespace, parentElement, startX, startY, endX, startY)
    .addAttribute("stroke-opacity", "0.3")
  // DS side of proscenium arch
  drawLine(svgDocuemnt, svgNamespace, parentElement, startX, endY, endX, endY)
    .addAttribute("stroke-opacity", "0.1")
}

fun Wall.drawSvg(svgDocuemnt: Document, svgNamespace: String, parentElement: Element) {
//  println("Drawing the wall from $x1,$y1 to $x2,$y2.")

  drawLine(svgDocuemnt, svgNamespace, parentElement, x1, y1, x2, y2)
}