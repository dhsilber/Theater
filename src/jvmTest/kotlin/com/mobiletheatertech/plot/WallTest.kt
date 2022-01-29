package com.mobiletheatertech.plot

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class)
class WallTest {

  @Test
  fun `is elemental`() {
    val element = IIOMetadataNode()
    val wall = Wall.factory(element)
    assertIs<Elemental>(wall)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithElement<Wall>>(Wall)
  }

}