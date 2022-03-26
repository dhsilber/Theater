package display

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import entities.Pipe
import entities.Wall
import entities.Proscenium

fun drawContent(drawScope: DrawScope) {
  println("Prosceniums: ${Proscenium.Instances.size}")
  for (instance in Proscenium.Instances) {
    println(instance.toString())
//          val (x1, y1, x2, y2) =
    instance.draw(drawScope)
//          drawLine(Color.Black, Offset(x1, y1), Offset(x2, y2))
  }
  for (instance in Wall.Instances) {
//          Text(instance.toString())
//          val (x1, y1, x2, y2) =
    instance.draw(drawScope)
//          with(density) {

//          drawLine(Color.Black, Offset(x1, y1), Offset(x2, y2))
//          }
//          drawLine(Color.Magenta, Offset(x10.toFloat(), y10.toFloat()), Offset(x20.toFloat(), y20.toFloat()))

  }
  for (instance in Pipe.Instances) {
    instance.draw(drawScope)
  }
}

fun Proscenium.draw(drawScope: DrawScope) {
//  println(toString())
  drawScope.drawLine(Color.Cyan, Offset(x - 17, y - 17), Offset(x + 17, y + 17))
  drawScope.drawLine(Color.Cyan, Offset(x + 17, y - 17), Offset(x - 17, y + 17))
  drawScope.drawCircle(Color.Cyan, 17f, Offset(x.toFloat(), y.toFloat()), style = Stroke(width = 2f))
  val startX = x - width / 2
  val startY = y
  val endX = x + width / 2
  val endY = y + depth
  // SR end of proscenium arch
  drawScope.drawLine(Color.Black, Offset(startX, startY), Offset(startX, endY))
  // SL end of proscenium arch
  drawScope.drawLine(Color.Black, Offset(endX, startY), Offset(endX, endY))

  // US side of proscenium arch
  drawScope.drawLine(Color.Gray, Offset(startX, startY), Offset(endX, startY))
  // DS side of proscenium arch
  drawScope.drawLine(Color.LightGray, Offset(startX, endY), Offset(endX, endY))
}

fun Wall.draw(drawScope: DrawScope) {
//  println(toString())
  drawScope.drawLine(Color.Black, Offset(x1, y1), Offset(x2, y2))
}

fun Pipe.draw(drawScope: DrawScope) {
  drawScope.drawRect(Color.Black, Offset(x,y), Size(length, Pipe.Diameter))
}
