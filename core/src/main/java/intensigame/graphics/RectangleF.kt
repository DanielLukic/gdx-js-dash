package intensigame.graphics

data class RectangleF(
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 0f,
    var height: Float = 0f
) {
    operator fun set(aPosition: Position, aSize: Size) {
        x = aPosition.x.toFloat()
        y = aPosition.y.toFloat()
        width = aSize.width.toFloat()
        height = aSize.height.toFloat()
    }

    fun setTo(aRectangle: RectangleF) {
        x = aRectangle.x
        y = aRectangle.y
        width = aRectangle.width
        height = aRectangle.height
    }

    fun applyOutsets(aOutsetSizeInPixels: Float) {
        x -= aOutsetSizeInPixels
        y -= aOutsetSizeInPixels
        width += aOutsetSizeInPixels * 2
        height += aOutsetSizeInPixels * 2
    }

    fun setCenterAndSize(aPosition: PositionF, aSize: SizeF) {
        setCenterAndSize(aPosition, aSize.width, aSize.height)
    }

    fun setCenterAndSize(aPosition: PositionF, aWidth: Float, aHeight: Float) {
        //#if DEBUG
        require(!(aWidth < 0 || aHeight < 0))
        //#endif
        width = aWidth
        height = aHeight
        x = aPosition.x - width / 2
        y = aPosition.y - height / 2
    }

    operator fun contains(aPosition: PositionF): Boolean {
        if (aPosition.x < x || aPosition.x > x + width) return false
        return if (aPosition.y < y || aPosition.y > y + height) false else true
    }

    fun contains(aX: Float, aY: Float): Boolean {
        if (aX < x || aX > x + width) return false
        return if (aY < y || aY > y + height) false else true
    }

    fun intersectsWith(aRectangleFixed: RectangleF): Boolean {
        val x1 = aRectangleFixed.x
        val x2 = aRectangleFixed.x + aRectangleFixed.width
        if (x > x2 || x + width < x1) return false
        val y1 = aRectangleFixed.y
        val y2 = aRectangleFixed.y + aRectangleFixed.height
        return if (y > y2 || y + height < y1) false else true
    }

    fun isAdjacent(that: RectangleF): Boolean {
        if (x == that.x && width == that.width) {
            if (y == that.y + that.height) return true
            if (y + height == that.y) return true
        }
        if (y == that.y && height == that.height) {
            if (x == that.x + that.width) return true
            if (x + width == that.x) return true
        }
        return false
    }

    fun uniteWith(that: RectangleF) {
        val left = Math.min(x, that.x)
        val top = Math.min(y, that.y)
        val right = Math.max(x + width, that.x + that.width)
        val bottom = Math.max(y + height, that.y + that.height)
        x = left
        y = top
        width = right - left
        height = bottom - top
    }

}
