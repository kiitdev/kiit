package kiit.utils.naming

/**
 * LowerCamel       lowerCamel
 * UpperCamel       UpperCamel
 * LowerHyphen      lower-hyphen
 * UpperHyphen      UPPER-HYPHEN
 * LowerUnderscore	lower_underscore
 * UpperUnderscore	UPPER_UNDERSCORE
 */
interface Case {
    val text: String
}

fun convertToCase(text: String, upper: Boolean, replacements: Set<Char>, replacement: Char): String {
    return text.foldIndexed("") { ndx, acc, c ->
        val currentCharIsUpper = c.isLetter() && c.uppercaseChar() == c
        val ch: String = if (replacements.contains(c)) {
            replacement.toString()
        } else if (ndx > 0 && text[ndx - 1].lowercaseChar() == text[ndx - 1] && currentCharIsUpper) {
            replacement.toString() + (if (upper) c.uppercaseChar() else c.lowercaseChar())
        } else {
            if (upper) c.uppercaseChar().toString() else c.lowercaseChar().toString()
        }
        acc + ch
    }
}

fun convertToCamel(text: String, upper: Boolean, replacements: Set<Char>): String {
    return text.foldIndexed("") { ndx, acc, c ->
        val isUpper = c.uppercaseChar() == c
        val ch = if (replacements.contains(c)) {
            Char.MIN_SURROGATE
        } else if (ndx == 0 && upper) {
            c.uppercaseChar()
        } else if (ndx == 0 && !upper) {
            c.lowercaseChar()
        } else if (ndx > 0 && replacements.contains(text[ndx - 1])) {
            c.uppercaseChar()
        } else if (ndx > 0 && text[ndx - 1].lowercaseChar() == text[ndx - 1] && isUpper) {
            c.uppercaseChar()
        } else {
            c
        }
        if (ch == Char.MIN_SURROGATE) acc else acc + ch
    }
}
