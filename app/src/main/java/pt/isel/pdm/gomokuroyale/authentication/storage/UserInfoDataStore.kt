package pt.isel.pdm.gomokuroyale.authentication.storage

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import kotlin.time.Duration.Companion.minutes

/**
 * The token expiration time is 59 minutes.
 **/
val TOKEN_EXPIRATION_TIME =  59.minutes

class UserInfoDataStore(
    private val store: DataStore<Preferences>,
    private val clock: Clock
) : UserInfoRepository {

    private val usernameKey = stringPreferencesKey(USER_NAME)
    private val accessToken = stringPreferencesKey(ACCESS_TOKEN)
    private val points = stringPreferencesKey(POINTS)
    private val time = stringPreferencesKey(TIME)


    override suspend fun login(userInfo: UserInfo) {
        val expirationTime = calculateExpirationTime()
        store.edit { preferences ->
            preferences[usernameKey] = userInfo.username
            preferences[accessToken] = userInfo.accessToken
            preferences[time] = expirationTime.toString()
        }
    }

    override suspend fun logout() {
        store.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun getUserInfo(): UserInfo? {
        val preferences = store.data.first()
        val username = preferences[usernameKey] ?: return null
        val accessToken = preferences[accessToken] ?: return null
        return UserInfo(accessToken, username)
    }



    override suspend fun isLoggedIn(): Boolean {
        val preferences = store.data.first()
        val userInfo = preferences[usernameKey]
        val tokenExpiration = preferences[time]?.let { Instant.parse(it) } ?: return false
        val now = clock.now()
        return userInfo != null && tokenExpiration > now
    }

    private fun calculateExpirationTime(): Instant {
        return clock.now().plus(TOKEN_EXPIRATION_TIME)
    }

    companion object {
        private const val USER_NAME = "username"
        private const val ACCESS_TOKEN = "access_token"
        private const val POINTS = "points"
        private const val TIME = "time"
    }
}