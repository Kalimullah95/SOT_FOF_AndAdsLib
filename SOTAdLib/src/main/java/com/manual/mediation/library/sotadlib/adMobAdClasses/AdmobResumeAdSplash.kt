package com.manual.mediation.library.sotadlib.adMobAdClasses

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.manual.mediation.library.sotadlib.BuildConfig
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.utils.AdLoadingDialog
import com.manual.mediation.library.sotadlib.utils.NetworkCheck

class AdmobResumeAdSplash(
    private val activity: Activity?,
    val adId: String,
    private val onAdDismissed: (() -> Unit)? = null,
    private val onAdFailed: (() -> Unit)? = null,
    private val onAdTimeout: (() -> Unit)? = null,
    private val onAdShowed: (() -> Unit)? = null
) {

    private var appOpenAd: AppOpenAd? = null
    private var isShowingDialog = false
    var isShowingAd = false

    private val timeoutHandler = Handler(Looper.getMainLooper())
    private val timeoutRunnable = Runnable {
        if (appOpenAd == null) {
            dismissWaitDialog()
            Log.i("SOT_ADS_TAG", "Admob: Resume : Timeout()")
            showDebugToast("OpenAd :: AdMob :: Timeout")
            onAdTimeout?.invoke()
        }
    }

    init {
        // Safe check to ensure Activity is valid before requesting ads
        activity?.takeIf { !it.isFinishing && !it.isDestroyed }?.let {
            fetchAd()
        } ?: run {
            onAdFailed?.invoke()
        }
    }

    private fun fetchAd() {
        if (isAdAvailable()) return

        if (activity == null || !NetworkCheck.isNetworkAvailable(activity)) {
            onAdFailed?.invoke()
            return
        }

        val loadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                Log.i("SOT_ADS_TAG", "Admob: Resume : onAdLoaded()")
                appOpenAd = ad
                timeoutHandler.removeCallbacks(timeoutRunnable) // Stop timeout timer immediately
                showAdIfAvailable()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.i("SOT_ADS_TAG", "Admob: Resume : onAdFailedToLoad()")
                timeoutHandler.removeCallbacks(timeoutRunnable)
                showDebugToast("OpenAd :: AdMob :: Failed to load")
                onAdFailed?.invoke()
            }
        }

        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity.applicationContext, adId, request, loadCallback)
        timeoutHandler.postDelayed(timeoutRunnable, 20000)
    }

    fun showAdIfAvailable() {
        if (isShowingAd || !isAdAvailable()) return

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.i("SOT_ADS_TAG", "Admob: Resume : onAdDismissedFullScreenContent()")
                dismissWaitDialog()
                appOpenAd = null
                isShowingAd = false
                showDebugToast("OpenAd :: AdMob :: onAdDismissedFullScreenContent()")
                onAdDismissed?.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.i("SOT_ADS_TAG", "Admob: Resume : onAdFailedToShowFullScreenContent()")
                dismissWaitDialog()
                appOpenAd = null
                isShowingAd = false
                showDebugToast("OpenAd :: AdMob :: Failed to show")
                onAdFailed?.invoke()
            }

            override fun onAdShowedFullScreenContent() {
                Log.i("SOT_ADS_TAG", "Admob: Resume : onAdShowedFullScreenContent()")
                isShowingAd = true
                dismissWaitDialog() // Dismiss dialog instantly when ad shows
                onAdShowed?.invoke()
            }
        }

        // Safe activity check before showing dialog and Ad
        activity?.takeIf { !it.isFinishing && !it.isDestroyed }?.let { safeActivity ->
            showWaitDialog(safeActivity)

            // OPTIMIZED: 7000ms loop removed. 1500ms reduced to 500ms.
            Handler(Looper.getMainLooper()).postDelayed({
                if (!safeActivity.isFinishing && !safeActivity.isDestroyed && appOpenAd != null) {
                    appOpenAd?.show(safeActivity)
                } else {
                    dismissWaitDialog()
                }
            }, 500)
        }
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

    private fun showWaitDialog(safeActivity: Activity) {
        Log.i("SOT_ADS_TAG", "Admob: Resume : showWaitDialog()")
        if (!isShowingDialog) {
            val view = safeActivity.layoutInflater.inflate(R.layout.dialog_adloading, null, false)
            isShowingDialog = true
            AdLoadingDialog.setContentView(safeActivity, view = view, isCancelable = false).showDialogInterstitial()
        }
    }

    private fun dismissWaitDialog() {
        Log.i("SOT_ADS_TAG", "Admob: Resume : dismissWaitDialog()")
        if (isShowingDialog) {
            activity?.takeIf { !it.isFinishing && !it.isDestroyed }?.let {
                AdLoadingDialog.dismissDialog(it)
            }
            isShowingDialog = false
        }
    }

    private fun showDebugToast(message: String) {
        activity?.takeIf { !it.isFinishing && BuildConfig.DEBUG }?.let { safeActivity ->
            Toast.makeText(safeActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}