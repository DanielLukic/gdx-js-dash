package intensigame.log

import java.util.*

class ChainedLogSink : LogSink {

    private val logSinks = ArrayList<LogSink>()

    fun add(logSink: LogSink): ChainedLogSink {
        logSinks.add(logSink)
        return this
    }

    override fun log(level: Log.Level, tag: String, message: String?, throwable: Throwable?) {
        logSinks.forEach { it.log(level, tag, message, throwable) }
    }
}
