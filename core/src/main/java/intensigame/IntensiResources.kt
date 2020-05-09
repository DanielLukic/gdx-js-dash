@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package intensigame

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import intensigame.graphics.*

class IntensiResources(
    private val engine: IntensiEngine
) {
    private val assetManager: AssetManager = engine.assetManager

    val screenWidth: Int = engine.screenWidth
    val screenHeight: Int = engine.screenHeight

    fun update() = assetManager.update(100)

    fun createImageResource(aWidth: Int, aHeight: Int) = ImageResource(aWidth, aHeight)
    fun createPixelData(aWidth: Int, aHeight: Int) = PixelData(aWidth, aHeight)
    fun loadData(aResourcePath: String): ByteArray = assetManager.get(aResourcePath)
    fun loadImageResource(aResourcePath: String) = ImageResource(assetManager.get(aResourcePath))
    fun loadMusic(aResourcePath: String) = engine.audioSystem.loadMusic(aResourcePath)
    fun loadPixelData(aResourcePath: String) = PixelData(assetManager.get(aResourcePath))
    fun loadShader(aResourcePath: String): ShaderProgram = assetManager.get(aResourcePath)
    fun loadSound(aResourcePath: String) = engine.audioSystem.loadSound(aResourcePath)
    fun loadString(aResourcePath: String) = String(loadData(aResourcePath))

    fun loadFontGenerator(aResourcePath: String): FontGenerator {
        val imageResource = loadImageResource("$aResourcePath.png")
        val chars = CharGenerator.fromLayout(imageResource, 16, 8)
        return if (assetManager.isLoaded("$aResourcePath.dst"))
            BitmapFontGenerator(chars, assetManager.get("$aResourcePath.dst"))
        else
            BitmapFontGenerator(chars)
    }
}
