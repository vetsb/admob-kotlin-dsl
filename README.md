# Admob Kotlin DSL
This library allows to show admob ad (Interstitial, Rewarded and Banner) easily. It based on Kotlin DSL features and Coroutines.

## Sample
### Banner Ad View
```kotlin
bannerAdView(applicationContext) {
	size = BannerSize.BANNER
}
```
### Interstitial Ad
```kotlin
interstitialAd(applicationContext) {
	addListeners {
		onFailedToLoad { errorCode ->
			// ...
		}

		onLoaded {
			// ...
		}

		onOpened {
			// ...
		}
	}
}.show()
```
### Rewarded Ad
```kotlin
rewardedAd(applicationContext) {
	addListeners {
		onFailedToLoad { errorCode ->
			// ...
		}

		onLoaded {
			// ...
		}

		onOpened {
			// ...
		}
	}
}.show()
```

## Documentation
![](https://raw.githubusercontent.com/vetsb/admob-kotlin-dsl/master/images/1.png)
![](https://raw.githubusercontent.com/vetsb/admob-kotlin-dsl/master/images/2.png)
![](https://raw.githubusercontent.com/vetsb/admob-kotlin-dsl/master/images/3.png)
![](https://raw.githubusercontent.com/vetsb/admob-kotlin-dsl/master/images/4.png)
