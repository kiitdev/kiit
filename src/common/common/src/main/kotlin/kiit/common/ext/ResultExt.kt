package kiit.common.ext

import kiit.codes.CodesToHttp
import kiit.codes.Err
import kiit.codes.StatusException
import kiit.result.Failure
import kiit.result.Result

private val httpCodes = CodesToHttp()

/**
 * De-structured into key/value pairs for structured logging
 */
fun Result<*, *>.structured(): List<Pair<String, Any?>> {
    return listOf(
            "success" to this.success,
            "code" to httpCodes.toCode(this.status),
            "desc" to this.status.title
    )
}


/**
 * Builds a list of key/value pairs from the Failure data and possible @see[kiit.codes.Err]
 * used for structured logging
 */
fun Failure<*>.structured(): List<Pair<String, Any?>> {
    val err = this.error
    return when (err) {
        null -> listOf()
        is Err -> err.strings().mapIndexed { ndx, e -> "Error $ndx" to e }
        is StatusException -> listOf("message" to err.message)
        else -> listOf("message" to err.toString())
    }
}
