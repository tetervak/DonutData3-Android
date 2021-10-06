package ca.tetervak.donutdata3.domain

import java.util.*

data class Donut(
    val id: String?,
    val name: String,
    val description: String,
    val rating: Float,
    val lowFat: Boolean = false,
    val brand: Brand = Brand.TIM_HORTONS,
    val imageFile: String = "cinnamon_sugar.png",
    val date: Date = Date()
)