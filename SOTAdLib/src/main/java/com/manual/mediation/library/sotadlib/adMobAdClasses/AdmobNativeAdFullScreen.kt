package com.manual.mediation.library.sotadlib.adMobAdClasses

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.utils.NetworkCheck


object AdmobNativeAdFullScreen {
    private val nativeAdCache = HashMap<String, NativeAd?>()
    private val adLoadingState = HashMap<String, Boolean>()

    fun requestAd(
        mContext: Activity?,
        adId: String,
        adName: String = "",
        remoteConfig: Boolean = true,
        populateView: Boolean = false,
        adContainer: CardView? = null,
        onAdFailed: (() -> Unit)? = null,
        onAdLoaded: (() -> Unit)? = null
    ) {
        if (mContext == null || !remoteConfig) {
            onAdFailed?.invoke()
            return
        }

        if (populateView && nativeAdCache[adName] != null) {
            Log.i("SOT_ADS_TAG", "Native: Admob: $adName showing from cache.")
            showCachedAd(adName, adContainer)
            onAdLoaded?.invoke()
            return
        }

        if (!populateView && (nativeAdCache[adName] != null || adLoadingState[adName] == true)) {
            Log.i("SOT_ADS_TAG", "Native: Admob: $adName already cached or loading. Skipping.")
            return
        }

        if (!NetworkCheck.isNetworkAvailable(mContext)) {
            onAdFailed?.invoke()
            return
        }

        adLoadingState[adName] = true
        Log.i("SOT_ADS_TAG", "Native: Admob: $adName starting network request...")

        val adLoader = AdLoader.Builder(mContext, adId)
            .forNativeAd { nativeAd ->
                nativeAdCache[adName] = nativeAd
                adLoadingState[adName] = false
                Log.i("SOT_ADS_TAG", "Native: Admob: $adName loaded successfully.")

                if (populateView) {
                    showCachedAd(adName, adContainer)
                }
                onAdLoaded?.invoke()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: LoadAdError) {
                    nativeAdCache[adName] = null
                    adLoadingState[adName] = false
                    Log.e("SOT_ADS_TAG", "Native: Admob: $adName failed: $errorCode")
                    onAdFailed?.invoke()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    // Optional: Clear cache on click if you want a fresh ad next time
                    // nativeAdCache[adName] = null
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun showCachedAd(adName: String, adContainer: CardView?) {
        val cachedAd = nativeAdCache[adName]
        val adView = adContainer?.findViewById<NativeAdView>(R.id.nativeAdViewAdmob)

        if (cachedAd != null && adView != null) {
            populateNativeAd(cachedAd, adView)
            adContainer.visibility = View.VISIBLE
        } else {
            Log.e("SOT_ADS_TAG", "Native: Admob: Cannot show $adName. Ad or View is null.")
        }
    }

    private fun populateNativeAd(nativeAd: NativeAd, adView: NativeAdView) {
        adView.headlineView = adView.findViewById(R.id.adHeadline)
        adView.bodyView = adView.findViewById(R.id.adBody)
        adView.callToActionView = adView.findViewById(R.id.adCallToAction)
        adView.iconView = adView.findViewById(R.id.adAppIcon)

        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.body == null) {
            adView.bodyView?.visibility = View.INVISIBLE
        } else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        val iconCard = adView.findViewById<CardView>(R.id.adIconCard)
        if (nativeAd.icon == null) {
            iconCard?.visibility = View.GONE
        } else {
            iconCard?.visibility = View.VISIBLE
            (adView.iconView as ImageView).setImageDrawable(nativeAd.icon!!.drawable)
        }

        configureMediaView(nativeAd, adView)
        adView.setNativeAd(nativeAd)
    }

    private fun configureMediaView(nativeAd: NativeAd, adView: NativeAdView) {
        val mediaView = adView.findViewById<View>(R.id.adMedia) as? MediaView
        if (mediaView != null && nativeAd.mediaContent != null) {
            adView.mediaView = mediaView
            adView.mediaView?.mediaContent = nativeAd.mediaContent!!
        }
    }
    fun clearAdCache() {
        try {
            nativeAdCache.values.forEach { it?.destroy() }
            nativeAdCache.clear()
            adLoadingState.clear()

            Log.i("SOT_ADS_TAG", "Native Ad Cache: All ads destroyed and maps cleared.")
        } catch (e: Exception) {
            Log.e("SOT_ADS_TAG", "Error clearing ad cache: ${e.message}")
        }
    }
}

