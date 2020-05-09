package intensigame.mod

import com.badlogic.gdx.audio.AudioDevice
import ibxm.IBXM
import intensigame.log.logVerbose

internal class ModPlayerThread(
    private val module: IBXM,
    private val sink: AudioDevice
) : Thread("MuxmPlayerThread") {

    private val lock = Object()
    private val songDuration = module.calculateSongDuration()

    init {
        logVerbose { "latency ${sink.latency}" }
        logVerbose { "buffer ${module.mixBufferLength}" }
        logVerbose { "duration $songDuration" }
    }

    private var volumeInPercent = 50
    private var myPlayPosition = 0
    private var paused = true
    private var stopped = false

    var looping = false

    fun stopPlayback() {
        stopped = true
        synchronized(lock) { lock.notifyAll() }
    }

    fun setVolume(percent: Int) {
        if (volumeInPercent == percent) return
        logVerbose { "setVolume $volumeInPercent -> $percent" }
        volumeInPercent = percent
    }

    fun unpause() {
        paused = false
        synchronized(lock) { lock.notifyAll() }
    }

    fun pause() {
        paused = true
    }

    // From Thread

    override fun run() {
        val mixBuf = IntArray(module.mixBufferLength)
        val outBuf = ShortArray(mixBuf.size)
        while (!stopped) {
            synchronized(lock) { while (paused) lock.wait().also { logVerbose { "paused" } } }
            if (stopped) break
            val count = module.getAudio(mixBuf)
            myPlayPosition += count
            if (myPlayPosition >= songDuration) {
                if (looping)
                    myPlayPosition = 0
                else
                    pause()
            }
            var outIdx = 0
            var mixIdx = 0
            val mixEnd = count * 2
            while (mixIdx < mixEnd) {
                var amp = mixBuf[mixIdx] * volumeInPercent / 100
                if (amp > 32767) amp = 32767
                if (amp < -32768) amp = -32768
                outBuf[outIdx++] = amp.toShort()
                mixIdx++
            }
            sink.writeSamples(outBuf, 0, outIdx)
        }
    }

}
