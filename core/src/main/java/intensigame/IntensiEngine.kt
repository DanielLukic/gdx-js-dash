package intensigame

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.assets.AssetManager
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import intensigame.audio.GdxAudioSystem
import intensigame.log.logError
import intensigame.log.logInfo
import intensigame.log.logVerbose
import intensigame.util.ByteArrayLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.RenderingScope
import java.io.FileInputStream
import java.io.FileOutputStream

@Suppress("MemberVisibilityCanBePrivate")
abstract class IntensiEngine(val screenWidth: Int, val screenHeight: Int)
    : Game(), CoroutineScope by RenderingScope() {

    private val kryo = Kryo().apply {
        isRegistrationRequired = false
    }

    val assetManager = AssetManager().also {
        it.setLoader(ByteArray::class.java, ByteArrayLoader())
    }

    val audioSystem = GdxAudioSystem(assetManager)
    val resources by lazy { IntensiResources(this) }

    protected var paused = false; private set

    private val keyReactions = KeyReactions()

    fun whenKeyDown(keyCode: Int, onKeyDown: (Int) -> Any?) = keyReactions.whenKeyDown(keyCode, onKeyDown)
    fun whenKeyDown(keyCodes: List<Int>, onKeyDown: (Int) -> Any?) = keyReactions.whenKeyDown(keyCodes, onKeyDown)

    fun onKeyDown(keyCode: Int): Boolean {
        val screen = screen as? IntensiScreen
        val handled = screen?.onKeyDown(keyCode)
        if (handled != null && handled == true) return true
        return keyReactions.onKeyDown(keyCode)
    }

    fun onKeyUp(keyCode: Int): Boolean {
        val screen = screen as? IntensiScreen
        val handled = screen?.onKeyUp(keyCode)
        if (handled != null && handled == true) return true
        return keyReactions.onKeyUp(keyCode)
    }

    override fun create() {

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun keyDown(keycode: Int): Boolean = onKeyDown(keycode)
            override fun keyUp(keycode: Int): Boolean = onKeyUp(keycode)
        }

        if (dev) loadState()

        Runtime.getRuntime().addShutdownHook(Thread {
            logInfo { "SHUTDOWN" }
            if (dev) saveState()
        })

        KtxAsync.initiate()
        KtxAsync.launch {
            run()
            logInfo { "TOP LEVEL DONE" }
            Gdx.app.exit()
        }
    }

    override fun resume() {
        super.resume()
        paused = false
        audioSystem.resumePlayback()
    }

    override fun render() {
        if (paused) return
        super.render()
    }

    override fun pause() {
        audioSystem.haltPlayback()
        paused = true
        super.pause()
    }

    fun setScreen(it: IntensiScreen) {
        it.init(this)
        super.setScreen(it)
    }

    protected fun loadState() {
        runCatching {
            logInfo { "loadState" }
            val stream = FileInputStream("state.kryo")
            val input = Input(stream)
            kryo.readClassAndObject(input).also { input.close() }
        }.onSuccess {
            state.setTo(it as IntensiState)
            logVerbose { state }
        }.onFailure {
            logError { it }
        }
    }

    protected fun saveState() {
        runCatching {
            logInfo { "saveState" }
            val output = Output(FileOutputStream("state.kryo"))
            kryo.writeClassAndObject(output, state).also {
                output.flush()
                output.close()
            }
        }.onSuccess {
            logVerbose { state }
        }.onFailure {
            logError { it }
        }
    }

    protected val state = IntensiState("engine")

    protected var dev = false

    protected abstract suspend fun run()
}
