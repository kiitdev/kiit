package test.setup

import kiit.common.checks.Check
import kiit.result.Outcome
import kiit.result.Outcomes


open class SampleErrorsNoMiddlewareApi  {

    /**
     * Error-handling using the Result<T> object to model
     * successes and failures for all scenarios
     */
    fun parseNumberWithResults(text:String): Outcome<Int> {

        return if(text.isNullOrEmpty()) {
            Outcomes.invalid("You must supply a non-empty string")
        }
        else if(!Check.isNumeric(text)){
            Outcomes.rejected("$text is not a valid number")
        }
        else {
            Outcomes.success(text.toInt())
        }
    }


    /**
     * Error-handling exceptions for bad inputs.
     * NOTE: This class implements the ApiWithMiddleWare interface
     * and has the "isErrorEnabled" flag set to true which will result
     * in the "onException" method handling the exception here.
     */
    fun parseNumberWithExceptions(text:String): Int {

        // This will throw exception for non-numbers
        val num = text.toInt()
        return num
    }
}


