package com.admob.coroutines.listener.interfaces

import com.admob.coroutines.constant.ErrorCode

interface FailedToLoadListener : AdvertisementListener {

    fun onFailed(errorCode: ErrorCode)
}