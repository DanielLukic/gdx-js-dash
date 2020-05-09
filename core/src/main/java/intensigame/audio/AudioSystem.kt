package intensigame.audio

import intensigame.log.Log
import intensigame.util.DynamicArray

abstract class AudioSystem protected constructor() {

    fun enableMusicAndSound() {
        isMusicEnabled = true
        isSoundEnabled = true
        enableResources(myMusicResources)
        enableResources(mySoundResources)
    }

    private fun enableResources(aResources: DynamicArray) {
        for (idx in 0 until aResources.size) {
            val resource = aResources[idx] as AudioResourceEx
            resource.enable()
        }
    }

    fun enableMusicOnly() {
        isMusicEnabled = true
        isSoundEnabled = false
        enableResources(myMusicResources)
        disableResources(mySoundResources)
    }

    private fun disableResources(aResources: DynamicArray) {
        for (idx in 0 until aResources.size) {
            val resource = aResources[idx] as AudioResourceEx
            resource.disable()
        }
    }

    fun enableSoundOnly() {
        isMusicEnabled = false
        isSoundEnabled = true
        disableResources(myMusicResources)
        enableResources(mySoundResources)
    }

    fun disable() {
        isMusicEnabled = false
        isSoundEnabled = false
        disableResources(myMusicResources)
        disableResources(mySoundResources)
    }

    fun resumePlayback() {
        if (isMusicEnabled) resumeResources(myMusicResources)
        if (isSoundEnabled) resumeResources(mySoundResources)
    }

    private fun resumeResources(aResources: DynamicArray) {
        for (idx in 0 until aResources.size) {
            val resource = aResources[idx] as AudioResourceEx
            resource.enable()
        }
    }

    fun haltPlayback() {
        Log.info("HALT PLAYBACK")
        haltResources(myMusicResources)
        haltResources(mySoundResources)
    }

    private fun haltResources(aResources: DynamicArray) {
        for (idx in 0 until aResources.size) {
            val resource = aResources[idx] as AudioResourceEx
            resource.disable()
        }
    }

    fun setMasterVolume(aVolumeInPercent: Int) {
        myMusicVolume = aVolumeInPercent
        mySoundVolume = aVolumeInPercent
        setVolume(myMusicResources, aVolumeInPercent)
        setVolume(mySoundResources, aVolumeInPercent)
    }

    private fun setVolume(aResources: DynamicArray, aVolumeInPercent: Int) {
        for (idx in 0 until aResources.size) {
            val resource = aResources[idx] as AudioResource
            resource.setVolume(aVolumeInPercent)
        }
    }

    fun setMasterMusicVolume(aVolumeInPercent: Int) {
        myMusicVolume = aVolumeInPercent
        setVolume(myMusicResources, aVolumeInPercent)
    }

    fun setMasterSoundVolume(aVolumeInPercent: Int) {
        mySoundVolume = aVolumeInPercent
        setVolume(mySoundResources, aVolumeInPercent)
    }

    fun loadMusic(aMusicName: String?): AudioResource {
        return try {
            //#if DEBUG
            Log.verbose("loading music resource {}", aMusicName)
            //#endif
            val resource = loadMusicUnsafe(aMusicName)
            if (!isMusicEnabled) resource.disable()
            resource.setVolume(myMusicVolume)
            resource.setLoopForever()
            registerMusicResource(resource)
            resource
        }
        catch (e: Exception) {
            Log.error("failed loading music resource {}: {}", aMusicName, e.toString(), e)
            SilentAudioResource()
        }
    }

    fun loadSound(aSoundName: String?): AudioResource {
        return try {
            //#if DEBUG
            Log.verbose("loading sound resource {}", aSoundName)
            //#endif
            val resource = loadSoundUnsafe(aSoundName)
            if (!isSoundEnabled) resource.disable()
            resource.setVolume(mySoundVolume)
            registerSoundResource(resource)
            resource
        }
        catch (e: Exception) {
            Log.error("failed loading sound resource $aSoundName: $e")
            SilentAudioResource()
        }
    }

    @Throws(Exception::class)
    protected abstract fun loadMusicUnsafe(aMusicName: String?): AudioResourceEx

    @Throws(Exception::class)
    protected abstract fun loadSoundUnsafe(aSoundName: String?): AudioResourceEx

    // Implementation
    private fun registerMusicResource(aMusicResource: AudioResourceEx) {
        myMusicResources.add(aMusicResource)
    }

    private fun registerSoundResource(aMusicResource: AudioResourceEx) {
        mySoundResources.add(aMusicResource)
    }

    private var myMusicVolume = 50
    private var mySoundVolume = 75
    private var isSoundEnabled = true
    private var isMusicEnabled = true
    private val myMusicResources = DynamicArray()
    private val mySoundResources = DynamicArray()
}
