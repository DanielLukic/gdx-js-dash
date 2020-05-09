package intensigame.util

import intensigame.graphics.Position

object FixedMath {
    const val FIXED_SHIFT = 12
    const val FIXED_MULTIPLIER = 1 shl FIXED_SHIFT
    const val FIXED_MASK = FIXED_MULTIPLIER - 1
    const val FIXED_ONE = FIXED_MULTIPLIER
    const val FIXED_HALF = FIXED_ONE shr 1
    const val FIXED_0 = 0
    const val FIXED_0_1 = FIXED_ONE / 10
    const val FIXED_0_2 = FIXED_ONE / 5
    const val FIXED_0_25 = FIXED_ONE / 4
    const val FIXED_0_5 = FIXED_ONE / 2
    const val FIXED_1 = FIXED_ONE
    const val FIXED_5 = FIXED_ONE * 5
    const val FIXED_10 = FIXED_ONE * 10
    const val FIXED_25 = FIXED_ONE * 25
    const val FIXED_30 = FIXED_ONE * 30
    const val FIXED_50 = FIXED_ONE * 50
    const val FIXED_100 = FIXED_ONE * 100
    const val FIXED_180 = FIXED_ONE * 180
    const val FIXED_360 = FIXED_ONE * 360
    fun toFixed(aInteger: Int): Int {
        return aInteger shl FIXED_SHIFT
    }

    fun toInt(aFixedDecimal: Int): Int {
        return aFixedDecimal shr FIXED_SHIFT
    }

    fun toInt(aFixedDecimal: Long): Long {
        return aFixedDecimal shr FIXED_SHIFT
    }

    fun toIntRounded(aFixedDecimal: Int): Int {
        return aFixedDecimal + FIXED_HALF shr FIXED_SHIFT
    }

    fun toFixed(aPosition: Position) {
        aPosition.x = toFixed(aPosition.x)
        aPosition.y = toFixed(aPosition.y)
    }

    fun toInt(aFixedPosition: Position) {
        aFixedPosition.x = toInt(aFixedPosition.x)
        aFixedPosition.y = toInt(aFixedPosition.y)
    }

    fun toIntRounded(aFixedPosition: Position) {
        aFixedPosition.x = toIntRounded(aFixedPosition.x)
        aFixedPosition.y = toIntRounded(aFixedPosition.y)
    }

    fun fraction(aFixedDecimal: Int): Int {
        return aFixedDecimal and FIXED_MASK
    }

    fun mul(aFixedDecimal1: Int, aFixedDecimal2: Int): Int {
        val a = aFixedDecimal1.toLong() * aFixedDecimal2.toLong()
        return (a shr FIXED_SHIFT).toInt()
    }

    fun div(aFixedDecimal1: Int, aFixedDecimal2: Int): Int {
        val a = aFixedDecimal1.toLong()
        val b = aFixedDecimal2.toLong()
        return ((a shl FIXED_SHIFT) / b).toInt()
    }

    fun length(aFixedDeltaX: Int, aFixedDeltaY: Int): Int {
        val a2 = mul(aFixedDeltaX, aFixedDeltaX).toLong()
        val b2 = mul(aFixedDeltaY, aFixedDeltaY).toLong()
        val c2 = toInt(a2 + b2 shl LENGTH_PRE_SHIFT)
        val length = IntegerSquareRoot.sqrt(c2.toInt())
        return toFixed(length) shr LENGTH_POST_SHIFT
    }

    fun toString(aFixedValue: Int): String {
        return Integer.toString(toInt(aFixedValue))
    }

    private const val LENGTH_PRE_SHIFT = FIXED_SHIFT
    private const val LENGTH_POST_SHIFT = LENGTH_PRE_SHIFT / 2
}
