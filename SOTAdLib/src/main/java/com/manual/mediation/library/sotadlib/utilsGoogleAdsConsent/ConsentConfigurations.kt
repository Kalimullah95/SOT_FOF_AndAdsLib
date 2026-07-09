package com.manual.mediation.library.sotadlib.utilsGoogleAdsConsent

import android.app.Activity
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.android.gms.ads.MobileAds
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import java.util.concurrent.atomic.AtomicBoolean

class ConsentConfigurations private constructor(
    private val activityContext: Activity,
    private val applicationContext: Application,
    private val testDeviceHashedIdList: ArrayList<String>,
    private val onConsentGathered: () -> Unit,
    // Passed from builder, ready to be used when you add Mintegral/Unity init logic
    private val mintegralAppKey: String,
    private val mintegralAppId: String,
    private val unityGameId: String,
    private val unityTestMode: Boolean
) {

    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private val isCallbackInvoked = AtomicBoolean(false) // Prevents multiple invocations

    // Explicitly bind to the Main Looper to prevent crashes
    private val slowInternetHandler = Handler(Looper.getMainLooper())

    init {
        consentInitializationSetup()
    }

    private fun consentInitializationSetup() {
        Log.i("ConsentMessage", "ConsentConfigurations: consentInitializationSetup called")

        // Timeout fallback
        slowInternetHandler.postDelayed({ safeInvokeCallback() }, 15000)

        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(activityContext)
        googleMobileAdsConsentManager.gatherConsent(
            activity = activityContext,
            testDeviceHashedIdList = testDeviceHashedIdList,
            removeSlowInternetCallBack = {
                Log.i("ConsentMessage", "ConsentConfigurations: removeSlowInternetCallBack")
                slowInternetHandler.removeCallbacksAndMessages(null)
            },
            errorMakingRequest = {
                Log.i("ConsentMessage", "ConsentConfigurations: errorMakingRequest")
                initializeMobileAdsSdk()
            },
            onConsentGatheringCompleteListener = { error ->
                if (googleMobileAdsConsentManager.canRequestAds) {
                    initializeMobileAdsSdk()
                } else if (error != null) {
                    Log.e("ConsentMessage", "ConsentConfigurations: error:: ${error.message}")
                    initializeMobileAdsSdk()
                }
            }
        )
    }

    private fun initializeMobileAdsSdk() {
        // Ensure we remove the timeout handler if initialization happens before 15 seconds
        slowInternetHandler.removeCallbacksAndMessages(null)

        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            Log.i("ConsentMessage", "initializeMobileAdsSdk() already called")
            safeInvokeCallback()
            return
        }

        if (NetworkCheck.isNetworkAvailable(activityContext)) {
            activityContext.getSharedPreferences("ConsentMessage", MODE_PRIVATE)
                .edit().putBoolean("FirstTime", true).apply()

            MobileAds.initialize(activityContext)

        }

        safeInvokeCallback()
    }

    /**
     * Ensures the onConsentGathered callback is fired exactly once,
     * whether from a network timeout or a successful initialization.
     */
    private fun safeInvokeCallback() {
        if (!isCallbackInvoked.getAndSet(true)) {
            onConsentGathered.invoke()
        }
    }

    class Builder {
        private lateinit var activityContext: Activity
        private lateinit var applicationContext: Application
        private var appKey: String = ""
        private var appId: String = ""
        private var gameId: String = ""
        private var testMode: Boolean = true
        private var testDeviceHashedIdList: ArrayList<String> = ArrayList()
        private lateinit var onConsentGathered: () -> Unit

        fun setApplicationContext(applicationContext: Application) = apply {
            this.applicationContext = applicationContext
        }

        fun setMintegralInitializationId(appKey: String, appId: String) = apply {
            this.appKey = appKey
            this.appId = appId
        }

        fun setUnityInitializationId(gameId: String, testMode: Boolean) = apply {
            this.gameId = gameId
            this.testMode = testMode
        }

        fun setActivityContext(activity: Activity) = apply {
            this.activityContext = activity
        }

        fun setTestDeviceHashedIdList(ids: ArrayList<String>) = apply {
            this.testDeviceHashedIdList = ids
        }

        fun setOnConsentGatheredCallback(callback: () -> Unit) = apply {
            this.onConsentGathered = callback
        }

        fun build(): ConsentConfigurations {
            require(::activityContext.isInitialized) { "Activity context must be provided" }
            require(::applicationContext.isInitialized) { "Application context must be provided" }
            require(::onConsentGathered.isInitialized) { "OnConsentGathered callback must be provided" }

            return ConsentConfigurations(
                activityContext,
                applicationContext,
                testDeviceHashedIdList,
                onConsentGathered,
                appKey,
                appId,
                gameId,
                testMode
            )
        }
    }
}

/*
class ConsentConfigurations private constructor(
    private val activityContext: Activity,
    private val applicationContext: Application,
    private val testDeviceHashedIdList: ArrayList<String>,
    private val onConsentGathered: () -> Unit) {

    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private val slowInternetHandler = Handler()

    init {
        consentInitializationSetup()
    }

    private fun consentInitializationSetup() {
        Log.i("ConsentMessage", "ConsentConfigurations: consentInitializationSetup called")
        slowInternetHandler.postDelayed(kotlinx.coroutines.Runnable { onConsentGathered.invoke() },15000)

        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(activityContext)
        googleMobileAdsConsentManager.gatherConsent(
            activity = activityContext,
            testDeviceHashedIdList = testDeviceHashedIdList,
            removeSlowInternetCallBack = {
            Log.i("ConsentMessage", "ConsentConfigurations: removeSlowInternetCallBack")
            slowInternetHandler.removeCallbacksAndMessages(null)
            },
            errorMakingRequest = {
                Log.i("ConsentMessage","ConsentConfigurations: ")
                    initializeMobileAdsSdk(initializeMobileAds = {
                        onConsentGathered.invoke()
                    })
            },
            onConsentGatheringCompleteListener = { error ->
                if (googleMobileAdsConsentManager.canRequestAds) {
                    initializeMobileAdsSdk(initializeMobileAds = {
                        onConsentGathered.invoke()
                    })
                } else {
                    if (error != null) {
                        Log.i("ConsentMessage","ConsentConfigurations: error:: "+error.message)
                            initializeMobileAdsSdk(initializeMobileAds = {
                                onConsentGathered.invoke()
                            })
                    }
                }
            })


    }

    private fun initializeMobileAdsSdk(initializeMobileAds: () -> Unit) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            Log.i("ConsentMessage","initializeMobileAdsSdk()")
            initializeMobileAds.invoke()
            return
        }
        Log.i("ConsentMessage","initializeMobileAdsSdk(): rem")
        if (NetworkCheck.isNetworkAvailable(activityContext)) {
            activityContext.getSharedPreferences("ConsentMessage", MODE_PRIVATE).edit().putBoolean("FirstTime", true).apply()
            MobileAds.initialize(activityContext)
            AdSettings.addTestDevice("0984fdbc-e473-40e8-91f5-b6b46ebc85b5")
            AdSettings.addTestDevice("240faf54-381a-4269-bbc6-713aed8a4b4b")
            AdSettings.addTestDevice("0f01a5f6-802a-4743-ae14-8e6a7a360965")
            AdSettings.addTestDevice("bba88f94-ecc3-4c56-bac8-8683f76946f9")
            AdSettings.addTestDevice("67e557c7-c6ee-4209-9e84-7e5b60546400")
            AdSettings.addTestDevice("937cc986-d628-450b-ae61-f6ad32e3b6a2")
            // AudienceNetworkAds.initialize(activityContext)
        }
        initializeMobileAds.invoke()
        slowInternetHandler.removeCallbacksAndMessages(null)
    }

    class Builder {
        private lateinit var activityContext: Activity
        private lateinit var applicationContext: Application
        private var appKey: String = ""
        private var appId: String = ""
        private var gameId: String = ""
        private var testMode: Boolean = true
        private var testDeviceHashedIdList: ArrayList<String> = ArrayList()
        private lateinit var onConsentGathered: () -> Unit

        fun setApplicationContext(applicationContext: Application) = apply {
            this.applicationContext = applicationContext
        }

        fun setMintegralInitializationId(appKey: String, appId: String) = apply {
            this.appKey = appKey
            this.appId = appId
        }

        fun setUnityInitializationId(gameId: String, testMode: Boolean) = apply {
            this.gameId = gameId
            this.testMode = testMode
        }

        fun setActivityContext(activity: Activity) = apply {
            this.activityContext = activity
        }

        fun setTestDeviceHashedIdList(ids: ArrayList<String>) = apply {
            this.testDeviceHashedIdList = ids
        }

        fun setOnConsentGatheredCallback(callback: () -> Unit) = apply {
            this.onConsentGathered = callback
        }

        fun build(): ConsentConfigurations {
            if (!::activityContext.isInitialized) {
                throw IllegalStateException("Activity context must be provided")
            }
            if (!::onConsentGathered.isInitialized) {
                throw IllegalStateException("OnConsentGathered callback must be provided")
            }
            return ConsentConfigurations(activityContext, applicationContext,  testDeviceHashedIdList, onConsentGathered)
        }
    }
}
*/
