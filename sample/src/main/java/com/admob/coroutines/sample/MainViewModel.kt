package com.admob.coroutines.sample

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admob.coroutines.impl.admob.dsl.addListeners
import com.admob.coroutines.impl.admob.dsl.interstitialAd
import com.admob.coroutines.impl.admob.dsl.rewardedAd
import kotlinx.coroutines.launch

class MainViewModel(
    private val activity: Activity
) : ViewModel() {

    private val isVisibleProgressBarLiveData = MutableLiveData<Boolean>()

    fun getVisibleProgressBarLiveData() = isVisibleProgressBarLiveData as LiveData<Boolean>

    fun onButtonShowInterstitialClick() {
        val interstitialAd = interstitialAd(activity) {
            addListeners {
                onFailedToLoad {
                    isVisibleProgressBarLiveData.postValue(false)
                }

                onLoaded {
                    isVisibleProgressBarLiveData.postValue(false)
                }
            }
        }

        viewModelScope.launch {
            isVisibleProgressBarLiveData.postValue(true)

            interstitialAd.show()
        }
    }

    fun onButtonShowRewardedClick() {
        val rewardedAd = rewardedAd(activity) {
            addListeners {
                onFailedToLoad {
                    isVisibleProgressBarLiveData.postValue(false)
                }

                onLoaded {
                    isVisibleProgressBarLiveData.postValue(false)
                }
            }
        }

        viewModelScope.launch {
            isVisibleProgressBarLiveData.postValue(true)

            rewardedAd.show()
        }
    }
}