package com.admob.coroutines.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.admob.coroutines.constant.BannerSize
import com.admob.coroutines.impl.admob.dsl.bannerAdView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        MainViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShowInterstitial.run {
            setOnClickListener {
                viewModel.onButtonShowInterstitialClick()
            }
        }

        btnShowRewarded.run {
            setOnClickListener {
                viewModel.onButtonShowRewardedClick()
            }
        }

        adLayout.run {
            addView(
                bannerAdView(applicationContext) {
                    size = BannerSize.BANNER
                }
            )
        }

        viewModel.getVisibleProgressBarLiveData().observe(this, Observer {
            btnShowInterstitial.run {
                isEnabled = !it
                isFocusable = !it
                isClickable = !it
            }

            btnShowRewarded.run {
                isEnabled = !it
                isFocusable = !it
                isClickable = !it
            }

            progressBar.run {
                visibility = if (it) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })
    }

    override fun onDestroy() {
        viewModel.getVisibleProgressBarLiveData().removeObservers(this)

        super.onDestroy()
    }
}
