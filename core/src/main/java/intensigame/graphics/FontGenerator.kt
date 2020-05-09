package intensigame.graphics

abstract class FontGenerator {

    fun maxCharWidth(aString: String): Int {
        var maxWidth = 0
        for (idx in 0 until aString.length) {
            maxWidth = Math.max(maxWidth, charWidth(aString[idx]))
        }
        return maxWidth
    }

    fun stringWidth(aString: String): Int {
        return substringWidth(aString, 0, aString.length)
    }

    fun blitString(aGraphics: Graphics, aText: String, aX: Int, aY: Int) {
        val textLength = aText.length
        blitString(aGraphics, aText, 0, textLength, aX, aY)
    }

    fun blitString(aGraphics: Graphics, aText: String, aX: Int, aY: Int, aAlignment: Int) {
        val textLength = aText.length
        val alignedPosition = getAlignedPosition(aX, aY, stringWidth(aText), charHeight(), aAlignment)
        blitString(aGraphics, aText, 0, textLength, alignedPosition.x, alignedPosition.y)
    }

    fun blitString(aGraphics: Graphics, aText: String, aPosition: Position, aAlignment: Int) {
        val textLength = aText.length
        val alignedPosition = getAlignedPosition(aPosition, stringWidth(aText), charHeight(), aAlignment)
        blitString(aGraphics, aText, 0, textLength, alignedPosition.x, alignedPosition.y)
    }

    fun blitText(aGraphics: Graphics, aText: String, aTextRect: Rectangle) {
        val textLength = aText.length
        var linePos = aTextRect.y
        var start = 0
        var end = start
        do {
            val nextSpaceOffset = aText.indexOf(" ", end + 1)
            if (nextSpaceOffset == -1) {
                val newWidth = substringWidth(aText, start, textLength - start)
                if (newWidth <= aTextRect.width) end = textLength
                if (start == end) end = textLength
            }
            else {
                val newWidth = substringWidth(aText, start, nextSpaceOffset - start)
                if (newWidth <= aTextRect.width) {
                    end = nextSpaceOffset
                    continue
                }
            }
            val eolIndex = findEOL(aText, start)
            if (eolIndex >= start && eolIndex < end) end = eolIndex
            val textWidth = substringWidth(aText, start, end - start)
            val offsetX = (aTextRect.width - textWidth) / 2
            blitString(aGraphics, aText, start, end, aTextRect.x + offsetX, linePos)
            linePos += charHeight()
            if (end < textLength && (aText[end] == '\n' || aText[end] == '|')) {
                end++
                linePos += charHeight() / 2
            }
            while (end < textLength && aText[end] == ' ') end++
            start = end
        } while (end < textLength)
    }

    private fun findEOL(aText: String, aStart: Int): Int {
        val eolIndex1 = aText.indexOf("\n", aStart)
        val eolIndex2 = aText.indexOf("|", aStart)
        if (eolIndex1 == -1) return eolIndex2
        return if (eolIndex2 == -1) eolIndex1 else Math.min(eolIndex1, eolIndex2)
    }

    fun blitNumber(aGraphics: Graphics, aPosition: Position, aNumber: Int, aAlignment: Int) {
        val digits = getNumberOfDigits(aNumber)
        var value = Math.abs(aNumber)
        var width = if (aNumber < 0) charWidth('-') else 0
        for (idx in 0 until digits) {
            val digit = value % 10
            width += charWidth(('0'.code + digit).toChar())
            value /= 10
        }
        val alignedPosition = getAlignedPosition(aPosition, width, charHeight(), aAlignment)
        value = Math.abs(aNumber)
        var x = alignedPosition.x + width
        for (idx in 0 until digits) {
            val digit = value % 10
            x -= charWidth(('0'.code + digit).toChar())
            blitChar(aGraphics, x, alignedPosition.y, '0'.code + digit)
            value /= 10
        }
        if (aNumber < 0) blitChar(aGraphics, x - charWidth('-'), alignedPosition.y, '-'.code)
    }

    fun blitNumber(aGraphics: Graphics, aPosition: Position, aNumber: Int, aAlignment: Int, aNumberOfDigits: Int) {
        val digitWidth = maxDigitCharWidth()
        val digits = Math.max(getNumberOfDigits(aNumber), aNumberOfDigits)
        val alignedPosition = getAlignedPosition(aPosition, digits * digitWidth, charHeight(), aAlignment)
        var value = Math.abs(aNumber)
        var x = alignedPosition.x + digitWidth * (digits - 1)
        for (idx in 0 until digits) {
            val digit = value % 10
            blitChar(aGraphics, x, alignedPosition.y, '0'.code + digit)
            x -= digitWidth
            value /= 10
        }
        if (aNumber < 0) blitChar(aGraphics, x, alignedPosition.y, '-'.code)
    }

    // Abstract Interface
    abstract fun charData(aCharCode: Char): CharData?
    abstract fun charHeight(): Int
    abstract fun charWidth(aCharCode: Char): Int
    abstract fun maxCharWidth(): Int
    abstract fun maxDigitCharWidth(): Int
    abstract fun substringWidth(aString: String, aOffset: Int, aLength: Int): Int
    abstract fun blitChar(aGraphics: Graphics, aX: Int, aY: Int, aAsciiCode: Int)
    abstract fun blitString(aGraphics: Graphics, aText: String, aStart: Int, aEnd: Int, aX: Int, aY: Int)

    @Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection", "unused")
    companion object {

        const val TOP = 0
        const val LEFT = 0
        const val BOTTOM = 1
        const val RIGHT = 2
        const val HCENTER = 4
        const val VCENTER = 8
        const val TOP_LEFT = TOP or LEFT
        const val CENTER = HCENTER or VCENTER

        fun getAlignedPosition(aX: Int, aY: Int, aWidth: Int, aHeight: Int, aAlignment: Int): Position {
            myBlitPos.x = aX
            myBlitPos.y = aY
            return getAlignedPosition(myBlitPos, aWidth, aHeight, aAlignment)
        }

        fun getAlignedPosition(aPosition: Position, aWidth: Int, aHeight: Int, aAlignment: Int): Position {
            myBlitPos.x = aPosition.x
            myBlitPos.y = aPosition.y
            if (aAlignment and HCENTER != 0) myBlitPos.x -= aWidth / 2 else if (aAlignment and RIGHT != 0) myBlitPos.x -= aWidth
            if (aAlignment and VCENTER != 0) myBlitPos.y -= aHeight / 2 else if (aAlignment and BOTTOM != 0) myBlitPos.y -= aHeight
            return myBlitPos
        }

        fun getNumberOfDigits(aNumber: Int): Int {
            var digits = 1
            var value = aNumber
            while (value > 9) {
                digits++
                value /= 10
            }
            return digits
        }

        private val myBlitPos = Position()
    }
}
