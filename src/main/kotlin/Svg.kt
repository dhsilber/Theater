package com.mobiletheatertech.plot

import display.drawSvgContent
import entities.Wall
import org.apache.batik.dom.GenericDOMImplementation
import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.DOMImplementation
import java.io.FileWriter
import java.io.Writer

class Svg {
  companion object {
    fun write() {

      // Get a DOMImplementation.
      val domImpl: DOMImplementation = GenericDOMImplementation.getDOMImplementation()

      // create an instance of org.w3c.dom.Document.
      val namespace = "http://www.w3.org/2000/svg"
      val document = domImpl.createDocument(namespace, "svg", null)

      // create an instance of the SVG Generator.
      val svgGenerator = SVGGraphics2D(document)

      /**
       * Getting the root element from the SVGGraphics2D object resets SVGGraphics2D, so all of the
       * drawing must be done before this method is invoked and all of the non-drawing modifications
       * to the DOM should be done after this.
       */
      val root = svgGenerator.getRoot()

      root.setAttribute("xmlns:plot", "http://www.davidsilber.name/namespaces/plot")

      drawSvgContent(document, namespace, root)


//        System.out.println( "In create. Pathname: " + pathname );
      // Finally, stream out SVG to the specified pathname
//        boolean useCSS = true; // we want to use CSS style attributes
      try {
        val out: Writer = FileWriter("${Configuration.plotDirectory}/wall.svg")

        /*
             * Per http://osdir.com/ml/text.xml.batik.user/2003-01/msg00121.html
             * getting the root element from the SVGGraphics2D object resets
             * SVGGraphics2D, so we have to make sure we generate the stream
             * from the root that we grabbed.
             */svgGenerator.stream(root, out) //, useCSS );
      } catch (e: Exception) {
//            System.err.println( e.toString() );
      }
    }
  }
}
