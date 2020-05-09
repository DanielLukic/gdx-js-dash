package intensigame.graphics

import intensigame.util.MathExtended

data class PositionF(
    var x: Float = 0f,
    var y: Float = 0f
) {
    constructor(aPosition: PositionF) : this(aPosition.x, aPosition.y)
    constructor(aPosition: Position) : this(aPosition.x.toFloat(), aPosition.y.toFloat())

    fun isZero() = x == 0f && y == 0f

    fun setTo(aX: Int, aY: Int) = apply { x = aX.toFloat(); y = aY.toFloat() }
    fun setTo(aX: Float, aY: Float) = apply { x = aX; y = aY }
    fun setTo(aPosition: PositionF) = apply { x = aPosition.x; y = aPosition.y }
    fun translate(aPosition: PositionF) = apply { x += aPosition.x; y += aPosition.y }
    fun translate(aX: Float, aY: Float) = apply { x += aX; y += aY }
    fun scale(aValue: Float) = apply { x *= aValue; y *= aValue }

    fun translated(aX: Int, aY: Int) = PositionF(x + aX, y + aY)

    fun distanceTo(aPositionFixed: PositionF): Float {
        return MathExtended.length(x - aPositionFixed.x, y - aPositionFixed.y)
    }

    fun normalize() {
        val length = MathExtended.length(x, y)
        if (length == 0f) return
        x /= length
        y /= length
    }

}
