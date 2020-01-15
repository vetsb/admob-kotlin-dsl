package com.admob.coroutines.impl.admob.dsl

import android.app.Activity
import android.content.Context
import android.view.View
import com.admob.coroutines.Advertisement
import com.admob.coroutines.impl.admob.banner.AdmobBannerAd
import com.admob.coroutines.impl.admob.banner.BannerAdvertisement
import com.admob.coroutines.impl.admob.interstitial.AdmobInterstitialAd
import com.admob.coroutines.impl.admob.reward.AdmobRewardedAd
import com.admob.coroutines.listener.AdvertisementListeners

fun bannerAdView(context: Context, init: BannerAdvertisement.() -> Unit): View {
    val ad = AdmobBannerAd(context)

    ad.init()

    return ad.getView()
}

fun interstitialAd(context: Context, init: Advertisement.() -> Unit): Advertisement {
    val ad = AdmobInterstitialAd(context)

    ad.init()

    return ad
}

fun rewardedAd(activity: Activity, init: Advertisement.() -> Unit): Advertisement {
    val ad = AdmobRewardedAd().apply {
        this.activity = activity
    }

    ad.init()

    return ad
}

fun Advertisement.addListeners(init: AdvertisementListeners.() -> Unit) {
    val listeners = AdvertisementListeners()

    listeners.init()

    listeners.getListeners().forEach {
        addListener(it)
    }
}