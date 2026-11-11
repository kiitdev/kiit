package kiit.http

import kiit.result.Outcome
import kiit.codes.Failed
import kiit.result.Failure
import kiit.result.Success
import kiit.result.Outcomes
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import okhttp3.Response

suspend fun awaitHttp(callback: (HttpRPC.HttpRPCResult) -> Unit): Response {
    return suspendCoroutine { cont ->
        callback(object : HttpRPC.HttpRPCResult {
            override fun onSuccess(result: Response) = cont.resume(result)
            override fun onFailure(e: Exception?) {
                e?.let { cont.resumeWithException(it) }
            }
        })
    }
}

suspend fun awaitHttpTry(callback: (HttpRPC.HttpRPCResult) -> Unit): kiit.result.Result<Response, Exception> {
    return suspendCoroutine { cont ->
        callback(object : HttpRPC.HttpRPCResult {
            override fun onSuccess(result: Response) = cont.resume(Success(result))
            override fun onFailure(e: Exception?) {
                e?.let { cont.resume(Failure(e, Failed.Rejected.DEFAULT)) }
            }
        })
    }
}

suspend fun awaitHttpOutcome(callback: (HttpRPC.HttpRPCResult) -> Unit): Outcome<Response> {
    return suspendCoroutine { cont ->
        callback(object : HttpRPC.HttpRPCResult {
            override fun onSuccess(result: Response) {
                cont.resume(Outcomes.success(result))
            }

            override fun onFailure(e: Exception?) {
                e?.let { cont.resume(Outcomes.rejected(e)) }
            }
        })
    }
}
