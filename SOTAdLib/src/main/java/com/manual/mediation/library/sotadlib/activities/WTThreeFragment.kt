package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdMobInterstitialInside
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.databinding.FragmentWTThreeBinding
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.manual.mediation.library.sotadlib.utils.PrefHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WTThreeFragment : Fragment() {
    private lateinit var binding: FragmentWTThreeBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private lateinit var item: WalkThroughItem

    companion object {
        private const val ARG_ITEM = "walkThroughItem"

        fun newInstance(item: WalkThroughItem): WTThreeFragment {
            return WTThreeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ITEM, item)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getParcelable(ARG_ITEM)
            ?: throw IllegalStateException("WalkThroughItem must be provided")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWTThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()

        Log.i("SOTStartTestActivity", "walkthrough3_scr")

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                context?.let {
                    Glide.with(it)
                        .asDrawable()
                        .load(item.drawableResId)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .skipMemoryCache(true)
                        .into(binding.main)
                }

            }
            lifecycleScope.launch {
                context?.let {
                    Glide.with(it)
                        .asDrawable()
                        .load(item.drawableBubbleResId)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .skipMemoryCache(true)
                        .into(binding.bubble)
                }
            }
        }


        context?.let {
            binding.txtHeading.setTextColor(ContextCompat.getColor(it, item.headingColor))
            binding.txtDescription.setTextColor(ContextCompat.getColor(it, item.descriptionColor))
            binding.btnNext.setTextColor(ContextCompat.getColor(it, item.nextColor))

        }


        binding.txtHeading.text = item.heading
        binding.txtDescription.text = item.description

        val interstitialLetsStartEnabled = sotAdsConfigurations?.getRemoteConfigData()?.get("INTERSTITIAL_LETS_START") as? Boolean ?: false

        binding.btnNext.setOnClickListener {

            if (interstitialLetsStartEnabled) {
                showAdmobWTThreeInterstitial()
            } else {

                letsStartClick()
            }
        }
    }



    private fun showAdmobWTThreeInterstitial() {
        activity?.let { safeActivity ->
            AdMobInterstitialInside.showIfAvailableOrLoadAdMobInterstitial(
                context = safeActivity,
                nameFragment = "WALKTHROUGH_3",
                adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_INTERSTITIAL_LETS_START")!!,
                onAdClosedCallBackAdmob = {
                    Log.i("SOT_ADS_TAG", "Interstitial : WALKTHROUGH_3 : onAdClosedCallBackAdmob()")
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(300)
                        if (isAdded && activity != null) {
                            letsStartClick()
                        }
                    }

                },
                onAdShowedCallBackAdmob = {
                    Log.i("SOT_ADS_TAG", "Interstitial : WALKTHROUGH_3 : onAdShowedCallBackAdmob()")
                }
            )
        }
    }


    private fun letsStartClick() {
        Log.i("SOTStartTestActivity", "walkthrough3_scr_tap_start")
        val safeActivity = activity ?: return
        PrefHelper(safeActivity).putBoolean("StartScreens", value = true)
        SOTAdsManager.notifyFlowFinished()
        safeActivity.finish()
    }


    override fun onResume() {
        super.onResume()
        if (!NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.8f)
            binding.nativeAdContainerAd.visibility = View.GONE
        }


        val nativeWalkThrough3Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_3") as? Boolean ?: false
        if (nativeWalkThrough3Enabled) {
            showAdmobWTThreeNatives()
        } else {
            binding.nativeAdContainerAd.visibility = View.GONE
        }
    }


    private fun showAdmobWTThreeNatives() {
        val safeActivity = activity ?: return

        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_3")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = safeActivity,
                adId = adId,
                adName = "WALKTHROUGH_3",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations
                    ?.getRemoteConfigData()
                    ?.getValue("NATIVE_WALKTHROUGH_3")
                    .toString()
                    .toBoolean(),
                populateView = true,
                adContainer = binding.nativeAdContainerAd,
                onAdFailed = {
                    binding.nativeAdContainerAd.visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_3: Admob: onAdFailed()")
                },
                onAdLoaded = {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_3: Admob: onAdLoaded()")
                }
            )
        }
    }
}