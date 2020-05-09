package intensigame.util

import intensigame.log.Log.verbose
import java.util.*

class Timing {

    fun doStart(aTag: Any?) {
        //#if TIMING
        val currentEntry = myEntryStack.last() as TimingEntry
        val childEntry = currentEntry.getOrCreateChild(aTag)
        childEntry.start()
        myEntryStack.add(childEntry)
        //#endif
    }

    fun doEnd(aTag: Any) {
        //#if TIMING
        val currentEntry = myEntryStack.removeLast() as TimingEntry
        currentEntry.end()
        if (currentEntry.tag == aTag) return
        verbose("Timing::doEnd {} != {}", currentEntry.tag, aTag)
        verbose("Timing::myEntryStack {}", myEntryStack)

        //#if FALSE
        System.exit(10)
        throw RuntimeException()
        //#endif
    }

    fun doDumpInto(aBuffer: StringBuffer?) {
        //#if TIMING
        myRootEntry.dumpInto(aBuffer, " ", 1)
        //#endif
    }

    private val myEntryStack = DynamicArray()
    private val myRootEntry = TimingEntry("ROOT")

    companion object {
        fun reset() {
            //#if TIMING
            theGlobalRootEntry.reset()
            theTimingsByThread.clear()
            //#endif
        }

        fun start(aTag: Any?) {
            //#if TIMING
            synchronized(theGlobalRootEntry) { theGlobalRootEntry.getOrCreateChild(aTag).start() }
            timingByThread.doStart(aTag)
            //#endif
        }

        fun end(aTag: Any) {
            //#if TIMING
            timingByThread.doEnd(aTag)
            synchronized(theGlobalRootEntry) { theGlobalRootEntry.getOrCreateChild(aTag).end() }
            //#endif
        }

        fun dumpInto(aBuffer: StringBuffer) {
            //#if TIMING
            val threads = theTimingsByThread.keys()
            while (threads.hasMoreElements()) {
                val thread = threads.nextElement() as Thread
                val timing = theTimingsByThread[thread] ?: continue
                val name = thread.name
                dumpTimingInto(aBuffer, timing.myRootEntry, name)
            }
            dumpTimingInto(aBuffer, theGlobalRootEntry, "GLOBAL")
            //#endif
        }

        // Package Interface
        const val INDENT_OFFSET = 60
        fun insertFixed(aBuffer: StringBuffer) {
            //#if TIMING
            aBuffer.append(INSERT_STRING)
            //#endif
        }

        fun insertFixed(aBuffer: StringBuffer, aString: String) {
            //#if TIMING
            val string = if (aString.length <= INSERT_WIDTH) aString else aString.substring(0, INSERT_WIDTH)
            aBuffer.append(INSERT_STRING.substring(0, INSERT_WIDTH - string.length))
            aBuffer.append(string)
            //#endif
        }

        @kotlin.jvm.JvmStatic
        fun insertFixed(aBuffer: StringBuffer, aValue: Long) {
            //#if TIMING
            insertFixed(aBuffer, java.lang.Long.toString(aValue))
            //#endif
        }

        // Implementation
        //#if TIMING
        private val timingByThread: Timing
            get() {
                val thread = Thread.currentThread()
                val timing = theTimingsByThread[thread]
                if (timing != null) return timing
                val newTiming = Timing()
                theTimingsByThread[thread] = newTiming
                return newTiming
            }

        private fun dumpTimingInto(aBuffer: StringBuffer, aTimingEntry: TimingEntry, aName: String) {
            aBuffer.append("\n")
            aBuffer.append("Timing(")
            aBuffer.append(aName)
            aBuffer.append("):\n")
            for (idx in 0 until INDENT_OFFSET) aBuffer.append(' ')
            insertFixed(aBuffer, "cnt")
            insertFixed(aBuffer, "acc")
            insertFixed(aBuffer, "min")
            insertFixed(aBuffer, "max")
            insertFixed(aBuffer, "avg")
            insertFixed(aBuffer, "(acc)")
            insertFixed(aBuffer, "(avg)")
            aBuffer.append("\n")
            aTimingEntry.dumpInto(aBuffer, " ", 1)
        }

        private const val ID_GLOBAL = "GLOBAL"
        private val theTimingsByThread = Hashtable<Thread, Timing>()
        private val theGlobalRootEntry = TimingEntry(ID_GLOBAL)
        private const val INSERT_STRING = "        "
        private const val INSERT_WIDTH = INSERT_STRING.length //#endif
    }

    init {
        //#if TIMING
        myEntryStack.add(myRootEntry)
        //#endif
    }
}
