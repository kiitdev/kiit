/**
 <kiit_header>
url: www.kiit.dev
git: www.github.com/slatekit/kiit
org: www.codehelix.co
author: Kishore Reddy
copyright: 2016 CodeHelix Solutions Inc.
license: refer to website and/or github
about: A Kotlin utility library, tool-kit and server backend.

 </kiit_header>
 */
package kiit.telemetry

import kiit.common.Identity
import kiit.result.Failure
import java.util.concurrent.atomic.AtomicReference

/**
 * Used to track the last request  and results of calling some function/target identified by @param id
 * NOTE: This is particularly helpful for functions/operations such as scheduled tasks, syncs, one-off commands.
 * This serves to track the following:
 *
 * 1. total requests  ( processed )
 * 2. total succeeded  e.g. passed
 * 3. total excluded   e.g. ineligible request
 * 4. total restricted e.g. security/auth failure
 * 5. total invalid    e.g. invalid / bad request
 * 6. total rejected   e.g. failed validation
 * 7. total unserved   e.g. unexpected error
 *
 * @param TRequest : Type of the request  / input to the operation
 * @param TResponse: Type of the response / output of the operation
 * @param TFailure : Type of the error of the operation
 */
open class Lasts<TRequest, TResponse, TFailure>(val id: Identity,
                                                tags:List<Tag> = listOf(),
                                                val custom:List<String>? = null) : Tagged, Events<TRequest, TResponse, TFailure>(tags) {

    // Last values
    private val _lastRequest     = AtomicReference<TRequest>(null)
    private val _lastPending     = AtomicReference<TRequest>(null)
    private val _lastSuccess     = AtomicReference<Triple<Any, TRequest, TResponse>>(null)
    private val _lastExcluded    = AtomicReference<Triple<Any, TRequest, TResponse>>(null)
    private val _lastRestricted  = AtomicReference<Triple<Any, TRequest ,Failure<TFailure>>>(null)
    private val _lastInvalid     = AtomicReference<Triple<Any, TRequest, Failure<TFailure>>>(null)
    private val _lastRejected    = AtomicReference<Triple<Any, TRequest, Failure<TFailure>>>(null)
    private val _lastUnserved    = AtomicReference<Triple<Any, TRequest, Failure<TFailure>>>(null)
    private val _customLasts = custom?.let {  c -> c.map {  it to AtomicReference<Triple<Any, TRequest, Failure<TFailure>>>() }.toMap() } ?: mapOf()

    override fun requested (sender:Any, request: TRequest)                           = _lastRequest.set(request)
    override fun succeeded (sender:Any, request: TRequest, res: TResponse)           = _lastSuccess.set(Triple(sender, request, res))
    override fun excluded  (sender:Any, request: TRequest, res: TResponse)           = _lastExcluded.set(Triple(sender, request, res))
    override fun restricted(sender:Any, request: TRequest, error:Failure<TFailure>)  = _lastRestricted.set(Triple(sender, request, error))
    override fun invalid   (sender:Any, request: TRequest, error:Failure<TFailure>)  = _lastInvalid.set(Triple(sender, request, error))
    override fun rejected  (sender:Any, request: TRequest, error:Failure<TFailure>)  = _lastRejected.set(Triple(sender, request, error))
    override fun unserved  (sender:Any, request: TRequest, error:Failure<TFailure>)  = _lastUnserved.set(Triple(sender, request, error))
    override fun custom    (sender:Any, name:String, req: TRequest, error:Failure<TFailure>) {
        getCustom(name)?.let { c -> c.set(Triple(sender, req, error)) }
    }

    fun lastProcessed ():TRequest?                  = _lastRequest.get()
    fun lastSuccess   ():Triple<Any, TRequest, TResponse>? = _lastSuccess.get()
    fun lastExcluded  ():Triple<Any, TRequest, TResponse>? = _lastExcluded.get()
    fun lastInvalid   ():Triple<Any, TRequest, Failure<TFailure>>? = _lastInvalid.get()
    fun lastRestricted():Triple<Any, TRequest, Failure<TFailure>>? = _lastRestricted.get()
    fun lastRejected  ():Triple<Any, TRequest, Failure<TFailure>>? = _lastRejected.get()
    fun lastUnserved  ():Triple<Any, TRequest, Failure<TFailure>>? = _lastUnserved.get()
    fun lastCustom(name:String):Triple<Any, TRequest, Failure<TFailure>>? = getCustom(name)?.get()

    fun clear(){
        _lastRequest.set(null)
        _lastPending.set(null)
        _lastSuccess.set(null)
        _lastExcluded.set(null)
        _lastRestricted.set(null)
        _lastInvalid.set(null)
        _lastRejected.set(null)
        _lastUnserved.set(null)
    }

    private fun getCustom(name:String):AtomicReference<Triple<Any, TRequest , Failure<TFailure>>>? {
        return if(_customLasts.contains(name)) _customLasts[name] else null
    }
}
