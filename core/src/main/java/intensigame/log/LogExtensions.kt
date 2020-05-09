package intensigame.log

import java.util.concurrent.CancellationException

private fun rescue(block: () -> Unit) = try {
    block()
}
catch (it: Throwable) {
    if (it is CancellationException) throw it
    Log.error(it)
}

fun <T> loggedDefault(default: T, message: String, vararg arguments: Any): T {
    rescue { Log.error(message, arguments) }
    return default
}

// Extensions which evaluate log level before evaluating the message

fun logStackTrace() {
    if (Log.level < Log.traceLevel) return
    Log.logStackTrace()
}

fun logTrace() {
    if (Log.level < Log.traceLevel) return
    Log.trace()
}

fun logVerbose(message: () -> Any?) {
    if (Log.level < Log.Level.VERBOSE) return
    rescue { Log.verbose(message().toString()) }
}

fun logInfo(message: () -> Any?) {
    if (Log.level < Log.Level.INFO) return
    rescue { Log.info(message().toString()) }
}

fun logWarn(message: () -> Any?) {
    if (Log.level < Log.Level.WARN) return
    rescue { Log.warn(message().toString()) }
}

fun logError(failure: Throwable? = null, message: () -> Any? = { "" }) {
    if (Log.level < Log.Level.ERROR) return
    rescue { Log.error(failure, message().toString()) }
}

fun <T> T.logVerbose(): T {
    if (Log.level >= Log.Level.VERBOSE) Log.verbose(this.toString())
    return this
}

fun <T> T.logInfo(): T {
    if (Log.level >= Log.Level.INFO) Log.info(this.toString())
    return this
}
