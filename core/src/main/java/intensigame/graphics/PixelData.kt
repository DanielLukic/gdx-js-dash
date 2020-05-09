package intensigame.graphics

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture

class PixelData(val pixmap: Pixmap) {

    constructor(aWidth: Int, aHeight: Int) : this(Pixmap(aWidth, aHeight, Pixmap.Format.RGBA8888))

    val width = pixmap.width
    val height = pixmap.height

    fun rgb(x: Int, y: Int) = pixmap.getPixel(x, y) ushr 8
    fun rgb(x: Int, y: Int, rgb888: Int) = pixmap.drawPixel(x, y, rgb888 shl 8 or 0xFF)

    fun cloned() = PixelData(width, height).also {
        it.pixmap.drawPixmap(pixmap, 0, 0)
    }

    fun imageResource() = ImageResource(Texture(pixmap))

}
