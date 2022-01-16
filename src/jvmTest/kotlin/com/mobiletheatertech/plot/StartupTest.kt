package com.mobiletheatertech.plot

import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class StartupTest {

  @Test
  fun `registers Plot objects to get their XML elements`() {
    Startup().startup()
    assertContains(TagRegistry.tagToCallback, Luminaire.Tag )
    assertEquals(1, TagRegistry.tagToCallback.size)
  }

}