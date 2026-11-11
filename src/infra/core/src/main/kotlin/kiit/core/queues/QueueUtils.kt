package kiit.core.queues

import kiit.codes.Failed
import kiit.result.Failure
import kiit.result.Success
import kiit.result.Try

suspend fun <T> completeAll(entries: List<QueueEntry<T>>?, done:suspend (QueueEntry<T>) -> Try<QueueEntry<T>> ): kiit.result.Result<String, List<Pair<QueueEntry<T>, Throwable>>> {
    return entries?.let {
        val results = it.map {
            val result = done(it)
            when(result) {
                is Success -> null
                is Failure -> Pair(it, result.error)
            }
        }
        collect(results)
    } ?: Failure(listOf(), Failed.Invalid.INVALID_VALUE)
}


fun <T> completeAllSync(entries: List<QueueEntry<T>>?, done:(QueueEntry<T>) -> Try<QueueEntry<T>> ): kiit.result.Result<String, List<Pair<QueueEntry<T>, Throwable>>> {
    return entries?.let {
        val results = it.map {
            val result = done(it)
            when(result) {
                is Success -> null
                is Failure -> Pair(it, result.error)
            }
        }
        collect(results)
    } ?: Failure(listOf(), Failed.Invalid.INVALID_VALUE)
}


fun <T> collect(results:List<Pair<QueueEntry<T>, Throwable>?>) : kiit.result.Result<String, List<Pair<QueueEntry<T>, Throwable>>> {
    val failures = results.filterNotNull()
    return when(failures.isEmpty()){
        true  -> Success("Completed")
        false -> Failure(failures)
    }
}