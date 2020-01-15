package com.admob.coroutines.impl.admob.interstitial

import android.content.Context
import com.admob.coroutines.Advertisement
import com.admob.coroutines.BuildConfig
import com.admob.coroutines.constant.ErrorCode
import com.admob.coroutines.impl.admob.listener.MultipleInterstitialAdListener
import com.admob.coroutines.listener.interfaces.AdvertisementListener
import com.admob.coroutines.listener.interfaces.FailedToLoadListener
import com.admob.coroutines.listener.interfaces.LoadedListener
import com.admob.coroutines.logger.log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class AdmobInterstitialAd(
    private val context: Context?
) : Advertisement {

    companion object {
        private const val TEST_KEY = "ca-app-pub-3940256099942544/1033173712"
    }

    override var key: String? = null

    private var isLoadingStarted = false

    private val listeners = arrayListOf<AdvertisementListener>()

    private val ad: InterstitialAd by lazy {
        InterstitialAd(context).apply {
            adUnitId = if (BuildConfig.DEBUG || key == null)
                TEST_KEY
            else
                key

            adListener = MultipleInterstitialAdListener(listeners)
        }
    }

    override fun addListener(listener: AdvertisementListener) {
        listeners.add(listener)
    }

    override suspend fun show() {
        log("AdmobInterstitialAd. Triggered show() method")

        if (ad.isLoaded) {
            ad.show()

            log("AdmobInterstitialAd. Ad is showed")
        } else {
            if (isLoadingStarted) {
                log("AdmobInterstitialAd. Ad is loading. Waiting...")

                CoroutineScope(Job() + Dispatchers.IO).launch {
                    while (isLoadingStarted) {
                        continue
                    }

                    log("AdmobInterstitialAd. Ad is loaded. Showing...")

                    withContext(Dispatchers.Main) {
                        ad.show()

                        log("AdmobInterstitialAd. Ad is showed")
                    }
                }
            } else {
                log("AdmobInterstitialAd. Ad is not loaded. Starting the loading")

                if (load()) {
                    ad.show()

                    log("AdmobInterstitialAd. Ad is showed")
                }
            }
        }
    }

    override suspend fun load(): Boolean {
        log("AdmobInterstitialAd. Started loading")

        val startTime = System.currentTimeMillis()

        return suspendCoroutine { continuation ->
            listeners.add(object : FailedToLoadListener {
                override fun onFailed(errorCode: ErrorCode) {
                    val endTime = System.currentTimeMillis()

                    isLoadingStarted = false

                    listeners.remove(this)

                    log("AdmobInterstitialAd. Ad was not loaded. ErrorCode = $errorCode. Duration in millis = ${endTime - startTime}")

                    continuation.resume(false)
                }
            })

            listeners.add(object : LoadedListener {
                override fun onLoaded() {
                    val endTime = System.currentTimeMillis()

                    isLoadingStarted = false

                    listeners.remove(this)

                    log("AdmobInterstitialAd. Ad is loaded. Duration in millis = ${endTime - startTime}")

                    continuation.resume(true)
                }
            })

            isLoadingStarted = true

            ad.loadAd(AdRequest.Builder().build())
        }
    }
}