import com.mobiletheatertech.plot.Configuration
import com.mobiletheatertech.plot.exception.InvalidXMLException
import display.addAttribute
import display.drawRectangle
import display.drawSvg
import display.generateSvgSymbols
import entities.Drawing
import entities.Event
import entities.Locator
import entities.Pipe
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
import j2html.TagCreator.video
import j2html.tags.DomContent
import j2html.tags.specialized.DivTag
import j2html.tags.specialized.H1Tag
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
          drawing.writeSinglePipeDrawing()
        }
      }
    }

  }
}


fun Drawing.writeSinglePipeDrawing() {
  println("Writing drawing for $id to $filename, showing $pipe")
  val pipe = Pipe.queryById(pipe) ?: return

  val svgDocument = startSvg()
  generateSvgSymbols(svgDocument)
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
      rawHtml(generatedSvgText)
    ),
    table(
      tr(
        td("Luminaire Type"),
        td("Address"),
        td("Circuit"),
        td("Channel"),
        td("Target"),
        td("Owner"),
        td("Notes"),
      ),
      pipe.drawHtmlDecendents()
    )
  ).renderFormatted()

  File("${Configuration.plotDirectory}/out/$filename.html").writeText(text)
}

fun Pipe.svgHighlightBox(svgDocument: SvgDocument) {
  val place = origin.venue
  val drawingResults = drawRectangle(svgDocument, place.x - 50, place.y - 50, length + 100, 100f, "teal")
  drawingResults.element.addAttribute("opacity", "0.3")
}

private fun Pipe.drawPipeSvg(svgDocument: SvgDocument): String {
  val pipeDrawingBoundary = this.drawSvg(svgDocument)
  val viewBox = "${pipeDrawingBoundary.xMin + 300} ${pipeDrawingBoundary.yMin - 50} 100 100"
  val root = svgDocument.root
  root.setAttribute("width", "1200")
  root.setAttribute("height", "150")
  root.setAttribute("viewBox", viewBox)
  return finishSvgString(svgDocument)
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


fun Pipe.drawHtmlDecendents(): DomContent {
  return each(dependents) { hanger: Locator ->
    val thing = hanger.luminaire
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
