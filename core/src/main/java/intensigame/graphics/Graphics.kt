package intensigame.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import intensigame.log.logInfo
import ktx.assets.disposeSafely
import ktx.graphics.use
import space.earlygrey.shapedrawer.ShapeDrawer

class Graphics(val width: Int, val height: Int) {

    private val buffers = arrayOf(
        FrameBuffer(Pixmap.Format.RGBA8888, width, height, false),
        FrameBuffer(Pixmap.Format.RGBA8888, width, height, false)
    )

    private val buffer get() = buffers[0]
    private val postProcessBuffer get() = buffers[1]

    private val camera = OrthographicCamera().apply { setToOrtho(true) }
    private val viewport: Viewport = ScreenViewport(camera).apply {
        unitsPerPixel = 1f
    }
    private val batch = SpriteBatch().apply { color = Color.WHITE }

    init {
        logInfo { "viewport.update($width, $height, centerCamera)" }
        viewport.update(width, height, true)
    }

    private val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
        setColor(Color.WHITE)
        drawPixel(0, 0)
    }.let { Texture(it).also { _ -> it.dispose() } }.let { TextureRegion(it, 0, 0, 1, 1) }

    private val drawer = ShapeDrawer(batch, pixmap)

    private var fontGenerator: FontGenerator? = null

    val output: TextureRegion get() = TextureRegion(buffers[0].colorBufferTexture)

    fun dispose() {
        batch.disposeSafely()
        buffers.forEach { it.disposeSafely() }
    }

    fun use(frame: Graphics.() -> Unit) = buffer.use {
        batch.projectionMatrix = camera.combined
        batch.use { /*clear();*/ frame(this) }
    }

    fun switchBatch(block: () -> Unit) {
        batch.end()
        block()
        batch.begin()
    }

    fun postProcess(shader: Shader) {
        postProcessBuffer.use {
            batch.projectionMatrix = camera.combined
            batch.use {
                clear()
                use(shader)
                batch.draw(output, 0f, 0f)
                useDefaultShader()
            }
        }
        buffers.reverse()
    }

    fun clear() {
        setColor(1f, 0f, 0f, 0f)
        fillRect(0, 0, width, height)
    }

    fun setColor(a: Float, r: Float, g: Float, b: Float) {
        drawer.setColor(r, g, b, a)
    }

    fun fillRect(aX: Int, aY: Int, aWidth: Int, aHeight: Int) {
        drawer.filledRectangle(aX.toFloat(), aY.toFloat(), aWidth.toFloat(), aHeight.toFloat())
    }

    fun useDefaultShader() {
        batch.shader = null
    }

    fun use(shader: Shader) {
        batch.shader = shader.program
        shader.updateBeforeUse()
    }

    fun setAlpha(a: Float) {
        batch.setColor(1f, 1f, 1f, a)
    }

    fun drawImage(aImage: ImageResource, aX: Int, aY: Int) {
        val texture = aImage.texture
        batch.draw(texture, aX.toFloat(), aY.toFloat())
    }

//    fun drawImage(aImage: ImageResource, aX: Int, aY: Int, aAlignment: Int) {
//        val position = Graphics.getAlignedPosition(aX, aY, aImage.width, aImage.height, aAlignment)
//        drawImage(aImage, position.x, position.y)
//    }

    fun drawImage(aImage: ImageResource, aSourceRect: Rectangle, aTargetX: Int, aTargetY: Int) {
        val it = aSourceRect.let {
            aImage.region(it.x, it.y, it.width, it.height)
        }
        batch.draw(it.data, aTargetX.toFloat(), aTargetY.toFloat())
    }

    fun drawImage(aImage: ImageResource, aSourceRect: Rectangle, aTargetRect: Rectangle) {
        val source = aSourceRect.let { aImage.region(it.x, it.y, it.width, it.height) }
        val target = aTargetRect.toRect()
        batch.draw(source.data, target.x, target.y, target.width, target.height)
    }

    fun drawImage(aImage: ImageResource, aTargetRect: Rectangle) {
        val texture = aImage.texture
        val target = aTargetRect.toRect()
        batch.draw(texture, target.x, target.y, target.width, target.height)
    }

    private fun Rectangle.toRect() = RectangleF(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())

    fun setFont(aFontGenerator: FontGenerator) {
        fontGenerator = aFontGenerator
    }

    fun drawText(aText: String, aX: Int, aY: Int) {
        fontGenerator?.blitString(this, aText, 0, aText.length, aX, aY)
    }

}
