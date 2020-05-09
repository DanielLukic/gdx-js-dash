package intensigame.graphics

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.disposeSafely

class ImageResource(
    val texture: Texture
) {
    constructor(aWidth: Int, aHeight: Int) : this(Texture(aWidth, aHeight, Pixmap.Format.RGBA8888))

    val width = texture.width
    val height: Int = texture.height

    fun pixelData(): PixelData {
        val data = texture.textureData
        if (!data.isPrepared) data.prepare()
        val pixels = data.consumePixmap()
        val pixmap = Pixmap(pixels.width, pixels.height, Pixmap.Format.RGBA8888)
        pixmap.drawPixmap(pixels, 0, 0)
        pixels.dispose() // TODO ?
        return PixelData(pixmap)
    }

    fun region(aX: Int, aY: Int, aWidth: Int, aHeight: Int) =
        ImageRegion(TextureRegion(texture, aX, aY, aWidth, aHeight))

    fun dispose() = texture.disposeSafely()
}
