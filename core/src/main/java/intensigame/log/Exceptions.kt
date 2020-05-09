package intensigame.log

val Throwable?.rootCause: Throwable?
    get() {
        if (this == null) return null
        val causeOrNull = cause ?: return this
        return causeOrNull.rootCause
    }
