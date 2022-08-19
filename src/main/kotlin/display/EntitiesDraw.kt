package display

import androidx.compose.ui.graphics.Color
import entities.Proscenium
import entities.Stair
import entities.Venue
import java.lang.Float.min

fun Proscenium.drawPlan(): List<DrawingOrder> {

  val startX = origin.x - width / 2
  val startY = origin.y
  val endX = origin.x + width / 2
  val endY = origin.y + depth
  val originSize = 7f

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  val cyan = IndependentColor(Color.Cyan, "cyan")
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(origin.x - originSize, endY - originSize, origin.x + originSize, endY + originSize),
    cyan
  ))
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(origin.x + originSize, endY - originSize, origin.x - originSize, endY + originSize),
    cyan
  ))
//  drawingOrders.add(DrawingOrder(
//    DrawingOrderOperation.CIRCLE,
//    listOf(origin.x, endY, originSize),
//    cyan
//  ))
  // SR end of proscenium arch
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(startX, startY, startX, endY),
  ))
  // SL end of proscenium arch
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(endX, startY, endX, endY),
  ))
  // US side of proscenium arch
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(startX, startY, endX, startY),
    IndependentColor(Color.Gray, "grey")
  ))
  // DS side of proscenium arch
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(startX, endY, endX, endY),
    IndependentColor(Color.LightGray, "lightgrey")
  ))

  return drawingOrders.toList()
}

fun Proscenium.drawSection(): List<DrawingOrder> {
  val venue = Venue.instances.first()
  val floorHeight = venue.height - origin.z
  val originY = venue.depth - origin.y
  val originSize = 7f

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  val cyan = IndependentColor(Color.Cyan, "cyan")
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(originY - originSize, floorHeight - originSize, originY + originSize, floorHeight + originSize),
    cyan
  ))
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(originY + originSize, floorHeight - originSize, originY - originSize, floorHeight + originSize),
    cyan
  ))
//  drawingOrders.add(DrawingOrder(
//    DrawingOrderOperation.CIRCLE,
//    listOf(originY, floorHeight, originSize),
//    cyan
//  ))
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(originY, floorHeight, originY, floorHeight - height),
    IndependentColor(Color.Green, "green")
  ))
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(originY + depth, floorHeight, originY + depth, floorHeight - height),
  ))
  drawingOrders.add(DrawingOrder(
    DrawingOrderOperation.LINE,
    listOf(originY, floorHeight - height, originY + depth, floorHeight - height),
    IndependentColor(Color.Gray, "grey")
  ))

  return drawingOrders.toList()
}

fun Stair.drawPlan(): List<DrawingOrder> {

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  repeat(steps) { index ->
    drawingOrders.add(DrawingOrder(
      DrawingOrderOperation.RECTANGLE,
      listOf(origin.x, origin.y + index * run, width, run),
    ))
  }

  return drawingOrders.toList()
}

fun Stair.drawSection(): List<DrawingOrder> {
  val venue = Venue.instances.first()

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  repeat(steps) { index ->
    drawingOrders.add(DrawingOrder(
      DrawingOrderOperation.LINE,
      listOf(
        venue.depth - origin.y - run * (index + 1),
        venue.height - origin.z + rise * index,
        venue.depth - origin.y - run * index,
        venue.height - origin.z + rise * index
      )
    ))
    drawingOrders.add(DrawingOrder(
      DrawingOrderOperation.LINE,
      listOf(
        venue.depth - origin.y - run * (index + 1),
        venue.height - origin.z + rise * index,
        venue.depth - origin.y - run * (index + 1),
        min( venue.height.toFloat(), venue.height - origin.z + rise *  (index + 1))
      )
    ))
  }

  return drawingOrders.toList()
}
