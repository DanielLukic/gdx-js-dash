package intensigame.graphics

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import intensigame.IntensiResources
import intensigame.log.logVerbose
import intensigame.log.logWarn
import intensigame.util.Disposable

fun IntensiResources.createShader(name: String): Shader {
    val program = loadShader(name)
    if (program.log.isNotBlank()) logWarn { program.log }
    logVerbose { program.attributes.toList() }
    logVerbose { program.uniforms.toList() }
    if (!program.isCompiled) error("shader program $name failed to compile")
    return object : Shader, Disposable {
        override val program = program
        override val parameters = mutableMapOf<String, ShaderProgram.() -> Unit>()
        override fun dispose() = program.dispose()
        override fun toString() = "Shader($name)"
    }
}
