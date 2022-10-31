package org.routing.geometries

import java.io.Serializable
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Simple WGS84 Geometry
 */
class Point(
    val lat: Float,
    val lon: Float
) : Serializable {

    fun distance(other: Point): Float {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (lat != other.lat) return false
        if (lon != other.lon) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lat.hashCode()
        result = 31 * result + lon.hashCode()
        return result
    }


}
