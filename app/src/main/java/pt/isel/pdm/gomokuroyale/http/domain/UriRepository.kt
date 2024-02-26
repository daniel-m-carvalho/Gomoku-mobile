package pt.isel.pdm.gomokuroyale.http.domain

import pt.isel.pdm.gomokuroyale.http.utils.Rels

interface UriRepository {

    suspend fun updateRecipeLinks(recipeLinks : List<Recipe>): List<Recipe>

    suspend fun getRecipeLink(rel : String): Recipe?
}