package intensigame.graphics

class CharData(
    private val myCharSet: ImageResource,
    aX: Int,
    aY: Int,
    aCharWidth: Int,
    aCharHeight: Int
) {
    private val myCharRect = Rectangle(aX, aY, aCharWidth, aCharHeight)

    fun blit(aGraphics: Graphics, aTargetRect: Rectangle) {
        aGraphics.drawImage(myCharSet, myCharRect, aTargetRect)
    }

    fun blit(aGraphics: Graphics, aX: Int, aY: Int) {
        aGraphics.drawImage(myCharSet, myCharRect, aX, aY)
    }

    fun blit(aGraphics: Graphics, aX: Int, aY: Int, aWidth: Int) {
        val originalWidth = myCharRect.width
        myCharRect.width = aWidth
        aGraphics.drawImage(myCharSet, myCharRect, aX, aY)
        myCharRect.width = originalWidth
    }

    fun blit(aGraphics: Graphics, aX: Int, aY: Int, aOffsetX: Int, aWidth: Int) {
        val originalX = myCharRect.x
        val originalWidth = myCharRect.width
        myCharRect.x += aOffsetX
        myCharRect.width = aWidth
        aGraphics.drawImage(myCharSet, myCharRect, aX, aY)
        myCharRect.x = originalX
        myCharRect.width = originalWidth
    }

    private val blitSource = Rectangle()
    private val blitDestination = Rectangle()

    fun blit(aGraphics: Graphics, aX: Int, aY: Int, aOffsetX: Int, aWidth: Int, aHeight: Int) {
        blitSource.setTo(myCharRect)
        blitSource.x += aOffsetX
        blitSource.width = aWidth
        blitDestination.x = aX
        blitDestination.y = aY
        blitDestination.width = aWidth
        blitDestination.height = aHeight
        aGraphics.drawImage(myCharSet, blitSource, blitDestination)
    }
}
