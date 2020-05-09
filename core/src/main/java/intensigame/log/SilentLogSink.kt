package intensigame.log

class SilentLogSink : LogSink {

    @Synchronized
    override fun log(level: Log.Level, tag: String, message: String?, throwable: Throwable?) {
        // nop
    }
}
