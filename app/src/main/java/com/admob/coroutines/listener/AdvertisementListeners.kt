package com.admob.coroutines.listener

import com.admob.coroutines.constant.ErrorCode
import com.admob.coroutines.listener.interfaces.*

class AdvertisementListeners {

    private val listeners = arrayListOf<AdvertisementListener>()

    fun getListeners() = listeners as List<AdvertisementListener>

    fun onClosed(listener: (isFullView: Boolean) -> Unit) {
        listeners.add(object : ClosedListener {
            override fun onClosed(isFullView: Boolean) {
                listener.invoke(isFullView)
            }
        })
    }

    fun onClicked(listener: () -> Unit) {
        listeners.add(object : ClickedListener {
            override fun onClicked() {
                listener.invoke()
            }
        })
    }

    fun onCompleted(listener: () -> Unit) {
        listeners.add(object : CompletedListener {
            override fun onCompleted() {
                listener.invoke()
            }
        })
    }

    fun onFailedToLoad(listener: (errorCode: ErrorCode) -> Unit) {
        listeners.add(object : FailedToLoadListener {
            override fun onFailed(errorCode: ErrorCode) {
                listener.invoke(errorCode)
            }
        })
    }

    fun onLoaded(listener: () -> Unit) {
        listeners.add(object : LoadedListener {
            override fun onLoaded() {
                listener.invoke()
            }
        })
    }

    fun onOpened(listener: () -> Unit) {
        listeners.add(object : OpenedListener {
            override fun onOpened() {
                listener.invoke()
            }
        })
    }
}