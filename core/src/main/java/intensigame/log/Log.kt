package intensigame.log

import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.system.exitProcess

object Log {

    private val NO_MESSAGE = ""
    private val NOT_FOUND = StackTraceElement(NO_MESSAGE, NO_MESSAGE, NO_MESSAGE, 0)

    var tag = "LOG"
    var level = Level.INFO
    var traceLevel = Level.VERBOSE
    var sink: LogSink = SystemOutLogSink()
    var mode: Mode? = Mode.ENHANCED
    var twoLines = false
    var failHard = false

    val scopeLevel = mutableMapOf<String, Level>()

    private val threadInfo: String
        get() {
            val thread = Thread.currentThread()
            if (thread.name == "main") return ""
            if (thread.name.startsWith("OkHttp ")) return "[OkHttp] "
            return if (thread.threadGroup.name == "main") String.format("[%s] ", thread.name) else String.format("%s ", thread.toString())
        }

    enum class Level(val value: Int, val tag: String?) {
        NONE(0, null),
        ERROR(1, "E"),
        WARN(2, "W"),
        INFO(3, "I"),
        VERBOSE(4, "V"),
    }

    enum class Mode {
        BRIEF,
        ENHANCED,
        FULL
    }

    fun `is`(level: Level): Boolean {
        return level.value <= Log.level.value
    }

    fun verifyThread(name: String) {
        if (name == Thread.currentThread().name) return
        throw IllegalStateException(Thread.currentThread().name + " != " + name)
    }

    fun thread() {
        verbose(Thread.currentThread().toString())
    }

    fun trace() {
        if (level.value < traceLevel.value) return
        val suffix = if (mode == Mode.FULL) "" else "#" + determineCaller().methodName
        sink.log(traceLevel, tag, enhanced("TRACE$suffix"), null)
    }

    fun logStackTrace() {
        val writer = StringWriter()
        val exception = RuntimeException()
        exception.printStackTrace(PrintWriter(writer))
        info(writer.toString().replace("java.lang.RuntimeException", "STACK TRACE"))
    }

    fun fail(message: String, vararg parameters: Any) {
        fail(null, message, *parameters)
    }

    fun fail(optionalThrowable: Throwable?, message: String, vararg parameters: Any) {
        error(optionalThrowable, message, *parameters)
        if (failHard) exitProcess(10)
    }

    fun error(throwable: Throwable) {
        if (level.value < Level.ERROR.value) return
        sink.log(Level.ERROR, tag, enhanced(NO_MESSAGE), throwable)
    }

    fun error(message: String, vararg parameters: Any?) {
        error(null, message, *parameters)
    }

    fun error(throwable: Throwable?, message: String, vararg parameters: Any?) {
        if (level.value < Level.ERROR.value) return
        sink.log(Level.ERROR, tag, enhanced(formatted(message, *parameters)), throwable)
    }

    fun warn(message: String, vararg parameters: Any?) {
        if (level.value < Level.WARN.value) return
        sink.log(Level.WARN, tag, enhanced(formatted(message, *parameters)), null)
    }

    fun info(message: String, vararg parameters: Any?) {
        if (level.value < Level.INFO.value) return
        sink.log(Level.INFO, tag, enhanced(formatted(message, *parameters)), null)
    }

    fun verbose(message: String, vararg parameters: Any?) {
        if (level.value < Level.VERBOSE.value) return
        sink.log(Level.VERBOSE, tag, enhanced(formatted(message, *parameters)), null)
    }

    fun log(level: Level, message: String, vararg parameters: Any) {
        if (Log.level.value < level.value) return
        sink.log(level, tag, enhanced(formatted(message, *parameters)), null)
    }

    private fun enhanced(message: String): Pair<String, Level?> {
        if (mode == Mode.BRIEF || mode == null) return message to null
        val caller = determineCaller()
        val classNameOnly = getClassNameOnly(caller.className)
        val methodName = getMethodName(caller)
        val lineNumber = caller.lineNumber
        val threadInfo = threadInfo
        // Note the . before the opening parenthesis. It is required to make IntelliJ create a clickable link.
        val separator = if (twoLines) "\n" else " "
        val formatted = String.format("%s%s%s%s.(%s.kt:%s)", message, separator, threadInfo, methodName, classNameOnly, lineNumber)
        val level = scopeLevel[classNameOnly] ?: scopeLevel["*"]
        return formatted to level
    }

    private fun getMethodName(caller: StackTraceElement): String {
        if (mode != Mode.FULL) return ""
        val result = caller.methodName
        return if (result.startsWith("lambda\$iterator$")) "" else String.format("[%s] ", result)
    }

    private fun determineCaller(): StackTraceElement {
        for (element in RuntimeException().stackTrace) {
            if (element.className == Log::class.java.name) continue
            if (element.className.contains(".LogExtensions")) continue
            if (element.className.contains(".Logging")) continue
            if (element.methodName.startsWith("log")) continue
            return element
        }
        return NOT_FOUND
    }

    private fun getClassNameOnly(classNameWithPackage: String): String {
        val lastDotPos = classNameWithPackage.lastIndexOf('.')
        return if (lastDotPos == -1) classNameWithPackage else classNameWithPackage.substring(lastDotPos + 1).replaceFirst("\\$.*".toRegex(), "")
    }

    private fun formatted(message: String, vararg parameters: Any?) = try {
        if (parameters.isEmpty()) message else String.format(message, *parameters)
    }
    catch (throwable: Throwable) {
        error(throwable, "failed formatting log message - ignored: %s with %s", message, Arrays.asList(*parameters))
        message
    }
}
