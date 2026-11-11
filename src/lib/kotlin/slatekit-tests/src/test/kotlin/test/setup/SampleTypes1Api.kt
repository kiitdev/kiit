package test.setup

import kiit.codes.Failed
import kiit.apis.Api
import kiit.common.crypto.Encryptor
import kiit.result.*
import kiit.codes.*
import kiit.result.Outcomes
import kiit.result.Tries


@Api(area = "samples", name = "types1", desc = "sample api to test getting different values")
class SampleTypes1Api {

    val enc: Encryptor = AppEncryptor

    /**
     * GET localhost:{port}/samples/types1/basic_string
     * GET localhost:{port}/samples/types1/basic_list
     */
    fun getBasicString()          : String            = "string"
    fun getBasicBoolean()         : Boolean           = true
    fun getBasicLong()            : Long              = 20L
    fun getBasicDouble()          : Double            = 123.45
    fun getEncrypted(text:String) : String            = enc.encrypt(text)
    fun getResult()               : Outcome<Int>       = Success(12345)
    fun getPair()                 : Pair<String,Long> = Pair("abc", 123)
    fun getBasicList()            : List<String>      = listOf("a", "b", "c")
    fun getBasicMap()             : Map<String,Int>   = mapOf("a" to 1, "b" to 2, "c" to 3)
    fun getObject()               : Movie = Movie.samples()[0]
    fun getListOfObject()         : List<Movie>       = Movie.samples()

    /**
     * These examples use the Slate Kit Result<T> to model success and failures.
     * The Result<T> has status codes that are HTTP compliant
     */
    fun getSuccess()              : Outcome<Movie>     = Success(Movie.samples().first())
    fun getBadRequest()           : Outcome<Movie>     = Outcomes.invalid("Check your inputs")
    fun getNotFound()             : Outcome<Movie>     = Outcomes.invalid(Err.of("Item not found"), Failed.Invalid.NOT_FOUND)
    fun getUnauthorized()         : Outcome<Movie>     = Outcomes.restricted("You can not edit this item")
    fun getConflict()             : Outcome<Movie>     = Outcomes.rejected("Item has already been changed", Failed.Rejected.CONFLICT)
    fun getFailure()              : Outcome<Movie>     = Outcomes.rejected("Error finding item")
    fun getDeprecated()           : Outcome<Movie>     = Outcomes.rejected("This feature is deprecated", Failed.Rejected("DEPRECATED", "Deprecated"))
    fun getUnexpected()           : Try<Movie>        = Tries.unserved(Exception("Unexpected errro occured"))
}
