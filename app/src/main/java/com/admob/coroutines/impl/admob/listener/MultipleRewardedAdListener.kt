package com.admob.coroutines.impl.admob.listener

import com.admob.coroutines.constant.ErrorCode
import com.admob.coroutines.listener.interfaces.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback

internal class MultipleRewardedAdListener(
    private val listeners: List<AdvertisementListener>
) : RewardedAdCallback() {

    private var isCompleted = false

    override fun onUserEarnedReward(rewardItem: RewardItem) {
        isCompleted = true

        listeners
            .filterIsInstance<CompletedListener>()
            .forEach {
                it.onCompleted()
            }
    }

    override fun onRewardedAdFailedToShow(code: Int) {
        super.onRewardedAdFailedToShow(code)

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

    override fun onRewardedAdClosed() {
        super.onRewardedAdClosed()

        listeners
            .filterIsInstance<ClosedListener>()
            .forEach {
                it.onClosed(isCompleted)
            }

        isCompleted = false
    }

    override fun onRewardedAdOpened() {
        super.onRewardedAdOpened()

        listeners
            .filterIsInstance<OpenedListener>()
            .forEach {
                it.onOpened()
            }
    }
}