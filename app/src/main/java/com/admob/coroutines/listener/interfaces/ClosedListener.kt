package com.admob.coroutines.listener.interfaces

interface ClosedListener : AdvertisementListener {

    fun onClosed(isFullView: Boolean)
}