package intensigame.graphics

data class Rectangle(
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) {
    operator fun set(aPosition: Position, aSize: Size) {
        x = aPosition.x
        y = aPosition.y
        width = aSize.width
        height = aSize.height
    }

    fun setTo(aRectangle: Rectangle) {
        x = aRectangle.x
        y = aRectangle.y
        width = aRectangle.width
        height = aRectangle.height
    }

    fun applyOutsets(aOutsetSizeInPixels: Int) {
        x -= aOutsetSizeInPixels
        y -= aOutsetSizeInPixels
        width += aOutsetSizeInPixels * 2
        height += aOutsetSizeInPixels * 2
    }

    fun setCenterAndSize(aPosition: Position, aSize: Size) {
        setCenterAndSize(aPosition, aSize.width, aSize.height)
    }

    fun setCenterAndSize(aPosition: Position, aWidth: Int, aHeight: Int) {
        //#if DEBUG
        require(!(aWidth < 0 || aHeight < 0))
        //#endif
        width = aWidth
        height = aHeight
        x = aPosition.x - width / 2
        y = aPosition.y - height / 2
    }

    operator fun contains(aPosition: Position): Boolean {
        if (aPosition.x < x || aPosition.x > x + width) return false
        return !(aPosition.y < y || aPosition.y > y + height)
    }

    fun contains(aX: Int, aY: Int): Boolean {
        if (aX < x || aX > x + width) return false
        return !(aY < y || aY > y + height)
    }

    fun intersectsWith(aRectangleFixed: Rectangle): Boolean {
        val x1 = aRectangleFixed.x
        val x2 = aRectangleFixed.x + aRectangleFixed.width
        if (x > x2 || x + width < x1) return false
        val y1 = aRectangleFixed.y
        val y2 = aRectangleFixed.y + aRectangleFixed.height
        return !(y > y2 || y + height < y1)
    }

    fun isAdjacent(that: Rectangle): Boolean {
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

    fun uniteWith(that: Rectangle) {
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
