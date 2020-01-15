# Admob Kotlin DSL
This library allows to show admob ad (Interstitial, Rewarded and Banner) easily. It based on Kotlin DSL features.

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
![](https://github.com/vetsb/admob-kotlin-dsl/tree/feature/readme/images/1.png)
![](https://github.com/vetsb/admob-kotlin-dsl/tree/feature/readme/images/2.png)
![](https://github.com/vetsb/admob-kotlin-dsl/tree/feature/readme/images/3.png)
![](https://github.com/vetsb/admob-kotlin-dsl/tree/feature/readme/images/4.png)
