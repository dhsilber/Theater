import com.mobiletheatertech.plot.Configuration
import display.drawSvgPlanContent
import display.drawSvgSectionContent
import org.apache.batik.dom.GenericDOMImplementation
import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.DOMImplementation
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.FileWriter
import java.io.StringWriter
import java.io.Writer

class Svg {
  companion object {
    fun writeAll() {
      writePlanView()
//      writeSectionView()
//      writePipeDrawings()
    }

    fun writePlanView() {
      val svgDetails = startSvg()
//      val (document, namespace, generator, root) = svgDetails
      drawSvgPlanContent(svgDetails)
      finishSvgFile(svgDetails, "plan")
    }

//    Somewhere in Svg.writeSectionView(), the venuePoint width is being set to something relating to the y value of this floor element:
//
//    I imagine that it has something to do with recasting the points to rotate the point of view.

    fun writeSectionView() {
      val svgDetails = startSvg()
//      val (document, namespace, generator, root) = svgDetails
      drawSvgSectionContent(svgDetails)
      finishSvgFile(svgDetails, "section")
    }

//    fun writePipeDrawings() {
//      for (drawing in Drawing.instances) {
//        val pipe = Pipe.queryById(drawing.pipe)
//          ?: throw InvalidXMLException("Pipe ${drawing.pipe} mentioned in drawing element ${drawing.id} not found")
//        writeSinglePipeDrawing(drawing, pipe)
//      }
//    }

//    fun writeSinglePipeDrawing(drawing: Drawing, pipe: Pipe) {
//      println("Writing drawing for ${drawing.id} to ${drawing.filename}, showing ${drawing.pipe}")
//      val svgDetails = startSvg()
//      drawSvgPipeDrawing(svgDetails, pipe)
//      finishSvgFile(svgDetails, drawing.filename)
//    }

  }
}

fun startSvg(): SvgDocument {

  val domImpl: DOMImplementation = GenericDOMImplementation.getDOMImplementation()
  val namespace = "http://www.w3.org/2000/svg"
  val document = domImpl.createDocument(namespace, "svg", null)
  val svgGenerator = SVGGraphics2D(document)

  /**
   * Getting the root element from the SVGGraphics2D object resets SVGGraphics2D, so all the
   * drawing must be done before this method is invoked and all the non-drawing modifications
   * to the DOM should be done after this.
   */
  val root = svgGenerator.root

//      root.setAttribute("xmlns:plot", "http://www.davidsilber.name/namespaces/plot")

  return SvgDocument(document, namespace, svgGenerator, root)
}

fun finishSvgFile(svgDetails: SvgDocument, filename: String) {
  //        System.out.println( "In create. Pathname: " + pathname );
  // Finally, stream out SVG to the specified pathname
//        boolean useCSS = true; // we want to use CSS style attributes
  try {
    println("Writing to: ${Configuration.plotDirectory}/out/$filename.svg")
    // (See also StringWriter, for writing svg snippets to strings that I can include in generated HTML files.)
    val out: Writer = FileWriter("${Configuration.plotDirectory}/out/$filename.svg")

    /**
     * Per http://osdir.com/ml/text.xml.batik.user/2003-01/msg00121.html
     * getting the root element from the SVGGraphics2D object resets
     * SVGGraphics2D, so we have to make sure we generate the stream
     * from the root that we grabbed.
     */
    svgDetails.generator.stream(svgDetails.root, out) //, useCSS );
  } catch (e: Exception) {
    System.err.println(e.toString());
  }
}

fun finishSvgString(svgDetails: SvgDocument): String {
  //        System.out.println( "In create. Pathname: " + pathname );
  // Finally, stream out SVG to the specified pathname
//        boolean useCSS = true; // we want to use CSS style attributes
  println("Writing SVG to String")
  // (See also StringWriter, for writing svg snippets to strings that I can include in generated HTML files.)
  val out: Writer = StringWriter()

  /**
   * Per http://osdir.com/ml/text.xml.batik.user/2003-01/msg00121.html
   * getting the root element from the SVGGraphics2D object resets
   * SVGGraphics2D, so we have to make sure we generate the stream
   * from the root that we grabbed.
   */
  try {
    svgDetails.generator.stream(svgDetails.root, out) //, useCSS );
  } catch (e: Exception) {
    System.err.println(e.toString());
  }
  return out.toString()
}

data class SvgDocument(
  val document: Document,
  val namespace: String,
  val generator: SVGGraphics2D,
  val root: Element,
)