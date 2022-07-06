import com.mobiletheatertech.plot.Configuration
import com.mobiletheatertech.plot.exception.InvalidXMLException
import display.drawSvg
import display.generateSvgSymbols
import entities.Drawing
import entities.Event
import entities.Locator
import entities.Pipe
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
import j2html.TagCreator.head
import j2html.TagCreator.html
import j2html.TagCreator.rawHtml
import j2html.TagCreator.table
import j2html.TagCreator.td
import j2html.TagCreator.tr
import j2html.tags.DomContent
import j2html.tags.specialized.DivTag
import j2html.tags.specialized.H1Tag
import java.io.File

//import javax.management.Query.div

class Html {
  companion object {
    fun writeAll() {
      writePipeDrawings()
    }

    fun writePipeDrawings() {
      for (drawing in Drawing.instances) {
        val pipe = Pipe.queryById(drawing.pipe)
          ?: throw InvalidXMLException("Pipe ${drawing.pipe} mentioned in drawing element ${drawing.id} not found")
        writeSinglePipeDrawing(drawing, pipe)
      }
    }

    fun writeSinglePipeDrawing(drawing: Drawing, pipe: Pipe) {
      println("Writing drawing for ${drawing.id} to ${drawing.filename}, showing ${drawing.pipe}")
      startHtmlFile(drawing)
//      drawSvgPipeDrawing(document, namespace, root, pipe)
    }

    fun writeHeading() {

    }
  }
}


fun startHtmlFile(drawing: Drawing) {
  val pipe = Pipe.queryById(drawing.pipe)

  val svgDocument = startSvg()
  generateSvgSymbols(svgDocument)
  val pipeDrawingBoundary = pipe?.drawSvg(svgDocument)
  println("For ${drawing.id}, pipe boundary is $pipeDrawingBoundary.")
  val root = svgDocument.root
  root.setAttribute("width", "600")
  root.setAttribute("height", "100")
  root.setAttribute("viewBox", "320 1000 100 100")
  val generatedSvgText = finishSvgString(svgDocument)

  val text = html(
    head(
    ),
    body(
      drawingHeading(),
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
      pipe?.drawHtmlDecendents()
    )
  ).renderFormatted()

  File("${Configuration.plotDirectory}/out/${drawing.filename}.html").writeText(text)
}

fun drawingHeading(): DivTag {
  return div(
    // TODO: Event should manage this...
    if (Event.instances.size > 0) {
      Event.instances[0]?.htmlDrawingHeading()
    } else {
      h1("No event specified")
    }
  )
}


fun Event.htmlDrawingHeading(): H1Tag = h1(id)


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
