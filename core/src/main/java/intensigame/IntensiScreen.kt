package intensigame

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import intensigame.graphics.Graphics
import intensigame.graphics.Shader
import intensigame.log.Log
import intensigame.log.logInfo
import intensigame.log.logVerbose
import intensigame.log.logWarn
import kotlinx.coroutines.*
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

@Suppress("MemberVisibilityCanBePrivate")
abstract class IntensiScreen(
    protected val disposeOnHide: Boolean = true,
    protected val cancelScriptOnHide: Boolean = true
) : KtxScreen {

    protected lateinit var engine: IntensiEngine

    private val screenCamera = OrthographicCamera()
    private val screenViewport: Viewport = ScreenViewport(screenCamera).apply {
        unitsPerPixel = 1f // TODO Is this relevant?
    }
    private val screenBatch = SpriteBatch().apply { color = Color.WHITE }
    private val graphics by lazy { Graphics(engine.screenWidth, engine.screenHeight) }

    private var scriptJob: Job? = null
    private var disposed = false

    private val keyReactions = KeyReactions()

    fun onKeyUp(keyCode: Int): Boolean = keyReactions.onKeyUp(keyCode)
    fun onKeyDown(keyCode: Int): Boolean = keyReactions.onKeyDown(keyCode)
    fun whenKeyUp(keyCode: Int, onKey: (Int) -> Any?) = keyReactions.whenKeyUp(keyCode, onKey)
    fun whenKeyDown(keyCode: Int, onKey: (Int) -> Any?) = keyReactions.whenKeyDown(keyCode, onKey)
    fun whenKeyDown(keyCodes: List<Int>, onKeyDown: (Int) -> Any?) = keyReactions.whenKeyDown(keyCodes, onKeyDown)

    fun init(engine: IntensiEngine) {
        Log.verifyThread("main")
        this.engine = engine
        this.engine.resources.prepare()
    }

    open fun IntensiResources.prepare() = Unit

    open suspend fun enact(scope: CoroutineScope): Job = scope.run {
        Log.verifyThread("main")
        while (!engine.resources.update()) {
            logWarn { "screen not ready - waiting" }
            yield()
        }
        launch { enactScript() }.also { scriptJob = it }
    }

    open suspend fun enactScript() = Unit

    protected suspend fun waitUntilCancelled() = CompletableDeferred<Unit>().await()

    fun cancelScript() = scriptJob?.cancel() ?: Unit

    abstract fun IntensiEngine.update(delta: Float)

    abstract fun Graphics.draw()

    open fun postProcessing(): Shader? = null

    open fun disposeSafely() = Unit

    override fun resize(width: Int, height: Int) {
        Log.verifyThread("main")
        logVerbose { "resize $width x $height" }
        screenViewport.update(width, height, true)
    }

    private var remainingDelta = 0f

    private var tickTime = 1 / 60f

    private var logPostProcessing: Any? = null

    override fun render(delta: Float) {
        Log.verifyThread("main")

        val estimatedFps = (1 / delta).toInt()
        if (estimatedFps < 55) logWarn { "estimated FPS: $estimatedFps" }
        else print("FPS: $estimatedFps\r")

        val ready = engine.resources.update()
        if (!ready) {
            logWarn { "screen $this not ready" }
            return
        }

        val combinedDelta = delta + remainingDelta
        val ticks = (combinedDelta / tickTime).toInt()
        if (ticks > 1) {
            logVerbose { "tickTime       $tickTime" }
            logVerbose { "delta          $delta" }
            logVerbose { "remainingDelta $remainingDelta" }
            logVerbose { "combinedDelta  $combinedDelta" }
            logInfo { "ticks          $ticks" }
        }
        remainingDelta = combinedDelta - ticks * tickTime
        repeat(ticks) {
            runCatching { engine.update(tickTime) }.onFailure { Log.error(it); engine.pause() }
        }

        graphics.use { graphics.draw() }
        postProcessing().let {
            if (logPostProcessing != it) logInfo { "post processing: $it" }
            logPostProcessing = it
            if (it != null) graphics.postProcess(it)
        }

        screenBatch.projectionMatrix = screenCamera.combined
        screenBatch.use {
            val sw = screenViewport.screenWidth.toFloat()
            val sh = screenViewport.screenHeight.toFloat()
            val width = engine.screenWidth
            val height = engine.screenHeight
            val times = minOf(sw / width, sh / height)
            val xOff = (screenViewport.screenWidth - width * times) / 2
            val yOff = (screenViewport.screenHeight - height * times) / 2
            graphics.output.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            screenBatch.draw(graphics.output, xOff, yOff, width * times, height * times)
        }
    }

    override fun hide() {
        Log.verifyThread("main")
        if (cancelScriptOnHide) scriptJob?.cancel()
        if (disposeOnHide) dispose()
    }

    override fun dispose() {
        Log.verifyThread("main")
        if (disposed) return
        disposed = true
        runCatching { disposeSafely() }.onFailure(Log::error)
        graphics.dispose()
        screenBatch.disposeSafely()
    }
}
