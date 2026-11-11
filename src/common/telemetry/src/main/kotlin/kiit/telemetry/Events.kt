package kiit.telemetry

import kiit.utils.events.Event
import kiit.result.*
import kiit.codes.*

/**
 * Handles the various success / failures statuses represented in the @see[kiit.codes.Status]
 */
open class Events<TRequest, TResponse, TFailure>(
    override val tags: List<Tag> = listOf(),
    val successConverter:((Any, TRequest, TResponse) -> Event)? = null,
    val failureConverter:((Any, TRequest, Failure<TFailure> ) -> Event)? = null,
    val eventHandler:((Event) -> Unit)? = null) : Tagged {

    open fun requested(sender: Any, request: TRequest) {
    }


    open fun succeeded(sender: Any, request: TRequest, response:TResponse) {
        handleSuccess(sender, request, response)
    }


    open fun excluded(sender: Any, request: TRequest, response:TResponse) {
        handleSuccess(sender, request, response)
    }


    open fun restricted(sender: Any, request: TRequest, error:Failure<TFailure>) {
        handleFailure(sender, request, error)
    }


    open fun invalid(sender: Any, request: TRequest, error:Failure<TFailure>) {
        handleFailure(sender, request, error)
    }


    open fun rejected(sender: Any, request: TRequest, error:Failure<TFailure>) {
        handleFailure(sender, request, error)
    }


    open fun unserved(sender: Any, request: TRequest, error:Failure<TFailure>) {
        handleFailure(sender, request, error)
    }


    open fun custom(sender: Any, name:String, request: TRequest, error:Failure<TFailure>) {}


    fun restricted(sender:Any, request: TRequest, error:TFailure)  = restricted(sender, request, Failure(error, Failed.Restricted.DEFAULT))
    fun invalid   (sender:Any, request: TRequest, error:TFailure)  = invalid   (sender, request, Failure(error, Failed.Invalid.DEFAULT))
    fun rejected  (sender:Any, request: TRequest, error:TFailure)  = rejected  (sender, request, Failure(error, Failed.Rejected.DEFAULT))
    fun unserved  (sender:Any, request: TRequest, error:TFailure)  = unserved  (sender, request, Failure(error, Failed.Unserved.DEFAULT))


    protected fun handleSuccess(sender:Any, request: TRequest, response:TResponse){
        successConverter?.let { converter ->
            eventHandler?.let { handler ->
                val event = converter(sender, request, response)
                handler(event)
            }
        }
    }

    protected fun handleFailure(sender:Any, request: TRequest, error:Failure<TFailure>){
        failureConverter?.let { converter ->
            eventHandler?.let { handler ->
                val event = converter(sender, request, error)
                handler(event)
            }
        }
    }


    open fun handle(sender: Any, request: TRequest, result:Result<TResponse, TFailure>){
        when(result) {
            is Success -> {
                when(result.status){
                    is Passed.Excluded  -> this.excluded(sender, request, result.value)
                    else                -> this.succeeded(sender, request, result.value)
                }
            }
            is Failure -> {
                when (result.status) {
                    is Failed.Restricted -> this.restricted(sender, request, result)
                    is Failed.Invalid    -> this.invalid   (sender, request, result)
                    is Failed.Rejected   -> this.rejected  (sender, request, result)
                    is Failed.Unserved   -> this.unserved  (sender, request, result)
                }
            }
        }
    }
}
