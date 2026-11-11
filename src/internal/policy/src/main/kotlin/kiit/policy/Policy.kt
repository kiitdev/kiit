package kiit.policy

import kiit.result.Outcome

/**
 * @param I : Input type
 * @param O : Output type
 */
interface Policy<I, O> {
    suspend fun run(i: I, operation: suspend(I) -> Outcome<O>): Outcome<O>
}
