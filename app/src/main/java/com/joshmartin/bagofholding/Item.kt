package com.joshmartin.bagofholding

data class Item(
    var name: String,
    var weight: Double,
    var cost: String,
) {
    override fun toString(): String = "$name, $weight lbs, $cost"
}