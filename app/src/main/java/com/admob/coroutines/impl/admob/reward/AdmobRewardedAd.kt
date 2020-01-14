package com.admob.coroutines.impl.admob.reward

import android.app.Activity
import com.admob.coroutines.Advertisement
import com.admob.coroutines.BuildConfig
import com.admob.coroutines.constant.ErrorCode
import com.admob.coroutines.impl.admob.listener.MultipleRewardedAdListener
import com.admob.coroutines.listener.interfaces.AdvertisementListener
import com.admob.coroutines.listener.interfaces.FailedToLoadListener
import com.admob.coroutines.listener.interfaces.LoadedListener
import com.admob.coroutines.logger.log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class AdmobRewardedAd : Advertisement {

    var activity: Activity? = null

    var key: String? = null

    companion object {
        private const val TEST_KEY = "ca-app-pub-3940256099942544/5224354917"
    }

    private var isLoadingStarted = false

    private val ad: RewardedAd by lazy {
        RewardedAd(activity, if (BuildConfig.DEBUG || key == null) TEST_KEY else key)
    }

    private val listeners = arrayListOf<AdvertisementListener>()

    override fun addListener(listener: AdvertisementListener) {
        listeners.add(listener)
    }

    override suspend fun show() {
        log("AdmobRewardedAd. Triggered show() method")

        val listener = MultipleRewardedAdListener(listeners)

        if (ad.isLoaded) {
            ad.show(activity, listener)

            log("AdmobRewardedAd. Ad is showed")
        } else {
            if (isLoadingStarted) {
                log("AdmobRewardedAd. Ad is loading. Waiting...")

                CoroutineScope(Job() + Dispatchers.IO).launch {
                    while (isLoadingStarted) {
                        continue
                    }

                    log("AdmobRewardedAd. Ad is loaded. Showing...")

                    withContext(Dispatchers.Main) {
                        ad.show(activity, listener)

                        log("AdmobRewardedAd. Ad is showed")
                    }
                }
            } else {
                log("AdmobRewardedAd. Ad is not loaded. Starting the loading")

                if (load()) {
                    ad.show(activity, listener)

                    log("AdmobRewardedAd. Ad is showed")
                }
            }
        }
    }

    override suspend fun load(): Boolean {
        log("AdmobRewardedAd. Started loading")

        val startTime = System.currentTimeMillis()

        return suspendCoroutine { continuation ->
            isLoadingStarted = true

            ad.loadAd(AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
                override fun onRewardedAdFailedToLoad(code: Int) {
                    super.onRewardedAdFailedToLoad(code)

                    val endTime = System.currentTimeMillis()

                    isLoadingStarted = false

                    val errorCode = when (code) {
                        AdRequest.ERROR_CODE_INTERNAL_ERROR -> ErrorCode.INTERNAL
                        AdRequest.ERROR_CODE_INVALID_REQUEST -> ErrorCode.INVALID_REQUEST
                        AdRequest.ERROR_CODE_NETWORK_ERROR -> ErrorCode.NETWORK
                        AdRequest.ERROR_CODE_NO_FILL -> ErrorCode.NO_FILL
                        else -> ErrorCode.INTERNAL
                    }

                    listeners
                        .filterIsInstance<FailedToLoadListener>()
                        .forEach {
                            it.onFailed(errorCode)
                        }

                    log("AdmobRewardedAd. Ad was not loaded. ErrorCode = $errorCode. Duration in millis = ${endTime - startTime}")

                    continuation.resume(false)
                }

                override fun onRewardedAdLoaded() {
                    super.onRewardedAdLoaded()

                    val endTime = System.currentTimeMillis()

                    isLoadingStarted = false

                    listeners
                        .filterIsInstance<LoadedListener>()
                        .forEach {
                            it.onLoaded()
                        }

                    log("AdmobRewardedAd. Ad is loaded. Duration in millis = ${endTime - startTime}")

                    continuation.resume(true)
                }
            })
        }
    }
}