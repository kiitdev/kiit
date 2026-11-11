package kiit.apis.rules

import kiit.apis.ApiRequest
import kiit.result.Outcome

interface Rule {
    fun validate(req: ApiRequest): Outcome<Boolean>
}
