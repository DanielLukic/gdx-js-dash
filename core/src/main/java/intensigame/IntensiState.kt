package intensigame

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class IntensiState(private val name: String) {

    @Suppress("unused")
    private constructor() : this("")

    val data = mutableMapOf<String, Any>()

    override fun toString(): String = "$name@${hashCode()}$data"

    fun setTo(it: IntensiState) {
        if (name != it.name) error("name mismatch: $name != ${it.name}")
        data.clear()
        data.putAll(it.data)
    }

    fun scope(key: String): IntensiState {
        data[key] as? IntensiState ?: set(key, IntensiState(key))
        return data[key] as IntensiState
    }

    fun clear() {
        data.values.mapNotNull { it as? IntensiState }.forEach { it.clear() }
        data.clear()
    }

    infix fun clear(key: String) {
        data.remove(key)
    }

    operator fun <T : Any?> set(key: String, value: T?) {
        if (value != null) data[key] = value as Any else data.remove(key)
        check(data[key] == value)
    }

    operator fun get(key: String): Any? = data[key]

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> v(key: String, default: T) = object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return data[key] as T? ?: default
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            set(key, value)
        }
    }

    inline fun <reified T : Any> o(key: String, default: T? = null) = object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return data[key] as? T ?: default
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            if (value != null) data[key] = value else data.remove(key)
        }
    }

    fun b(key: String) = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return data[key] as? Boolean ?: false
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            data[key] = value
        }
    }

    fun i(key: String) = object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return data[key] as? Int ?: 0
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            data[key] = value
        }
    }

    fun f(key: String) = object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return data[key] as? Float ?: 0f
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
            data[key] = value
        }
    }

}
