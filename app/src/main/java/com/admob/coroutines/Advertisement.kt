package com.admob.coroutines

import com.admob.coroutines.listener.interfaces.AdvertisementListener

interface Advertisement {

    var key: String?

    fun addListener(listener: AdvertisementListener)

    /**
     * Loads and shows advertisement
     */
    suspend fun show()

    /**
     * Only loads advertisement.
     *
     * @return Whether ad is loaded
     */
    suspend fun load(): Boolean
}