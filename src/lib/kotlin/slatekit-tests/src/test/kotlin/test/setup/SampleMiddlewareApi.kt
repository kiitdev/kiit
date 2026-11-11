package test.setup

import kiit.apis.*
import kiit.result.Outcome
import kiit.result.Outcomes


@Api(area = "app", name = "SampleMiddleware", desc = "api to access and manage users 3", auth = AuthModes.NONE, policies = ["api_test"])
open class SampleMiddlewareApi  {

    // Used for demo/testing purposes
    var middlewareHook = mutableListOf<ApiRequest>()


    @Action(policies = ["action_test"])
    fun hi(): String {
        return "hi world"
    }


    @Action(policies = ["action_test"])
    fun hello(): String {
        return "hello world"
    }


    @Action()
    fun unexpected(): String {
        throw Exception("Testing failed middleware")
    }


    @Action()
    fun errored(): Outcome<String> {
        return Outcomes.rejected("test failed")
    }
}