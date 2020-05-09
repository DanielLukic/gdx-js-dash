package intensigame.graphics

import intensigame.util.FixedMath

data class Position(
    var x: Int = 0,
    var y: Int = 0
) {
    constructor(aPosition: Position) : this(aPosition.x, aPosition.y)

    fun isZero() = x == 0 && y == 0
    fun equals(aX: Int, aY: Int) = x == aX && y == aY

    fun translate(aPosition: Position) {
        x += aPosition.x
        y += aPosition.y
    }

    fun setTo(aX: Int, aY: Int) {
        x = aX
        y = aY
    }

    fun setTo(aPosition: Position) {
        x = aPosition.x
        y = aPosition.y
    }

    fun scaleFixed(aValueFixed: Int) {
        x = FixedMath.mul(x, aValueFixed)
        y = FixedMath.mul(y, aValueFixed)
    }

    fun distanceToFixed(aPositionFixed: Position): Int {
        return FixedMath.length(x - aPositionFixed.x, y - aPositionFixed.y)
    }

    fun normalizeFixed() {
        val length = FixedMath.length(x, y)
        if (length == 0) return
        x = FixedMath.div(x, length)
        y = FixedMath.div(y, length)
    }

    fun validDirection(): Boolean {
        return x != 0 || y != 0
    }
}
