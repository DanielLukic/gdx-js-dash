package intensigame.audio

class SilentAudioResource : AudioResourceEx {
    override fun enable() {}
    override fun disable() {}
    override fun setLoopForever() {}
    override fun setVolume(aVolumeInPercent: Int) {}
    override fun mute() {}
    override fun unmute() {}
    override fun start() {}
    override fun stop() {}
    override fun pause() {}
    override fun resume() {}
}
