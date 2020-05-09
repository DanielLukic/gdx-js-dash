package intensigame.log

interface LogSink {
    fun log(level: Log.Level, tag: String, message: String?, throwable: Throwable?)
    fun log(level: Log.Level, tag: String, conditionalMessage: Pair<String?, Log.Level?>, throwable: Throwable?) {
        val dynamicLevel = conditionalMessage.second
        if (dynamicLevel != null && level > dynamicLevel) return
        log(conditionalMessage.second ?: level, tag, conditionalMessage.first, throwable)
    }
}
