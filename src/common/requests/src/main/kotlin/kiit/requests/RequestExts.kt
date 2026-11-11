package kiit.requests

import kiit.codes.CodesToHttp
import kiit.codes.Err
import kiit.result.Failure
import kiit.result.Result
import kiit.result.Success

private val httpCodes = CodesToHttp()

/**
 * Converts result to Response.
 */
fun <T, E> Result<T, E>.toResponse(): Response<T> {
    return when (this) {
        is Success -> CommonResponse(
            this.success,
            this.status.name,
            this.status.group,
            httpCodes.toCode(this.status),
            null,
            this.value,
            this.status.title,
            null
        )

        is Failure -> {
            val ex: Exception = when (val e = this.error) {
                is Exception -> e
                is Err -> Exception(e.message)
                else -> Exception(e.toString())
            }
            CommonResponse(
                this.success,
                this.status.name,
                this.status.group,
                httpCodes.toCode(this.status),
                null,
                null,
                when (val e = this.error) {
                    is Err -> e.message
                    is Throwable -> e.message ?: this.status.title
                    else -> this.status.title
                },
                ex
            )
        }
    }
}
