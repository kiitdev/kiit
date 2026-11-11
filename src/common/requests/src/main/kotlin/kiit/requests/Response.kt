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
package kiit.requests

import kiit.codes.Failed
import kiit.codes.Passed

/**
 * General purpose class to model a Response at an application boundary ( such as http response )
 * NOTE: This is used for the APIs in Slate Kit
 *
 * [type] is the status group ( e.g. "Succeeded", "Rejected" ) and [code] is the status
 * as a protocol code ( http status when built from a result via [toResponse] ).
 */
interface Response<out T> {
    val success: Boolean
    val name: String
    val type: String
    val code: Int
    val meta: Map<String, String>?
    val value: T?
    val desc: String?
    val err: Exception?
    val tag: String?

    /**
     * adds to the existing metadata
     */
    fun withMeta(meta: List<Pair<String, String>>): Response<T>
}

// The checks below go by status group ( the response type ), not by the numeric code, since the
// code is a protocol code and does not separate the groups ( e.g. Rejected maps to http 409 ).
private val successGroups = setOf(
    Passed.Succeeded.DEFAULT.group,
    Passed.Pending.DEFAULT.group,
    Passed.Information.DEFAULT.group
)
private val excludedGroup = Passed.Excluded.DEFAULT.group
private val badRequestGroups = setOf(Failed.Invalid.DEFAULT.group, Failed.Restricted.DEFAULT.group)
private val failureGroups = setOf(Failed.Rejected.DEFAULT.group, Failed.Unserved.DEFAULT.group)

fun <T> Response<T>.isInSuccessRange(): Boolean = this.type in successGroups
fun <T> Response<T>.isFilteredOut(): Boolean = this.type == excludedGroup
fun <T> Response<T>.isInBadRequestRange(): Boolean = this.type in badRequestGroups
fun <T> Response<T>.isInFailureRange(): Boolean = this.type in failureGroups
