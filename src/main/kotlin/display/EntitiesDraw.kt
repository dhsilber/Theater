package display

import display.DrawingOrderOperation.CIRCLE
import display.DrawingOrderOperation.LINE
import display.DrawingOrderOperation.RECTANGLE
import display.DrawingOrderOperation.FILLED_RECTANGLE
import display.DrawingOrderOperation.USE
import androidx.compose.ui.graphics.Color
import coordinates.Point
import coordinates.StagePoint
import coordinates.VenuePoint
import entities.Floor
import entities.Luminaire
import entities.Pipe
import entities.PipeBase
import entities.Proscenium
import entities.SetPlatform
import entities.Setpiece
import entities.Shape
import entities.Stair
import entities.Venue
import java.lang.Float.min

fun Proscenium.drawPlan(): List<DrawingOrder> {

  val startX = origin.x - width / 2
  val startY = origin.y
  val endX = origin.x + width / 2
  val endY = origin.y - depth
  val originSize = 7f

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  val cyan = IndependentColor(Color.Cyan, "cyan")
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(origin.x - originSize, origin.y - originSize, origin.x + originSize, origin.y + originSize),
    color = cyan
  ))
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(origin.x + originSize, origin.y - originSize, origin.x - originSize, origin.y + originSize),
    color = cyan
  ))
//  drawingOrders.add(DrawingOrder(
//    CIRCLE,
//    data = listOf(origin.x, endY, originSize),
//    cyan
//  ))
  // SR end of proscenium arch
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(startX, startY, startX, endY),
  ))
  // SL end of proscenium arch
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(endX, startY, endX, endY),
  ))
  // US side of proscenium arch
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(startX, endY, endX, endY),
    color = IndependentColor(Color.Gray, "grey")
  ))
  // DS side of proscenium arch
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(startX, startY, endX, startY),
    color = IndependentColor(Color.LightGray, "lightgrey")
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
    operation = LINE,
    entity = this,
    data = listOf(originY - originSize, floorHeight - originSize, originY + originSize, floorHeight + originSize),
    color = cyan
  ))
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(originY + originSize, floorHeight - originSize, originY - originSize, floorHeight + originSize),
    color = cyan
  ))
//  drawingOrders.add(DrawingOrder(
//    CIRCLE,
//    data = listOf(originY, floorHeight, originSize),
//    cyan
//  ))
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(originY, floorHeight, originY, floorHeight - height),
    color = IndependentColor(Color.Green, "green")
  ))
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(originY + depth, floorHeight, originY + depth, floorHeight - height),
  ))
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(originY, floorHeight - height, originY + depth, floorHeight - height),
    color = IndependentColor(Color.Gray, "grey")
  ))

  return drawingOrders.toList()
}

fun Floor.drawPlan(): List<DrawingOrder> {

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  drawingOrders.add(DrawingOrder(
    operation = FILLED_RECTANGLE,
    entity = this,
    data = listOf(surface.x, surface.y, surface.width, surface.depth),
    color = IndependentColor(Color.Gray, "grey"),
    opacity = 0.1F,
  ))

  return drawingOrders.toList()
}

fun Floor.drawSection(): List<DrawingOrder> {
  val venue = Venue.instances.first()
  val height = venue.height - surface.z
  val originY = venue.depth - surface.y - surface.depth

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(originY, height, originY + surface.depth, height)
  ))
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(originY, height, originY, venue.height.toFloat())
  ))
  drawingOrders.add(DrawingOrder(
    FILLED_RECTANGLE,
    entity = this,
    data = listOf(originY, height, surface.depth, surface.z),
    color = IndependentColor(Color.Gray, "grey"),
    opacity = 0.1F,
  ))

  return drawingOrders.toList()
}

fun Stair.drawPlan(): List<DrawingOrder> {

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  repeat(steps) { index ->
    drawingOrders.add(DrawingOrder(
      RECTANGLE,
      entity = this,
      data = listOf(origin.x, origin.y + index * run, width, run),
    ))
  }

  return drawingOrders.toList()
}

fun Stair.drawSection(): List<DrawingOrder> {
  val venue = Venue.instances.first()

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  repeat(steps) { index ->
    drawingOrders.add(DrawingOrder(
      operation = LINE,
      entity = this,
      data = listOf(
        venue.depth - origin.y - run * (index + 1),
        venue.height - origin.z + rise * index,
        venue.depth - origin.y - run * index,
        venue.height - origin.z + rise * index
      )
    ))
    drawingOrders.add(DrawingOrder(
      operation = LINE,
      entity = this,
      data = listOf(
        venue.depth - origin.y - run * (index + 1),
        venue.height - origin.z + rise * index,
        venue.depth - origin.y - run * (index + 1),
        min(venue.height.toFloat(), venue.height - origin.z + rise * (index + 1))
      )
    ))
  }

  return drawingOrders.toList()
}

fun PipeBase.drawPlan(): List<DrawingOrder> {
  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()
  val place = origin.venue

  drawingOrders.add(DrawingOrder(
    operation = CIRCLE,
    entity = this,
    data = listOf(place.x, place.y, PipeBase.Radius)
  ))

  return drawingOrders.toList()
}

fun PipeBase.drawSection(): List<DrawingOrder> {
  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()
  val venue = Venue.instances.first()
  val place = origin.venue

  drawingOrders.add(DrawingOrder(
    operation = RECTANGLE,
    entity = this,
    data = listOf(
      venue.depth - place.y - PipeBase.Radius,
      venue.height - place.z - PipeBase.Height,
      PipeBase.Radius * 2,
      PipeBase.Height
    )
  ))

  return drawingOrders.toList()
}

fun Pipe.drawPlan(): List<DrawingOrder> {
  return when (vertical) {
    true -> drawPlanVertical()
    else -> drawPlanHorizontal()
  }
}

fun Pipe.drawPlanVertical(): List<DrawingOrder> {
  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()
  val place = origin.venue

  drawingOrders.add(DrawingOrder(
    operation = CIRCLE,
    entity = this,
    data = listOf(place.x, place.y, Pipe.Diameter / 2)
  ))

  return drawingOrders.toList()
}

fun Pipe.drawPlanHorizontal(): List<DrawingOrder> {
  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()
  val place = origin.venue

  drawingOrders.add(DrawingOrder(
    operation = RECTANGLE,
    entity = this,
    data = listOf(place.x, place.y, length, Pipe.Diameter)
  ))
  val offsetToCenter = length / 2
  dependents.forEach {
    val location = place.x + it.location + offsetToCenter
    val luminaire = it.hangable as Luminaire
    drawingOrders.add(DrawingOrder(
      operation = USE,
      entity = luminaire,
      data = listOf(location, place.y),
      useType = luminaire.type,
    ))
  }
  return drawingOrders.toList()
}

fun Pipe.drawSection(): List<DrawingOrder> {
  return when (vertical) {
    true -> drawSectionVertical()
    else -> drawSectionHorizontal()
  }
}

fun Pipe.drawSectionVertical(): List<DrawingOrder> {
  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()
  val venue = Venue.instances.first()
  val place = origin.venue

  drawingOrders.add(DrawingOrder(
    operation = RECTANGLE,
    entity = this,
    data = listOf(
      venue.depth - place.y,
      venue.height - place.z - length,
      Pipe.Diameter,
      length,
    )
  ))
  dependents.forEach {
    when (it.hangable) {
      is Luminaire -> {
        val luminaire = it.hangable as Luminaire
        drawingOrders.add(DrawingOrder(
          operation = USE,
          entity = luminaire,
          data = listOf(
            venue.depth - place.y,
            venue.height - place.z - it.location,
          ),
          useType = luminaire.type,
        ))
      }
      is Pipe -> {}
    }
  }

  return drawingOrders.toList()
}

fun Pipe.drawSectionHorizontal(): List<DrawingOrder> {
  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()
  val venue = Venue.instances.first()
  val place = origin.venue

  drawingOrders.add(DrawingOrder(
    operation = CIRCLE,
    entity = this,
    data = listOf(
      venue.depth - place.y,
      venue.height - place.z,
      Pipe.Diameter / 2
    )
  ))
  dependents.forEach {
    val luminaire = it.hangable as Luminaire
    drawingOrders.add(DrawingOrder(
      operation = USE,
      entity = luminaire,
      data = listOf(
        venue.depth - place.y,
        venue.height - place.z,
      ),
      useType = luminaire.type,
    ))
  }

  return drawingOrders.toList()
}

fun Setpiece.drawSection(): List<DrawingOrder> {
  return parts.flatMap { it.drawSection(origin.venue) }
}

fun SetPlatform.drawSection(placement: VenuePoint): List<DrawingOrder> {
  val platformPlacement = placement + origin
  return shapes.flatMap { it.drawSection(placement, platformPlacement) }
}

fun Shape.drawSection(placement: VenuePoint, platformPlacement: VenuePoint): List<DrawingOrder> {
  val venue = Venue.instances.first()
  val height = venue.height - platformPlacement.z
  val frontY = venue.depth - platformPlacement.y - rectangle.depth / 2
  val rearY = venue.depth - platformPlacement.y + rectangle.depth / 2
  val floorLevelAtplatformFront = Floor.levelAt(
    platformPlacement.x - rectangle.width / 2,
    platformPlacement.y - rectangle.depth / 2
  )
  val floorLevelAtplatformRear = Floor.levelAt(
    platformPlacement.x - rectangle.width / 2,
    platformPlacement.y + rectangle.depth / 2
  )
  val floor = venue.height - placement.z

  val drawingOrders: MutableList<DrawingOrder> = mutableListOf()

  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(frontY, height, frontY + rectangle.depth, height)
  ))
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(frontY, height, frontY, venue.height - floorLevelAtplatformFront)
  ))
  drawingOrders.add(DrawingOrder(
    operation = LINE,
    entity = this,
    data = listOf(rearY, height, rearY, venue.height - floorLevelAtplatformRear)
  ))

  return drawingOrders.toList()
}