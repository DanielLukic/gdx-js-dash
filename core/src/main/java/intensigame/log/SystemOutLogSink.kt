package intensigame.log

class SystemOutLogSink : LogSink {

    @Synchronized
    override fun log(level: Log.Level, tag: String, message: String?, throwable: Throwable?) {
        System.out.flush()

        val stream = if (level == Log.Level.ERROR) System.err else System.out
        stream.println(String.format("%s [%s] %s", level.tag, tag, message))
        throwable?.printStackTrace(System.err)
        stream.flush()
    }
}
