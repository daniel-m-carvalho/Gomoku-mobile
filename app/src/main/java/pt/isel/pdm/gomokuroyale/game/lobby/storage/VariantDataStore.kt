package pt.isel.pdm.gomokuroyale.game.lobby.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pt.isel.pdm.gomokuroyale.game.lobby.domain.VariantRepository
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import java.lang.reflect.Type

class VariantDataStore(
    private val store: DataStore<Preferences>,
    private val gson: Gson
) : VariantRepository {

    override suspend fun getVariants(): List<Variant> {
        val variantsJson = store.data.map { preferences ->
            preferences[variantsKey] ?: "[]"
        }.first()
        val type: Type = object : TypeToken<List<Variant>>() {}.type
        return gson.fromJson(variantsJson, type)
    }

    override suspend fun storeVariants(variants: List<Variant>) {
        val variantsJson = gson.toJson(variants)
        store.edit { preferences ->
            preferences[variantsKey] = variantsJson
        }
    }

    companion object {
        private val variantsKey = stringPreferencesKey("variants")
    }
}