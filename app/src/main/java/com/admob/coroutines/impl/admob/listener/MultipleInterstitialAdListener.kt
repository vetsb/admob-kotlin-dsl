package com.admob.coroutines.impl.admob.listener

import com.admob.coroutines.constant.ErrorCode
import com.admob.coroutines.listener.interfaces.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest

internal class MultipleInterstitialAdListener(
    private val listeners: List<AdvertisementListener>
) : AdListener() {

    override fun onAdClicked() {
        super.onAdClicked()

        listeners
            .filterIsInstance<ClickedListener>()
            .forEach {
                it.onClicked()
            }
    }

    override fun onAdFailedToLoad(code: Int) {
        super.onAdFailedToLoad(code)

        listeners
            .filterIsInstance<FailedToLoadListener>()
            .forEach {
                val errorCode = when (code) {
                    AdRequest.ERROR_CODE_INTERNAL_ERROR -> ErrorCode.INTERNAL
                    AdRequest.ERROR_CODE_INVALID_REQUEST -> ErrorCode.INVALID_REQUEST
                    AdRequest.ERROR_CODE_NETWORK_ERROR -> ErrorCode.NETWORK
                    AdRequest.ERROR_CODE_NO_FILL -> ErrorCode.NO_FILL
                    else -> ErrorCode.INTERNAL
                }

                it.onFailed(errorCode)
            }
    }

    override fun onAdClosed() {
        super.onAdClosed()

        listeners
            .filterIsInstance<ClosedListener>()
            .forEach {
                it.onClosed(true)
            }
    }

    override fun onAdOpened() {
        super.onAdOpened()

        listeners
            .filterIsInstance<OpenedListener>()
            .forEach {
                it.onOpened()
            }
    }

    override fun onAdLoaded() {
        super.onAdLoaded()

        listeners
            .filterIsInstance<LoadedListener>()
            .forEach {
                it.onLoaded()
            }
    }
}