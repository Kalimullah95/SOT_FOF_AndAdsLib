package com.manual.mediation.library.sotadlib.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.callingClasses.WelcomeScreensConfiguration
import com.manual.mediation.library.sotadlib.interfaces.WelcomeInterface
import com.manual.mediation.library.sotadlib.objects.StatusBarColor
import com.manual.mediation.library.sotadlib.utils.hideSystemUIUpdated
import com.manual.mediation.library.sotadlib.utils.setStatusBarColor

class WelcomeScreenOne : AppCompatBaseActivity(), WelcomeInterface {

    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private var myView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        hideSystemUIUpdated()
        onBackPressedDispatcher.addCallback(this) {
            /**Disable backPress until Home**/
        }
        setStatusBarColor(StatusBarColor.getColor())
        sotAdsConfigurations = SOTAdsManager.getConfigurations()

        WelcomeScreensConfiguration.welcomeInstance?.let { config ->
            config.setWelcomeInterface(this)
            myView = config.view
            myView?.parent?.let { parent ->
                if (parent is ViewGroup) {
                    parent.removeView(myView)
                }
            }
            setContentView(myView)
        }

        val nativeWalkThrough1Enabled = (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_1") as? Boolean ?: false) && (sotAdsConfigurations?.getRemoteConfigData()?.get("IS_PURCHASED") as? Boolean == false)
        if (nativeWalkThrough1Enabled) {
            loadAdmobWTOneNatives()
        }

    }

    override fun onResume() {
        super.onResume()
        val nativeSurvey1Enabled = (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_1") as? Boolean ?: false) && (sotAdsConfigurations?.getRemoteConfigData()?.get("IS_PURCHASED") as? Boolean == false)
        if (nativeSurvey1Enabled) {
            showAdmobSurveyOneNatives()
        } else {
            myView?.let {
                myView?.findViewById<CardView>(R.id.nativeAdContainerAdmob)?.visibility = View.GONE
                myView?.findViewById<CardView>(R.id.nativeAdContainerMintegral)?.visibility = View.GONE
            }
        }
    }

    override fun showWelcomeTwoScreen() {
        WelcomeScreensConfiguration.welcomeInstance?.setWelcomeInterface(null)
        try {
            if (!isFinishing && !isDestroyed) {
                startActivity(Intent(this, WelcomeScreenDup::class.java), ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                finish()

            }
        } catch (e: Exception) {

        }
    }

    private fun showAdmobSurveyOneNatives() {
        myView?.let {
            myView?.findViewById<CardView>(R.id.nativeAdContainerMintegral)?.visibility = View.GONE
            myView?.findViewById<CardView>(R.id.nativeAdContainerAdmob)?.visibility = View.VISIBLE
            sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_1")?.let { adId ->
                AdmobNativeAdManager.requestAd(
                    mContext = this,
                    adId = adId,
                    adName = "NATIVE_SURVEY_1",
                    isMedia = true,
                    isMediumAd = true,
                    remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1").toString().toBoolean(),
                    populateView = true,
                    requestAgain = false,
                    adContainer = myView?.findViewById(R.id.nativeAdContainerAdmob),
                    onAdFailed = {
                        myView?.findViewById<CardView>(R.id.nativeAdContainerAdmob)?.visibility = View.GONE
                        Log.i("SOT_ADS_TAG","WelcomeScreenOne: Admob: onAdFailed()")
                    },
                    onAdLoaded = {
                        Log.i("SOT_ADS_TAG","WelcomeScreenOne: Admob: onAdLoaded()")
                    }
                )
            } ?: Log.w("WelcomeScreenOne", "ADMOB_NATIVE_SURVEY_1 ad ID is missing.")
        }
    }
    private fun loadAdmobWTOneNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_1")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "WALKTHROUGH_1",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG","Admob ad ID not found for WALKTHROUGH_1")
        }
    }

}