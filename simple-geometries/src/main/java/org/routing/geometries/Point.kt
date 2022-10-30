package org.routing.geometries

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Simple WGS84 Geometry
 */
class Point(
        private val lat: Float,
        private val lon: Float
) {

    fun distance(other: Point) : Float {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians((other.lat - this.lat).toDouble())
        val dLng = Math.toRadians((other.lon - this.lon).toDouble())
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(this.lat.toDouble())) *
                cos(Math.toRadians(other.lat.toDouble())) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadius * c).toFloat()
    }
}
