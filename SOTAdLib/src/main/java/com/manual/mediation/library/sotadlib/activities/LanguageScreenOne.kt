package com.manual.mediation.library.sotadlib.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.adapters.LanguageAdapter
import com.manual.mediation.library.sotadlib.callingClasses.LanguageScreensConfiguration
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.interfaces.LanguageInterface
import com.manual.mediation.library.sotadlib.utils.hideSystemUIUpdated

class LanguageScreenOne : AppCompatBaseActivity(), LanguageInterface {

    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var recyclerView: RecyclerView
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        supportActionBar?.hide()
        hideSystemUIUpdated()
        setContentView(R.layout.language_screen_one)
        Log.i("SOTStartTestActivity", "language1_scr")

        recyclerView = findViewById(R.id.recyclerViewLanguage)
        recyclerView.layoutManager = LinearLayoutManager(this)

        Log.i("LanguageScreenOne", "Language: onCreate")

        LanguageScreensConfiguration.languageInstance?.let { config ->
            config.setLanguageInterface(this)

            config.languageList?.let {
                if (config.selectedDrawable != null && config.unSelectedDrawable != null) {
                    if (config.selectedRadio != null && config.unSelectedRadio != null) {
                        languageAdapter = LanguageAdapter(
                            ctx = this,
                            languages = config.languageList!!,
                            selectedDrawable = config.selectedDrawable!!,
                            unSelectedDrawable = config.unSelectedDrawable!!,
                            selectedRadio = config.selectedRadio!!,
                            unSelectedRadio = config.unSelectedRadio!!) {
                            config.showLanguageTwoScreen()
                        }
                        recyclerView.adapter = languageAdapter
                    }
                }
            }
        }


    }




    override fun showLanguageTwoScreen() {
        if (!isFinishing && !isDestroyed) {
            try {
                val intent = Intent(this, LanguageScreenDup::class.java)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                    overridePendingTransition(0, 0)
                } else {
                    Log.e("LanguageScreenOne", "No activity found to handle intent")
                }
            } catch (e: Exception) {
                Log.e("LanguageScreenOne", "Error starting activity", e)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val nativeLanguage1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_1") as? Boolean ?: false
        if (nativeLanguage1Enabled) {
            showAdmobLanguageScreenOneNatives()
        } else {
            findViewById<CardView>(R.id.nativeAdContainerAd)?.let {
                findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
            }
        }
    }

    private fun showAdmobLanguageScreenOneNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_LANGUAGE_1")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_LANGUAGE_1",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1").toString().toBoolean(),
                populateView = true,
                adContainer = findViewById(R.id.nativeAdContainerAd),
                onAdFailed = {
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("LanguageScreenOne", "Language: onAdFailed()")
                },
                onAdLoaded = {
                    Log.i("LanguageScreenOne", "Language: onAdLoaded()")
                }
            )
        } ?: Log.w("LanguageScreenOne", "ADMOB_NATIVE_LANGUAGE_1 ad ID is missing.")
    }


}