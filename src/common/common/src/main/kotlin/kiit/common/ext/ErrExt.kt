package kiit.common.ext

import kiit.codes.Err


/**
 * Builds a list of errs:
 * error a
 * error b
 * error c
 */
fun Err.strings(): List<String> {
    val all = this.flatten()
    return all.map { err ->
        when(err){
            is Err.ErrorInfo  -> "Error   : " + err.message
            is Err.ErrorField -> "Field   : name=" + err.field + ", value=" + err.value + ", msg=" + err.message
            else              -> "${err.message}"
        }
    }
}


/**
 * Builds a list of numbered errs:
 * 1. error a
 * 2. error b
 * 3. error c
 */
fun Err.numbered(): List<String> {
    return this.strings().mapIndexed { ndx, it -> "${ndx + 1}. $it"}
}


/**
 * Recursively flattens the err into the list provided.
 * E.g. Err could be
 * 1. @see[kiit.codes.Err.ErrorInfo]
 * 2. @see[kiit.codes.Err.ErrorField]
 * 3. @see[kiit.codes.Err.ErrorList]
 */
fun Err.flatten(): List<Err> {
    return when (this) {
        is Err.ErrorField -> listOf(this)
        is Err.ErrorInfo -> listOf(this)
        is Err.ErrorList -> {
            val errs = this.errors
            val childErrors = errs.map { it.flatten() }
            val allErrors = childErrors.flatten()
            allErrors
        }
    }
}