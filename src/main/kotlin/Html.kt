import com.mobiletheatertech.plot.Configuration
import display.DrawingResults
import display.SvgBoundary
import display.addAttribute
import display.drawRectangle
import display.drawSvgPlan
import display.generateSvgSymbols
import entities.Drawing
import entities.Event
import entities.Locator
import entities.Luminaire
import entities.Pipe
import entities.PipeBase
import entities.Venue
//import kotlinx.html.body
//import kotlinx.html.div
//import kotlinx.html.dom.createHTMLTree
//import kotlinx.html.dom.document
//import kotlinx.html.h1
//import kotlinx.html.head
//import kotlinx.html.html
//import kotlinx.html.stream.appendHTML
//import kotlinx.html.svg
import j2html.TagCreator.body
import j2html.TagCreator.div
import j2html.TagCreator.each
import j2html.TagCreator.h1
import j2html.TagCreator.h2
import j2html.TagCreator.head
import j2html.TagCreator.html
import j2html.TagCreator.rawHtml
import j2html.TagCreator.span
import j2html.TagCreator.style
import j2html.TagCreator.table
import j2html.TagCreator.td
import j2html.TagCreator.tr
import j2html.tags.DomContent
import j2html.tags.specialized.H2Tag
import j2html.tags.specialized.SpanTag
import java.io.File

class Html {
  companion object {
    fun writeAll() {
      writePipeDrawings()
    }

    fun writePipeDrawings() {
      for (drawing in Drawing.instances) {
        if (drawing.hasPipe) {
          drawing.writePipeDetailDrawing()
        }
      }
    }

  }
}

fun Drawing.writePipeDetailDrawing() {
  val svgDocument = startSvg()
  generateSvgSymbols(svgDocument)
  val base = PipeBase.queryById(pipe)
  if (null !== base) {
    writeSingleBoomDrawing(base, svgDocument)
    return
  }
  val pipe = Pipe.queryById(pipe)
  if (null !== pipe) writeSinglePipeDrawing(pipe, svgDocument)
}

fun Drawing.writeSinglePipeDrawing(pipe: Pipe, svgDocument: SvgDocument) {
  println("Writing drawing for $id to $filename, showing $pipe")

  pipe.svgHighlightBox(svgDocument)
  val generatedSvgText = pipe.drawPipeSvg(svgDocument)

  val text = html(
    head(
      style(
        rawHtml(".information { border: 0.25rem double  black; text-align: center;}")
      )
    ),
    body(
      informationHeaderBlock(),
      rawHtml(generatedSvgText),
      table(
        drawPipeDescendentsHtmlTableHeadings(),
        pipe.drawHtmlDecendents()
      )
    ),
  ).renderFormatted()

  File("${Configuration.plotDirectory}/out/$filename.html").writeText(text)
}

fun Drawing.writeSingleBoomDrawing(base: PipeBase, svgDocument: SvgDocument) {
  println("Writing drawing for $id to $filename, showing $base")

  val generatedSvgText = base.drawBoomDetailSvg(svgDocument)

  val text = html(
    head(
      style(
        rawHtml(".information { border: 0.25rem double  black; text-align: center;}")
      )
    ),
    body(
      informationHeaderBlock(),
      rawHtml(generatedSvgText),
//      table(
//        drawPipeDescendentsHtmlTableHeadings(),
//        pipe.drawHtmlDecendents()
//      )
    ),
  ).renderFormatted()

  File("${Configuration.plotDirectory}/out/$filename.html").writeText(text)
}

fun Pipe.svgHighlightBox(svgDocument: SvgDocument) {
  val place = origin.venue
  val drawingResults = drawRectangle(svgDocument, place.x - 50, place.y - 50, length + 100, 100f, "teal")
  drawingResults.element.addAttribute("opacity", "0.3")
}

fun unfinishedSvgHighlightBox(svgDocument: SvgDocument): DrawingResults {
  val drawingResults = drawRectangle(svgDocument, 0f, 0f, 0f, 0f, "teal")
  drawingResults.element.addAttribute("opacity", "0.3")
  return drawingResults
}

private fun PipeBase.drawBoomDetailSvg(svgDocument: SvgDocument): String {
  val highlight = unfinishedSvgHighlightBox(svgDocument).element
  val baseDrawingBoundary = this.drawFrontViewSvg(svgDocument)
  val viewBox = "${baseDrawingBoundary.xMin * 2f} ${baseDrawingBoundary.yMin - 15} 100 100"
  val root = svgDocument.root
  root.setAttribute("width", "1200")
  root.setAttribute("height", (170).toString())
  root.setAttribute("viewBox", viewBox)
  highlight.setAttribute("x", (baseDrawingBoundary.xMin - 50).toString())
  highlight.setAttribute("y", (baseDrawingBoundary.yMin - 50).toString())
  highlight.setAttribute("width", (baseDrawingBoundary.width + 100f).toString())
  highlight.setAttribute("height", (baseDrawingBoundary.height + 100).toString())
  return finishSvgString(svgDocument)
}

private fun Pipe.drawPipeSvg(svgDocument: SvgDocument): String {
  val pipeDrawingBoundary = drawSvgPlan(svgDocument)
  val viewBox = "${pipeDrawingBoundary.xMin + 300} ${pipeDrawingBoundary.yMin - 50} 100 100"
  val root = svgDocument.root
  root.setAttribute("width", "1200")
  root.setAttribute("height", "150")
  root.setAttribute("viewBox", viewBox)
  return finishSvgString(svgDocument)
}

fun PipeBase.drawFrontViewSvg(svgDocument: SvgDocument): SvgBoundary {
  val place = origin.venue
  val drawingResults = drawRectangle(svgDocument, place.x - PipeBase.Radius, 0f, PipeBase.Radius * 2, 3f)
  var boundary = drawingResults.boundary
  println("Boundary: $boundary")
  if (null !== upright) {
    val pipeDrawingResults = (upright as Pipe).drawFrontViewSvg(svgDocument)
    boundary += pipeDrawingResults.boundary
  }
  println("Boundary: $boundary")
  return boundary
}

fun Pipe.drawFrontViewSvg(svgDocument: SvgDocument): DrawingResults {
  val place = origin.venue
  val parentPlace = (parentEntity as PipeBase).origin.venue
  return drawRectangle(
    svgDocument,
    place.x - Pipe.Diameter / 2,
    place.z - parentPlace.z,
    Pipe.Diameter,
    length,
    fillColor = "black"
  )
}

fun Drawing.informationHeaderBlock(): SpanTag {
  return span(
    div(
      h1("$id"),
      // TODO: Event should manage this...
      if (Event.instances.size > 0) {
        Event.instances[0].htmlDrawingHeading()
      } else {
        h1("No event specified")
      },
      if (Venue.instances.size > 0) {
        Venue.instances[0].htmlDrawingHeading()
      } else {
        h1("No venue specified")
      }
    ).attr("class", "information")
  )
}


fun Event.htmlDrawingHeading(): H2Tag = h2(id)

fun Venue.htmlDrawingHeading(): H2Tag = h2("$building - $room")

fun drawPipeDescendentsHtmlTableHeadings(): DomContent {
  return tr(
    td("Luminaire Type"),
    td("Address"),
    td("Circuit"),
    td("Channel"),
    td("Target"),
    td("Owner"),
    td("Notes"),
  )
}

fun Pipe.drawHtmlDecendents(): DomContent {
  return each(dependents) { hanger: Locator ->
    val thing = hanger.hangable as Luminaire
    tr(
      td(thing.type),
      td(thing.address.toString()),
      td(thing.circuit),
      td(thing.channel.toString()),
      td(thing.target),
      td(thing.owner),
      td(thing.info),
    )
  }
}

/*
Information Box:

Pirates of Penzance - November 2022
Carlisle School - Corey Auditorium
               ---
House Pipe Build Details


Footnote:
Generated 2022-06-30 17:45

 */
