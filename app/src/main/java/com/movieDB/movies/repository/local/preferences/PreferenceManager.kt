package com.grassdoor.android.repository.local.preferences

import android.content.Context
import android.content.SharedPreferences

open class PreferenceManager constructor(context: Context) : PrefsHelper {
    private val PREFS_NAME = "GrassdoorPrefs"

    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun setApiToken(apiKey: String) {
        preferences[API_KEY] = apiKey
    }

    override fun getApiToken(): String {
        return preferences[API_KEY] ?: ""
    }

    override fun setUserId(userId: String) {
        preferences[USER_ID] = userId
    }

    override fun getUserId(): String {
        return preferences[USER_ID] ?: ""
    }

    override fun setIsLoggedIn(isLoggedIn: Boolean) {
        preferences[IS_LOGGED_IN] = isLoggedIn
    }

    override fun isLoggedIn(): Boolean {
        return preferences[IS_LOGGED_IN] ?: false
    }

    override fun clearPrefs() {
        preferences.edit().clear().apply()
    }

    override fun setLocationZipcode(zipcode: String) {
        preferences[LOCATION_ZIPCODE] = zipcode
    }

    override fun getLocationZipcode(): String {
        return preferences[LOCATION_ZIPCODE] ?: "00000"
    }

    override fun setPlaceId(id: String) {
        preferences[PLACE_ID] = id
    }

    override fun getPlaceId(): String {
        return preferences[PLACE_ID] ?: ""
    }

    override fun setCountry(country: String) {
        preferences[COUNTRY_NAME] = country
    }

    override fun getCountry(): String {
        return preferences[COUNTRY_NAME] ?: ""
    }

    override fun setState(state: String) {
        preferences[STATE_NAME] = state
    }

    override fun getState(): String {
        return preferences[STATE_NAME] ?: ""
    }

    override fun setCity(city: String) {
        preferences[CITY_NAME] = city
    }

    override fun getCity(): String {
        return preferences[CITY_NAME] ?: ""
    }

    override fun setLatitude(lat: String) {
        preferences[LATITUDE] = lat
    }

    override fun getLatitude(): String {
        return preferences[LATITUDE] ?: ""
    }

    override fun setLongitude(lon: String) {
        preferences[LONGITUDE] = lon
    }

    override fun getLongitude(): String {
        return preferences[LONGITUDE] ?: ""
    }

    override fun setAddress(address: String) {
        preferences[ADDRESS] = address
    }

    override fun getAddress(): String {
        return preferences[ADDRESS] ?: ""
    }

    override fun setCountryCode(code: String) {
        preferences[COUNTRY_CODE] = code
    }

    override fun getCountryCode(): String {
        return preferences[COUNTRY_CODE] ?: ""
    }

    override fun setScheduledType(type: String) {
        preferences[SCHEDULED_TYPE] = type
    }

    override fun getScheduledType(): String {
        return preferences[SCHEDULED_TYPE] ?: ""
    }

    companion object {
        const val API_KEY = "api_key"
        const val USER_ID = "user_id"
        const val IS_LOGGED_IN = "is_logged_in"
        const val LOCATION_ZIPCODE = "location_zipcode"
        const val PLACE_ID = "place_id"
        const val COUNTRY_NAME = "country_name"
        const val STATE_NAME = "state_name"
        const val CITY_NAME = "city_name"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ADDRESS = "address"
        const val COUNTRY_CODE = "countryCode"
        const val SCHEDULED_TYPE = "scheduledType"
    }

    /**
     * SharedPreferences extension function, to listen the edit() and apply() fun calls
     * on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    private operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }


    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    private inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}