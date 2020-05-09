@file:JvmName("Lwjgl3Launcher")

package desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.glutils.HdpiMode
import game.BdEngine
import intensigame.log.Log
import java.awt.Toolkit

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
      return
    Log.level = Log.Level.INFO
    Log.traceLevel = Log.Level.INFO
    Lwjgl3Application(BdEngine, defaultConfiguration)
}

private val defaultConfiguration = Lwjgl3ApplicationConfiguration().apply {
    setTitle("gdx-test")
    setHdpiMode(HdpiMode.Pixels)
    setDecorated(true)
    setResizable(false)
    setMaximized(false)

    val width = Toolkit.getDefaultToolkit().screenSize.width * 2 / 3
    val height = Toolkit.getDefaultToolkit().screenSize.height * 2 / 3
    setWindowSizeLimits(512, 512, width, height)
    useVsync(false)
    setIdleFPS(10)
    setForegroundFPS(60)

    setWindowedMode(width, height)
    setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
}
