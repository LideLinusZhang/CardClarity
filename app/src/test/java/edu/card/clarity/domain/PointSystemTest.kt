package edu.card.clarity.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class PointSystemTest {

    @Test
    fun getEquivalentCashAmount() {
        val pointSystem = PointSystem(
            id = UUID.randomUUID(),
            name = "Test Point System",
            pointToCashConversionRate = 0.01f  // 1 point = 0.01 cash
        )

        val points = 1000
        val expectedCashAmount = points * pointSystem.pointToCashConversionRate

        assertEquals(expectedCashAmount, pointSystem.getEquivalentCashAmount(points))
    }
}