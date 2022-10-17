package tests.display

import androidx.compose.ui.graphics.Color
import display.DrawingOrderOperation
import display.IndependentColor
import display.drawPlan
import display.drawSection
import entities.Floor
import entities.Pipe
import entities.PipeBase
import entities.Proscenium
import entities.Venue
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.assertj.core.api.SoftAssertions
import kotlin.test.Test
import tests.entities.FloorTest
import tests.entities.PipeBaseTest
import tests.entities.PipeTest
import tests.entities.ProsceniumTest
import tests.entities.VenueTest

class EntitiesDrawTest {

  @Test
  fun `Proscenium drawPlan orders`() {
    val proscenium = Proscenium.factory(ProsceniumTest().minimalXml())

    val orders = proscenium.drawPlan()

    assertThat(orders).hasSize(6)
    assertThat(orders).extracting("operation", "entity")
      .containsOnly(
        tuple(DrawingOrderOperation.LINE, proscenium),
      )
    // Colors not tested
  }

  @Test
  fun `Floor drawPlan orders filled rectangle`() {
    val floor = Floor.factory(FloorTest().minimalXml())

    val orders = floor.drawPlan()

    assertThat(orders).hasSize(1)
    val order = orders.first()
    SoftAssertions().apply {
      assertThat(order.operation).isEqualTo(DrawingOrderOperation.FILLED_RECTANGLE)
      assertThat(order.entity).isInstanceOf(Floor::class.java)
      assertThat(order.color).isEqualTo(IndependentColor(Color.Gray, "grey"))
      assertThat(order.opacity).isEqualTo(0.1F)
    }.assertAll()
  }

  @Test
  fun `Floor drawSection orders`() {
    Venue.factory(VenueTest().minimalXml())
    val floor = Floor.factory(FloorTest().minimalXml())

    val orders = floor.drawSection()

    assertThat(orders).hasSize(3)
    assertThat(orders).extracting("operation", "entity")
      .containsExactlyInAnyOrder(
        tuple(DrawingOrderOperation.LINE, floor),
        tuple(DrawingOrderOperation.LINE, floor),
        tuple(DrawingOrderOperation.FILLED_RECTANGLE, floor)
      )
    assertThat(orders).extracting("operation", "entity", "color", "opacity")
      .contains(
        tuple(DrawingOrderOperation.FILLED_RECTANGLE, floor, IndependentColor(Color.Gray, "grey"), 0.1f)

      )
  }

  @Test
  fun `Floor drawSection orders for slope`() {
    Venue.factory(VenueTest().minimalXml())
    val floor = Floor.factory(FloorTest().minimalSlopedXml())

    val orders = floor.drawSection()

    assertThat(orders).hasSize(2)
    assertThat(orders).extracting("operation", "entity")
      .containsExactlyInAnyOrder(
        tuple(DrawingOrderOperation.LINE, floor),
        tuple(DrawingOrderOperation.FILLED_RIGHT_TRIANGLE, floor)
      )
    assertThat(orders).extracting("operation", "entity", "color", "opacity")
      .contains(
        tuple(DrawingOrderOperation.FILLED_RIGHT_TRIANGLE, floor, IndependentColor(Color.Gray, "grey"), 0.1f)
      )
  }

  @Test
  fun `PipeBase drawPlan orders`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml())

    val orders = pipeBase.drawPlan()

    assertThat(orders).hasSize(1)
    val order = orders.first()
    SoftAssertions().apply {
      assertThat(order.operation).isEqualTo(DrawingOrderOperation.CIRCLE)
      assertThat(order.entity).isInstanceOf(PipeBase::class.java)
    }.assertAll()
  }

  @Test
  fun `PipeBase drawSection orders`() {
    Venue.factory(VenueTest().minimalXml())
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml())

    val orders = pipeBase.drawSection()

    assertThat(orders).hasSize(1)
    val order = orders.first()
    SoftAssertions().apply {
      assertThat(order.operation).isEqualTo(DrawingOrderOperation.RECTANGLE)
      assertThat(order.entity).isInstanceOf(PipeBase::class.java)
    }.assertAll()
  }

  @Test
  fun `Pipe (vertical) drawPlan orders`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val pipe = Pipe.factory(PipeTest().minimalXmlWithPipeBaseParent(), pipeBase)

    val orders = pipe.drawPlan()

    assertThat(orders).hasSize(1)
    val order = orders.first()
    SoftAssertions().apply {
      assertThat(order.operation).isEqualTo(DrawingOrderOperation.CIRCLE)
      assertThat(order.entity).isInstanceOf(Pipe::class.java)
    }.assertAll()
  }

  @Test
  fun `Pipe (vertical) drawSection orders`() {
    Venue.factory(VenueTest().minimalXml())
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val pipe = Pipe.factory(PipeTest().minimalXmlWithPipeBaseParent(), pipeBase)

    val orders = pipe.drawSection()

    assertThat(orders).hasSize(1)
    val order = orders.first()
    SoftAssertions().apply {
      assertThat(order.operation).isEqualTo(DrawingOrderOperation.RECTANGLE)
      assertThat(order.entity).isInstanceOf(Pipe::class.java)
    }.assertAll()
  }
}