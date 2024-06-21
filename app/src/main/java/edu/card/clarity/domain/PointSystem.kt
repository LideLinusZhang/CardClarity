package edu.card.clarity.domain

class PointSystem (
    val name: String,
    val pointToCashConversionRate: Float
) {
    fun getEquivalentCashAmount(pointAmount: Int): Float = pointAmount * pointToCashConversionRate
}