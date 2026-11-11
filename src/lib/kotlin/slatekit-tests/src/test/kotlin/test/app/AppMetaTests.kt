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

package slate.test

import kiit.codes.Status
import kiit.common.CliCodes
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import kiit.common.*
import kiit.common.info.About
import kiit.app.App
import kiit.app.AppRunner
import kiit.codes.Codes
import test.TestApp


class AppMetaTests  {


  @Test fun can_request_help() {
    checkHelp(arrayOf("help", "-help", "--help", "/help", "?"), CliCodes.HELP, "help")
  }


  @Test fun can_request_about() {
    checkHelp(arrayOf("about", "-about", "--about", "/about", "info"), CliCodes.ABOUT, "help")
  }


  @Test fun can_request_version() {
    checkHelp(arrayOf("version", "-version", "--version", "/version", "ver"), CliCodes.VERSION,  "help")
  }


  fun checkHelp(words:Array<String>, expected:Status, msg:String) {
    runBlocking {
      for (word in words) {
        val result = AppRunner.run(
                TestApp::class.java,
                rawArgs = arrayOf(word),
                about = About.none,
                builder = { ctx -> App(ctx) }
        )
        Assert.assertEquals(expected.name, result.status.name)
      }
    }
  }
}
