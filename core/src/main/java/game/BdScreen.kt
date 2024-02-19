package game

import com.badlogic.gdx.graphics.Texture
import intensigame.IntensiEngine
import intensigame.IntensiResources
import intensigame.IntensiScreen
import intensigame.graphics.Graphics
import intensigame.graphics.ImageResource
import intensigame.log.Log

internal class BdScreen : IntensiScreen() {

    private lateinit var sprites: ImageResource

    override fun IntensiResources.prepare() {
        engine.assetManager.load("sprites.png", Texture::class.java)
        Log.info(CAVES[0].toString())
    }

    override suspend fun enactScript() = waitUntilCancelled()

    private var renderDelta = 0f // to accommodate the game/render split

    override fun IntensiEngine.update(delta: Float) {
        game_frame(delta)
        renderDelta += delta
    }

    private var loaded = false

    override fun Graphics.draw() {
        if (!engine.assetManager.isLoaded("sprites.png")) return
        if (!::sprites.isInitialized) sprites = engine.resources.loadImageResource("sprites.png")
        if (!loaded) {
            load(this, sprites)
            loaded = true
        }
        render_frame(renderDelta)
        renderDelta = 0f
    }
}
