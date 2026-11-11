/**
 *  <kiit_header>
 * url: www.kiit.dev
 * git: www.github.com/slatekit/kiit
 * org: www.codehelix.co
 * author: Kishore Reddy
 * copyright: 2016 CodeHelix Solutions Inc.
 * license: refer to website and/or github 
 *  </kiit_header>
 */

package test.setup

import kiit.apis.*
import kiit.common.*
import kiit.apis.core.Roles
import kiit.common.types.ContentFile
import kiit.common.crypto.EncDouble
import kiit.common.crypto.EncString
import kiit.common.crypto.EncInt
import kiit.common.crypto.EncLong
import kiit.common.values.Metadata
import kiit.requests.Request
import kiit.connectors.entities.AppEntContext
import kiit.result.Outcome
import kiit.result.Success


@Api(area = "app", name = "users", desc = "api to access and manage users 3", roles= ["admin"], auth = AuthModes.TOKEN, verb = Verbs.AUTO, sources = [Sources.ALL])
class UserApi(val context: AppEntContext)
  //: ApiBaseEntity<Long, User, EntityService<Long, User>>(context, Long::class, User::class, context.ent.getService())
{

  @Action(name = "activate", desc = "activates a users account 3", roles= [Roles.PARENT], sources = [Sources.PARENT])
  @Input(name = "phone", desc = "phone number", examples = ["123-456-789"])
  @Input(name = "code", desc = "activation code", defaults = "0", examples = ["1234"])
  fun activate(phone:String, code:Int, isPremiumUser:Boolean, date: DateTime): Outcome<String> =
    Success("ok activated $phone, $code, $isPremiumUser, $date")


  @Action(desc = "activates a users account 3")
  @Input(name = "phone", desc = "phone number", examples = ["123-456-789"])
  @Input(name = "code", desc = "activation code", defaults = "0", examples = ["1234"])
  fun testTypes(phone:String, current:Boolean, code:Short, zip:Int, id:Long, rating:Float, value:Double, date: DateTime): Outcome<String> =
          Success("ok $phone, $current, $code, $zip, $id, $rating, $value, $date")


  @Action(name = "", desc = "activates a users account 3", roles= [""], verb = Verbs.GET)
  fun info(format:String = "json"): Outcome<String> {
    return Success("ok info")
  }

  
  @Action(sources = [Sources.CLI])
  fun protocolCLI(code:Int, tag:String): Outcome<String> {
    return Success("protocolCLI ${code} ${tag}")
  }


  @Action(sources = [Sources.WEB])
  fun protocolWeb(code:Int, tag:String): Outcome<String> {
    return Success("protocolWeb ${code} ${tag}")
  }


  @Action(sources = [Sources.ALL])
  fun protocolAny(code:Int, tag:String): Outcome<String> {
    return Success("protocolAny ${code} ${tag}")
  }


  @Action()
  fun protocolParent(code:Int, tag:String): Outcome<String> {
    return Success("protocolParent ${code} ${tag}")
  }


  @Action(desc = "")
  fun testException(code:Int, tag:String): Outcome<String> {
    throw Exception("Test unhandled exception")
  }


  @Action(desc = "", roles= [Roles.NONE])
  fun rolesNone(code:Int, tag:String): Outcome<String> {
    return Success("rolesNone ${code} ${tag}")
  }


  @Action(desc = "", roles=[Roles.ALL])
  fun rolesAny(code:Int, tag:String): Outcome<String> {
    return Success("rolesAny ${code} ${tag}")
  }


  @Action(desc = "", roles= ["dev"])
  fun rolesSpecific(code:Int, tag:String): Outcome<String>  {
    return Success("rolesSpecific ${code} ${tag}")
  }


  @Action(desc = "", roles= [Roles.PARENT])
  fun rolesParent(code:Int, tag:String): Outcome<String> {
    return Success("rolesParent ${code} ${tag}")
  }


  @Action(desc = "invites a new user", roles= [Roles.PARENT])
  fun invite(email:String, phone:String, promoCode:String): Outcome<String> {
    return Success("ok sent invitation to $email, $phone, $promoCode")
  }


  @Action(desc = "invites a new user", roles= [Roles.PARENT])
  fun register(user: User): Outcome<String> {
    return Success("ok object user")
  }


  @Action(desc = "test decryption of int", roles=[Roles.ALL])
  fun decInt(id: EncInt): Outcome<String> {
    return Success("ok" + " " + "decrypted int : " + id.value)
  }


  @Action(desc = "test decryption of long", roles=[Roles.ALL])
  fun decLong(id: EncLong): Outcome<String> {
    return Success("ok" + " " + "decrypted long : " + id.value)
  }


  @Action(desc = "test decryption of double", roles=[Roles.ALL])
  fun decDouble(id: EncDouble): Outcome<String>
  {
    return Success("ok" + " " + "decrypted double : " + id.value)
  }


  @Action(desc = "test decryption of string", roles=[Roles.ALL])
  fun decString(id: EncString): Outcome<String>
  {
    return Success("ok" + " " + "decrypted string : " + id.value)
  }


  @Action(name = "testArgs", desc = "test types", roles=[Roles.ALL])
  fun testArgs(phone:String, code:Int, isPremiumUser:Boolean, date:DateTime, key: EncString): Outcome<String>
  {
    return Success("ok $phone $code $isPremiumUser, $key")
  }


  @Action(desc = "", roles=[Roles.ALL], verb = Verbs.POST)
  fun argTypeRequest(req: Request): Outcome<String> {
    return Success("ok" + " " + "raw send id: " + req.data!!.getInt("id"))
  }


  @Action(desc = "", roles=[Roles.ALL], verb = Verbs.POST)
  fun argTypeMeta(meta: Metadata): Outcome<String> {
    return Success("ok" + " " + "raw meta token: " + meta.get("token"))
  }


  @Action(desc = "gets the current promo code", roles=[Roles.ALL], verb = Verbs.POST)
  fun argTypeFile(doc: ContentFile): Outcome<String> {
    return Success("ok" + " " + String(doc.data))
  }


  @Action(desc = "gets the current promo code", roles=[Roles.ALL], verb = Verbs.POST)
  fun argTypeListString(items:List<String>): Outcome<String> {
    return Success("ok" + " " + items.fold("", { acc, curr -> acc + "," + curr } ))
  }


  @Action(desc = "gets the current promo code", roles=[Roles.ALL], verb = Verbs.POST)
  fun argTypeListInt(items:List<Int>): Outcome<String> {
    return Success("ok" + " " + items.fold("", { acc, curr -> acc + "," + curr.toString() } ))
  }


  @Action(desc = "gets the current promo code", roles=[Roles.ALL], verb = Verbs.POST)
  fun argTypeMapInt(items:Map<String,Int>): Outcome<String> {
    val sortedPairs = items.keys.toList().sortedBy{ k:String -> k }.map{ key -> Pair(key, items[key]) }
    val delimited = sortedPairs.fold("", { acc, curr -> acc + "," + curr.first + "=" + curr.second } )
    return Success("ok" + " " + delimited)
  }
}
