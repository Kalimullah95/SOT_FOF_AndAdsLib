package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.adapters.LanguageAdapter
import com.manual.mediation.library.sotadlib.callingClasses.LanguageScreensConfiguration
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.utils.MyLocaleHelper
import com.manual.mediation.library.sotadlib.utils.hideSystemUIUpdated

class LanguageScreenDup: AppCompatBaseActivity() {

    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imvDone: AppCompatImageView
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        supportActionBar?.hide()
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        hideSystemUIUpdated()
        setContentView(R.layout.language_screen_dup)
        Log.i("SOTStartTestActivity", "language2_scr")
        imvDone = findViewById(R.id.imvDone)
        recyclerView = findViewById(R.id.recyclerViewLanguage)
        recyclerView.layoutManager = LinearLayoutManager(this)

        LanguageScreensConfiguration.languageInstance?.let { config ->
            config.languageList?.let { languageList ->
                config.selectedDrawable?.let { selectedDrawable ->
                    config.unSelectedDrawable?.let { unSelectedDrawable ->
                        config.selectedRadio?.let { selectedRadio ->
                            config.unSelectedRadio?.let { unSelectedRadio ->
                                languageAdapter = LanguageAdapter(
                                    ctx = this,
                                    languages = languageList,
                                    selectedDrawable = selectedDrawable,
                                    unSelectedDrawable = unSelectedDrawable,
                                    selectedRadio = selectedRadio,
                                    unSelectedRadio = unSelectedRadio
                                ) {
                                        position ->
                                    // Handle selection changes
                                    val language = languageList[position]
                                    MyLocaleHelper.setLocale(this, language.code)
                                    //recreate()
                                }
                                recyclerView.adapter = languageAdapter
                            }
                        }
                    }
                }
            }
        }

        imvDone.setOnClickListener {
            Log.i("SOTStartTestActivity", "language2_scr_tap_language")
            intent?.let {
                if (it.getStringExtra("From").equals("AppSettings")) {
                    finish()
                } else {
                    SOTAdsManager.showWelcomeScreen()
                    finish()
                }
            }
        }


        if (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_1") as? Boolean == true) {
            loadAdmobSurveyOneNatives()
        }

        val nativeSurvey2Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_2") as? Boolean ?: false
        if (nativeSurvey2Enabled) {
            loadAdmobSurveyDupNatives()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                intent?.let {
//                    if (it.getStringExtra("From").equals("AppSettings")) {
                        finish()
//                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
        if (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_2") as? Boolean == true) {
            findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
            showAdmobLanguageScreenDupNatives()
        } else {
            findViewById<CardView>(R.id.nativeAdContainerAd)?.let {
                findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
            }
        }
    }

    private fun showAdmobLanguageScreenDupNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_LANGUAGE_2")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_LANGUAGE_2",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_2").toString().toBoolean(),
                populateView = true,
                adContainer = findViewById(R.id.nativeAdContainerAd),
                onAdFailed = {
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "LanguageScreenDup: Admob onAdFailed()")
                },
                onAdLoaded = {
                    Log.i("SOT_ADS_TAG", "LanguageScreenDup: Admob onAdLoaded()")
                }
            )
        } ?: Log.w("SOT_ADS_TAG", "ADMOB_NATIVE_LANGUAGE_2 ad ID is missing.")
    }



    private fun loadAdmobSurveyOneNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_1")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_SURVEY_1",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.w("SOT_ADS_TAG", "ADMOB_NATIVE_SURVEY_1 ad ID is missing.")
        }
    }


    private fun loadAdmobSurveyDupNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_2")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_SURVEY_2",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG", "Admob ad ID not found for NATIVE_SURVEY_2")
        }
    }

}