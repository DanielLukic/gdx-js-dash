package intensigame.mod

import intensigame.audio.AudioResourceEx
import intensigame.log.logVerbose

internal class ModPlayerResource(data: ByteArray) : AudioResourceEx {

    private val myPlayer = ModPlayer.load(data)

    private var myCurrentVolume = 50
    private var myPlayingFlag = false
    private var myEnabledFlag = true

    // From AudioResourceEx

    override fun enable() {
        if (myEnabledFlag) return
        myEnabledFlag = true
        if (myPlayingFlag) myPlayer.start()
    }

    override fun disable() {
        if (!myEnabledFlag) return
        myPlayingFlag = myPlayer.isPlaying
        myEnabledFlag = false
        if (myPlayer.isPlaying) myPlayer.pause()
    }

    // From AudioResource

    override fun setLoopForever() {
        myPlayer.setLooping(true)
    }

    override fun setVolume(aVolumeInPercent: Int) {
        if (myCurrentVolume == aVolumeInPercent) return
        logVerbose { "setVolume $myCurrentVolume => $aVolumeInPercent" }
        myCurrentVolume = aVolumeInPercent
        myPlayer.setVolume(aVolumeInPercent)
    }

    override fun mute() {
        myPlayer.setVolume(0)
    }

    override fun unmute() {
        setVolume(myCurrentVolume)
    }

    override fun start() {
        if (myEnabledFlag) myPlayer.start() else triggerPlayAfterEnable()
    }

    override fun stop() {
        if (myPlayer.isPlaying) myPlayer.stop()
    }

    override fun pause() {
        if (myPlayer.isPlaying) myPlayer.pause()
    }

    override fun resume() {
        if (myEnabledFlag) myPlayer.start() else triggerPlayAfterEnable()
    }

    // Implementation

    private fun triggerPlayAfterEnable() {
        myPlayingFlag = true
    }
}
