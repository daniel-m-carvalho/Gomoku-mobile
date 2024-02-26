package pt.isel.pdm.gomokuroyale.http.domain

/**
 * Data class representing a Recipe in the application.
 *
 * @property rel The relationship type of the Recipe.
 * @property href The hyperlink reference of the Recipe.
 */
data class Recipe(
    val rel : String,
    val href : String,
)