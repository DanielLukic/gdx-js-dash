package intensigame.graphics

class CharGenerator private constructor(aCharBitmap: ImageResource, aCharWidth: Int, aCharHeight: Int) {

    val charWidth: Int
    val charHeight: Int
    val charsPerRow: Int
    val charsPerColumn: Int

    val numberOfFrames: Int
        get() = charsPerRow * charsPerColumn

    @Suppress("MemberVisibilityCanBePrivate")
    fun getCharData(aCharCode: Int): CharData {
        if (aCharCode < 0 || aCharCode >= myDataCache.size) return getCharData(0)
        if (myDataCache[aCharCode] == null) {
            val column = aCharCode % charsPerRow
            val row = aCharCode / charsPerRow
            val x = column * charWidth
            val y = row * charHeight
            myDataCache[aCharCode] = CharData(myBitmap, x, y, charWidth, charHeight)
        }
        return myDataCache[aCharCode] ?: error("null char data")
    }

    fun blit(aGraphics: Graphics, aX: Int, aY: Int, aCharCode: Int) {
        val data = getCharData(aCharCode)
        data.blit(aGraphics, aX, aY)
    }

    fun blit(aGraphics: Graphics, aTargetRect: Rectangle, aCharCode: Int) {
        val data = getCharData(aCharCode)
        data.blit(aGraphics, aTargetRect)
    }

    fun blit(aGraphics: Graphics, aX: Int, aY: Int, aCharCode: Int, aWidth: Int) {
        val data = getCharData(aCharCode)
        data.blit(aGraphics, aX, aY, aWidth)
    }

    private val myDataCache: Array<CharData?>
    private val myBitmap: ImageResource

    companion object {
        fun fromLayout(aCharBitmap: ImageResource, aCharsPerRow: Int, aCharsPerColumn: Int): CharGenerator {
            val charWidth = aCharBitmap.width / aCharsPerRow
            val charHeight = aCharBitmap.height / aCharsPerColumn
            return CharGenerator(aCharBitmap, charWidth, charHeight)
        }

        @JvmStatic
        fun fromSize(aCharBitmap: ImageResource, aCharWidth: Int, aCharHeight: Int): CharGenerator {
            return CharGenerator(aCharBitmap, aCharWidth, aCharHeight)
        }
    }

    // Implementation
    init {
        val bitmapWidth = aCharBitmap.width
        val bitmapHeight = aCharBitmap.height
        val charsInRow = bitmapWidth / aCharWidth
        val charsInColumn = bitmapHeight / aCharHeight
        //#if DEBUG
        require(!(charsInRow * aCharWidth != bitmapWidth || charsInColumn * aCharHeight != bitmapHeight)) { "Char size mismatch" }
        //#endif
        myBitmap = aCharBitmap
        charWidth = aCharWidth
        charHeight = aCharHeight
        myDataCache = arrayOfNulls(charsInRow * charsInColumn)
        charsPerRow = charsInRow
        charsPerColumn = charsInColumn
    }
}
