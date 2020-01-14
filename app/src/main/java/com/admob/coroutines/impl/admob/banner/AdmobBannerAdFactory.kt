package com.admob.coroutines.impl.admob.banner

import android.content.Context
import android.view.View
import com.admob.coroutines.BuildConfig
import com.admob.coroutines.constant.BannerSize
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * Creates View with advertisement.
 */
class AdmobBannerAdFactory(
    private val context: Context,
    private val key: String?
) {

    companion object {
        private const val TEST_KEY = "ca-app-pub-3940256099942544/6300978111"
    }

    /**
     * Creates AdView
     */
    fun createView(size: BannerSize): View {
        return AdView(context).apply {
            adUnitId = if (BuildConfig.DEBUG || key == null)
                TEST_KEY
            else
                key

            adSize = when (size) {
                BannerSize.BANNER -> AdSize.BANNER
                BannerSize.LARGE_BANNER -> AdSize.LARGE_BANNER
                BannerSize.MEDIUM_RECTANGLE -> AdSize.MEDIUM_RECTANGLE
                BannerSize.FULL_BANNER -> AdSize.FULL_BANNER
                BannerSize.LEADERBOARD -> AdSize.LEADERBOARD
                BannerSize.SMART_BANNER -> AdSize.SMART_BANNER
            }

            loadAd(AdRequest.Builder().build())
        }
    }
}