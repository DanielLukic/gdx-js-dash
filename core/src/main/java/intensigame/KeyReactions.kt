package intensigame

import com.badlogic.gdx.Input
import java.util.*

val Int.isLeft get() = this == Input.Keys.LEFT || this == Input.Keys.DPAD_LEFT
val Int.isRight get() = this == Input.Keys.RIGHT || this == Input.Keys.DPAD_RIGHT
val Int.isDown get() = this == Input.Keys.DOWN || this == Input.Keys.DPAD_DOWN
val Int.isUp get() = this == Input.Keys.UP || this == Input.Keys.DPAD_UP

val directional = listOf(
    Input.Keys.DPAD_DOWN,
    Input.Keys.DPAD_LEFT,
    Input.Keys.DPAD_RIGHT,
    Input.Keys.DPAD_UP,

    Input.Keys.DOWN,
    Input.Keys.LEFT,
    Input.Keys.RIGHT,
    Input.Keys.UP
)

val anyExitKey = listOf(
    Input.Keys.ESCAPE,
    Input.Keys.Q
)

val anyStartOrSelectKey = listOf(
    Input.Keys.BUTTON_A,
    Input.Keys.BUTTON_CIRCLE,
    Input.Keys.BUTTON_SELECT,
    Input.Keys.BUTTON_START,
    Input.Keys.ENTER,
    Input.Keys.SPACE
)

internal class KeyReactions {

    private val downReactions = LinkedList<(Int) -> Any?>()
    private val upReactions = LinkedList<(Int) -> Any?>()

    fun onKeyDown(keyCode: Int): Boolean = downReactions.firstOrNull {
        val handled = it(keyCode)
        handled != null && handled != false
    } != null

    fun onKeyUp(keyCode: Int): Boolean = upReactions.firstOrNull {
        val handled = it(keyCode)
        handled != null && handled != false
    } != null

    fun whenKeyDown(keyCode: Int, onKey: (Int) -> Any?) = downReactions.add(0) {
        if (it == keyCode) onKey(it) else null
    }

    fun whenKeyDown(keyCodes: List<Int>, onKey: (Int) -> Any?) = downReactions.add(0) {
        if (it in keyCodes) onKey(it) else null
    }

    fun whenKeyUp(keyCode: Int, onKey: (Int) -> Any?) = upReactions.add(0) {
        if (it == keyCode) onKey(it) else null
    }

}
