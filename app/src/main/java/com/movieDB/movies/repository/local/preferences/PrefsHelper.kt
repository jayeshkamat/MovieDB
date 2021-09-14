package com.grassdoor.android.repository.local.preferences

interface PrefsHelper {
    fun setApiToken(apiKey: String)
    fun getApiToken(): String
    fun setUserId(userId: String)
    fun getUserId(): String
    fun setIsLoggedIn(isLoggedIn: Boolean)
    fun isLoggedIn(): Boolean
    fun setLocationZipcode(code: String)
    fun getLocationZipcode(): String
    fun setPlaceId(id: String)
    fun getPlaceId(): String
    fun setCountry(country: String)
    fun getCountry(): String
    fun setState(state: String)
    fun getState(): String
    fun setCity(city: String)
    fun getCity(): String
    fun setLatitude(lat: String)
    fun getLatitude(): String
    fun setLongitude(lon: String)
    fun getLongitude(): String
    fun setAddress(address: String)
    fun getAddress(): String
    fun setCountryCode(code: String)
    fun getCountryCode(): String
    fun setScheduledType(type: String)
    fun getScheduledType(): String
    fun clearPrefs()
}