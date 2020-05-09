package intensigame.audio

interface AudioResource {
    fun setLoopForever()
    fun setVolume(aVolumeInPercent: Int)
    fun mute()
    fun unmute()
    fun start()
    fun stop()
    fun resume()
    fun pause()
}
