package kiit.common

import kiit.codes.Passed
import kiit.codes.StatusConstants

/**
 * Statuses for app and CLI level actions that are not part of the built-in kiit-codes registry.
 * They are ordinary [Passed.Succeeded] codes, so they pass through results like any other success.
 */
object CliCodes {
    @JvmField val HELP = Passed.Succeeded("HELP", "Help", StatusConstants.KIIT, "cli")
    @JvmField val ABOUT = Passed.Succeeded("ABOUT", "About", StatusConstants.KIIT, "cli")
    @JvmField val VERSION = Passed.Succeeded("VERSION", "Version", StatusConstants.KIIT, "cli")

    /** The built-in exit status. */
    @JvmField val EXIT = Passed.Succeeded.EXITED
}
