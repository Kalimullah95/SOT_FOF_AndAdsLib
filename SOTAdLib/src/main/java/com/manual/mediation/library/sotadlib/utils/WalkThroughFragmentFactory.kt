package com.manual.mediation.library.sotadlib.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.manual.mediation.library.sotadlib.activities.WTFullScreenAdFragment
import com.manual.mediation.library.sotadlib.activities.WTOneFragment
import com.manual.mediation.library.sotadlib.activities.WTThreeFragment
import com.manual.mediation.library.sotadlib.activities.WTTwoFragment
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.interfaces.CommonEventTracker

class WalkThroughFragmentFactory(
    private val walkThroughItems: ArrayList<WalkThroughItem>,
    private val eventTracker: CommonEventTracker? = null,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            // Use getOrNull to prevent IndexOutOfBoundsException
            WTOneFragment::class.java.name -> {
                val data = walkThroughItems.getOrNull(0) ?: WalkThroughItem()
                WTOneFragment.newInstance(data, eventTracker)
            }
            WTTwoFragment::class.java.name -> {
                val data = walkThroughItems.getOrNull(1) ?: WalkThroughItem()
                WTTwoFragment.newInstance(data, eventTracker)
            }
            WTThreeFragment::class.java.name -> {
                val data = walkThroughItems.getOrNull(2) ?: WalkThroughItem()
                WTThreeFragment.newInstance(data, eventTracker)
            }
            WTFullScreenAdFragment::class.java.name -> {
                WTFullScreenAdFragment.newInstance(eventTracker)
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}

/*
class WalkThroughFragmentFactory(
    private val walkThroughItems: ArrayList<WalkThroughItem>,
    private val eventTracker: CommonEventTracker? = null,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            WTOneFragment::class.java.name -> WTOneFragment.newInstance(walkThroughItems[0], eventTracker)
            WTTwoFragment::class.java.name -> WTTwoFragment.newInstance(walkThroughItems[1],eventTracker)
            WTThreeFragment::class.java.name -> WTThreeFragment.newInstance(walkThroughItems[2],eventTracker)
            WTFullScreenAdFragment::class.java.name -> WTFullScreenAdFragment.newInstance(eventTracker)
            else -> super.instantiate(classLoader, className)
        }
    }
}*/
