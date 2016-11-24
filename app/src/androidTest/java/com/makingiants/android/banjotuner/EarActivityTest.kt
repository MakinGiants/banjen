package com.makingiants.android.banjotuner

import android.support.test.filters.MediumTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class EarActivityTest {

    @Rule @JvmField
    var mActivityRule: ActivityTestRule<EarActivity> = ActivityTestRule(EarActivity::class.java)

    @Test
    fun test_onClick_ifUnselected_playSound() {
        test(mActivityRule.activity) {
            click(1)
        }.isPlaying()
    }

    @Test
    fun test_onClick_ifSelected_stopSound() {
        test(mActivityRule.activity) {
            click(1)
            click(1)
        }.isNotPlaying()
    }
}

