package kiit.serialization.responses

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import kiit.result.*
import kiit.codes.*
import kiit.result.Outcomes

/**
 * {
    "errs": [{
    "field": "email",
    "type": "input",
    "message": "Email already exists",
    "value": "slate.kit@gmail.com"
    }],
    "code": 500004,
    "success": false,
    "meta": null,
    "name": "CONFLICT",
    "tag": null,
    "value": null,
    "desc": "Conflict"
    }
 */
object ResponseDecoder {

    fun <T> outcome(json: String?, cls:Class<*>, converter:(JSONObject) -> T): Outcome<T> {
        val parser = JSONParser()
        val doc = parser.parse(json)
        val root = doc as JSONObject
        val result = when(root.get("success") as Boolean) {
            true -> {
                val status = decodeStatus(root) as Passed
                val value = converter(root)
                Success(value, status)
            }
            false -> {
                val status = decodeStatus(root) as Failed
                if (root.containsKey("errs")) {
                    val errList = decodeErrs(root)
                    Failure<Err>(errList, status)
                } else {
                    Failure<Err>(Err.of("unable to parse result"), status)
                }
            }
        }
        return result
    }


    fun decodeStatus(root:JSONObject): Status {
        val name = root.get("name") as String
        val title = root.get("desc") as? String ?: name
        val group = root.get("type") as String
        // Built-in code with the same group and name, otherwise a custom code in that group.
        val known = Codes.all.firstOrNull { it.group == group && it.name == name }
        return known ?: when(group) {
            "Succeeded"   -> Passed.Succeeded(name, title)
            "Pending"     -> Passed.Pending(name, title)
            "Excluded"    -> Passed.Excluded(name, title)
            "Information" -> Passed.Information(name, title)
            "Restricted"  -> Failed.Restricted(name, title)
            "Invalid"     -> Failed.Invalid(name, title)
            "Rejected"    -> Failed.Rejected(name, title)
            else          -> Failed.Unserved(name, title)
        }
    }


    fun decodeErrs(root:JSONObject): Err.ErrorList {
        val errs = when(val errs = root.get("errs")) {
            is JSONArray -> {
                errs.mapNotNull { err ->
                    when(err) {
                        is JSONObject -> decodeErr(err)
                        else -> null
                    }
                }
            }
            else -> listOf()
        }
        return Err.ErrorList(errs, if(errs.isNotEmpty()) errs[0].message else "")
    }


    fun decodeErr(err:JSONObject): Err {
        val type = err.getValue("type")
        return when(type?.toString()?.trim()){
            "input"  -> {
                Err.on(
                        field = err.getValue("field") as String,
                        value = err.getValue("value") as String,
                        message = err.getValue("message") as String
                )
            }
            "action" -> {
                Err.of(message = err.getValue("message") as String)
            }
            else -> Err.of(message = err.getValue("message") as String)
        }
    }
}