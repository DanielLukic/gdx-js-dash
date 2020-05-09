package intensigame.graphics

class BitmapFontGenerator(
    aCharGen: CharGenerator,
    aWidths: ByteArray? = null
) : FontGenerator() {

    companion object {
        private const val MIN_ASCII_CODE = 32
    }

    private val myCharHeight: Int
    private val myCharWidths: ByteArray
    private val myCharGen: CharGenerator = aCharGen

    init {
        myCharHeight = myCharGen.charHeight
        myCharWidths = aWidths ?: run {
            if (aCharGen.charWidth > 127) throw RuntimeException("nyi")
            val width = aCharGen.charWidth.toByte()
            val numberOfChars = aCharGen.charsPerRow * aCharGen.charsPerColumn
            val charWidths = ByteArray(numberOfChars)
            for (idx in 0 until numberOfChars) charWidths[idx] = width
            charWidths
        }
    }

    // From FontGenerator

    override fun charData(aCharCode: Char): CharData {
        return myCharGen.getCharData(aCharCode.code - 32)
    }

    override fun charHeight(): Int {
        return myCharHeight
    }

    override fun charWidth(aCharCode: Char): Int {
        return if (aCharCode.code < MIN_ASCII_CODE || aCharCode.code - MIN_ASCII_CODE > myCharWidths.size) 0 else myCharWidths[aCharCode.code - MIN_ASCII_CODE].toInt()
    }

    override fun maxCharWidth(): Int {
        return myCharGen.charWidth
    }

    override fun maxDigitCharWidth(): Int {
        return myCharGen.charWidth
    }

    override fun substringWidth(aString: String, aOffset: Int, aLength: Int): Int {
        var length = 0
        for (idx in aOffset until aOffset + aLength) {
            length += charWidth(aString[idx])
        }
        return length
    }

    override fun blitChar(aGraphics: Graphics, aX: Int, aY: Int, aAsciiCode: Int) {
        val index = aAsciiCode - MIN_ASCII_CODE
        if (index < 0 || index >= myCharWidths.size) return
        val width = myCharWidths[index].toInt()
        myCharGen.blit(aGraphics, aX, aY, index, width)
    }

    override fun blitString(aGraphics: Graphics, aText: String, aStart: Int, aEnd: Int, aX: Int, aY: Int) {
        if (aText.isEmpty()) return
        blitStringUnbuffered(aGraphics, aText, aStart, aEnd, aX, aY)
    }

    // Implementation
    private fun blitStringUnbuffered(aGraphics: Graphics, aText: String, aStart: Int, aEnd: Int, aX: Int, aY: Int) {
        var x = aX
        for (idx in aStart until aEnd) {
            val code = aText[idx]
            blitChar(aGraphics, x, aY, code.code)
            x += charWidth(code)
        }
    }

    private fun getPart(aText: String, aStart: Int, aEnd: Int): String {
        return if (aStart == 0 && aEnd == aText.length) aText else aText.substring(aStart, aEnd)
    }
}
