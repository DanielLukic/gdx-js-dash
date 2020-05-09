package intensigame.util

import intensigame.log.logError
import intensigame.log.logWarn
import kotlinx.coroutines.channels.Channel

fun <T> Channel<T>.offerOrLog(it: T) {
    runCatching { trySend(it) }
        .onSuccess { result -> if (!result.isSuccess) logWarn { "failed to offer $it" } }
        .onFailure { throwable -> logError { "failed to offer $it: $throwable" } }
}
