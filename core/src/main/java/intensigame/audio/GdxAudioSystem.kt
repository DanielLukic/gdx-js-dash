package intensigame.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import intensigame.log.logVerbose
import intensigame.mod.ModPlayerResource

class GdxAudioSystem(
    private val assetManager: AssetManager
) : AudioSystem() {

    override fun loadMusicUnsafe(aMusicName: String?): AudioResourceEx {
        val data = assetManager.get<ByteArray>(aMusicName)
        return ModPlayerResource(data)
    }

    override fun loadSoundUnsafe(aSoundName: String?): AudioResourceEx {
        val sound = Gdx.audio.newSound(Gdx.files.internal(aSoundName))
        return object : AudioResourceEx {

            private var enabled = true
            private var looping = false
            private var volumeInPercent = 75

            override fun enable() {
                enabled = true
            }

            override fun disable() {
                enabled = false
            }

            override fun setLoopForever() {
                looping = true
            }

            override fun setVolume(aVolumeInPercent: Int) {
                if (volumeInPercent == aVolumeInPercent) return
                logVerbose { "setVolume $volumeInPercent => $aVolumeInPercent" }
                volumeInPercent = aVolumeInPercent
            }

            override fun mute() = Unit
            override fun unmute() = Unit

            override fun start() {
                if (looping)
                    sound.loop(volumeInPercent / 100f)
                else
                    sound.play(volumeInPercent / 100f)
            }

            override fun stop() = sound.stop()
            override fun resume() = sound.resume()
            override fun pause() = sound.pause()
        }
    }
}
