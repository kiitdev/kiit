package kiit.telemetry

import kiit.common.Identity
import kiit.codes.Failed
import kiit.codes.Passed
import kiit.codes.Status
import java.util.concurrent.atomic.AtomicLong


/**
 * Used for diagnostics / metrics to track and count various @see[kiit.codes.Status]
 * representing successes / failures of some operation identified by @param id.
 * This serves to track the following:
 *
 * 1. total requests  ( processed )
 * 2. total succeeded  e.g. succeeded, pending or information
 * 3. total excluded   e.g. ineligible request, skipped
 * 4. total restricted e.g. security/auth failure
 * 5. total invalid    e.g. invalid / bad request
 * 6. total rejected   e.g. expected errors, rule violations
 * 7. total unserved   e.g. unexpected errors, unavailable
 */
class Counters(val id: Identity = Identity.empty,
               val tags:List<Tag> = listOf(),
               custom:List<String>? = null) {


    private val counters = build(custom)
    val processed = Counter()
    val succeeded = Counter()
    val excluded   = Counter()
    val restricted = Counter()
    val invalid    = Counter()
    val rejected   = Counter()
    val unserved   = Counter()

    /**
     * Track status in the counters
     */
    fun increment(status:Status) {
        processed.inc()
        when(status) {
            is Passed.Excluded   -> excluded.inc()
            is Passed            -> succeeded.inc()
            is Failed.Restricted -> restricted.inc()
            is Failed.Invalid    -> invalid.inc()
            is Failed.Rejected   -> rejected.inc()
            is Failed.Unserved   -> unserved.inc()
        }
    }

    fun get(name:String):Long = getInternal(name)?.get() ?: 0L
    fun inc(name:String):Long = getInternal(name)?.inc() ?: 0L
    fun dec(name:String):Long = getInternal(name)?.dec() ?: 0L


    /**
     * Reset all counterst to 0
     */
    fun reset() {
        processed.set(0L)
        succeeded.set(0L)
        excluded.set(0L)
        restricted.set(0L)
        invalid.set(0L)
        rejected.set(0L)
        unserved.set(0L)
        counters.keys.map { counters[it]?.set(0L) }
    }


    /**
     * Gets the counter for a specific name
     */
    private fun getInternal(name:String): Counter? {
        return if(counters.containsKey(name)) {
            counters[name]
        } else {
            null
        }
    }


    companion object {
        val PROCESSED  = "Processed"
        val SUCCEEDED  = "Succeeded"
        val EXCLUDED   = "Excluded"
        val RESTRICTED = "Restricted"
        val INVALID    = "Invalid"
        val REJECTED   = "Rejected"
        val UNSERVED   = "Unserved"

        /**
         * Track status in the counters
         */
        fun count(counters: Counters, status:Status) {
            counters.increment(status)
        }


        /**
         * Tracks the status of all the results using the counters
         */
        fun <S,F> count(counters: Counters, results:List<kiit.result.Result<S, F>>): Unit {
            // 1. Track processed, failed, etc
            results.forEach { result -> counters.increment(result.status) }
        }


        /**
         * Count the number of processed items
         * @param counter   :
         * @param limit
         * @param operation
         */
        fun countOrReset(counter:AtomicLong, enabled:Boolean, limit:Long, size:Int, operation:() -> Unit) {
            val current = counter.get()
            if (current >= limit) {
                if(enabled) {
                    operation()
                }
                counter.set(0L)
            }
            else {
                counter.set(current + size)
            }
        }


        fun build(custom:List<String>? = null):Map<String, Counter> {
            return custom?.let { names -> names.map { it to Counter(listOf()) }.toMap() } ?: mapOf()
        }
    }
}
