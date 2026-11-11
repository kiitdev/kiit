package kiit.policy.hooks

import kiit.common.Ignore
import kiit.result.Outcome

/**
 * A "Filter" based middle-ware that either allows/disallows an API call to proceed
 *
 * NOTE: This is applied before conversion of request parameters( e.g. JSON )
 * to action parameters.
 */
interface Filter<TReq> : Middleware {

    /**
     * Middleware to filter a request
     * @param req : The request ( e.g. call )
     * @sample: kiit.result.Success("")
     */
    @Ignore
    suspend fun filter(req: TReq): Outcome<TReq>
}
