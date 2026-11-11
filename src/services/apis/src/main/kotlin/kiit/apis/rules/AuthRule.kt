package kiit.apis.rules

import kiit.apis.ApiRequest
import kiit.result.Outcome
import kiit.result.Outcomes


object AuthRule : Rule {
    override fun validate(req:ApiRequest): Outcome<Boolean> {
        val target = req.target!!
        val api = target.api
        val noAuth = req.auth == null //|| api.auth.isNullOrEmpty()
        val actionRoles = target.action.roles.orElse(api.roles)
        val isAuthed = actionRoles.isAuthed

        // CASE 1: No auth for action
        return if (noAuth && !isAuthed) {
            Outcomes.success(true)
        }
        // CASE 2: No auth and action requires roles!
        else if (noAuth) {
            Outcomes.restricted("Auth not configured")
        }
        // CASE 3: Proceed to authorize
        else {
            req.auth?.check(req.request, target.action.auth, actionRoles)
                ?: Outcomes.restricted("Auth not configured")
        }
        return Outcomes.invalid()
    }
}
