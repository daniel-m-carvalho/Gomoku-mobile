package pt.isel.pdm.gomokuroyale.util

/**
 * Represents search terms. A term is a string with between 3 and 120 characters. White spaces
 * at the beginning and end of the term are ignored.
 * @property value The term's text.
 * @see https://github.com/isel-leic-pdm/2324i/blob/fb422ab8a84b7f7e49bc0b0466fd782f398c4f2b/demos/Jokes/app/src/main/java/isel/pdm/jokes/domain/JokesService.kt
 */
@JvmInline
value class Term(val value: String) {
    init {
        require(isValidTerm(value)) { "Term must have between 3 and 120 characters" }
    }
}

/**
 * Checks if the given string is a valid term.
 * @param termWannabe The string to check.
 * @return `true` if the given string is a valid term, `false` otherwise.
 */
fun isValidTerm(termWannabe: String): Boolean = termWannabe.trim().let {
    it.isNotBlank() && it.length in 3 until 120
}

/**
 * Extension method that converts this string into a [Term] instance.
 * @return The [Term] instance or `null` if this string is not a valid term.
 */
fun String.toTermOrNull(): Term? = if (isValidTerm(this)) Term(this) else null