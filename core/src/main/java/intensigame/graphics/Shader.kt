package intensigame.graphics

import com.badlogic.gdx.graphics.glutils.ShaderProgram

interface Shader {
    val program: ShaderProgram
    val parameters: MutableMap<String, ShaderProgram.() -> Unit>

    operator fun set(key: String, value: Float) {
        parameters[key] = { setUniformf(key, value) }
    }

    fun updateBeforeUse() {
        parameters.values.forEach { it(program) }
    }
}
