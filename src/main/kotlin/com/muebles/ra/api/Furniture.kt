package com.muebles.ra.api

data class Furniture(val name: String, val deviceId: String) {
    fun valid(): Boolean = !name.isNullOrBlank() && !deviceId.isNullOrBlank()

    fun reference(): String = "${deviceId}/${name}"

    fun virtualizedReference(): String = "${deviceId}/${name}-virtualized"
}

data class FurnitureURL(val url: String)
