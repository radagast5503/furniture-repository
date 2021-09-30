package com.muebles.ra.api

data class Furniture(val name: String, val deviceId: String) {
    fun valid(): Boolean = !name.isNullOrBlank() && !deviceId.isNullOrBlank()
}

data class FurnitureURL(val url: String)
