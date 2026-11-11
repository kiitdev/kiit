package kiit.policy.policies

import kiit.common.log.Logger
import kiit.policy.Policy
import kiit.codes.Failed
import kiit.result.Outcome
import kiit.result.Outcomes
import java.util.concurrent.atomic.AtomicLong

/**
 * Policy to call the operation in steps ( e.g. every 2, 3 times )
 * @param I : Input type
 * @param O : Output type
 */
class Step<I, O>(val steps: Long, val logger: Logger? = null) : Policy<I, O> {
    private val count = AtomicLong(0L)

    override suspend fun run(i: I, operation: suspend(I) -> Outcome<O>): Outcome<O> {
        val curr = count.incrementAndGet()
        logger?.info("EVERY: curr = $curr")
        val result = when(curr >= steps) {
            true -> {
                count.set(0L)
                val res = operation(i)
                res
            }
            false -> {
                Outcomes.rejected("Skipping periodic execution", Failed.Rejected.PRECONDITION_FAILED)
            }
        }
        return result
    }
}
