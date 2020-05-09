package intensigame.mod

import com.badlogic.gdx.Gdx
import ibxm.IBXM
import ibxm.Module
import intensigame.log.logVerbose

internal class ModPlayer(
    private var module: IBXM
) {
    companion object {
        private const val DEFAULT_SAMPLE_RATE = 11025

        fun load(data: ByteArray): ModPlayer {
            val module = IBXM(Module(data.inputStream()), DEFAULT_SAMPLE_RATE)
            return ModPlayer(module)
        }
    }

    private val audioDevice = Gdx.audio.newAudioDevice(DEFAULT_SAMPLE_RATE, false)

    private var myRefillThread: ModPlayerThread? = null
    private var looping = false
    private var volumeInPercent = 50

    var isPlaying = false

    fun setLooping(enabled: Boolean) {
        looping = enabled
        myRefillThread?.looping = enabled
    }

    fun setVolume(percent: Int) {
        if (volumeInPercent == percent) return
        logVerbose { "setVolume $volumeInPercent -> $percent" }
        volumeInPercent = percent
        myRefillThread?.setVolume(percent)
    }

    fun start() {
        if (isPlaying) return
        isPlaying = true

        myRefillThread = myRefillThread ?: ModPlayerThread(module, audioDevice).apply {
            isDaemon = true
            setLooping(looping)
            setVolume(volumeInPercent)
            start()
        }

        myRefillThread?.unpause()
    }

    fun pause() {
        myRefillThread?.pause()
        isPlaying = false
    }

    fun stop() {
        myRefillThread?.stopPlayback()
        myRefillThread = null
        isPlaying = false
    }
}
