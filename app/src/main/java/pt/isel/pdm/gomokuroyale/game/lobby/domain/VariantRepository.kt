package pt.isel.pdm.gomokuroyale.game.lobby.domain

import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant

interface VariantRepository {

   suspend fun getVariants(): List<Variant>
   suspend fun storeVariants(variants: List<Variant>)

}