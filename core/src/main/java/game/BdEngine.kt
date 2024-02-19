package game

import com.badlogic.gdx.Input
import intensigame.IntensiEngine
import intensigame.anyExitKey
import intensigame.log.Log
import kotlinx.coroutines.cancel

internal var DEV_MODE = true

object BdEngine : IntensiEngine(40 * 32, 23 * 32) {

    init {
        Log.tag = "GAME"
        Log.level = Log.Level.VERBOSE
        //Log.scopeLevel["IntensiResources"] = Log.Level.VERBOSE
        Log.scopeLevel["*"] = Log.Level.INFO

        dev = DEV_MODE
    }

    override suspend fun run() {

        whenKeyDown(anyExitKey) { cancel() }
        whenKeyDown(Input.Keys.P) { if (paused) resume() else pause() }
        whenKeyDown(Input.Keys.C) { state.clear() }

        val screen = BdScreen()
        setScreen(screen)
        screen.enact(this).join()
    }

}
