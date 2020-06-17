package com.kevalpatel2106.yip.settings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.core.showOrHide
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.settings.preferenceList.SettingsFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_settings.settings_ads_view
import kotlinx.android.synthetic.main.activity_settings.settings_toolbar
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {
    private val compositeDisposable = CompositeDisposable()

    @Inject
    internal lateinit var billingRepo: BillingRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@SettingsActivity)
        setToolbar()
        setAds()

        // Set preference
        supportFragmentManager.commit {
            replace(R.id.settings, SettingsFragment.getNewInstance())
        }
    }

    private fun setAds() {
        billingRepo.observeIsPurchased()
            .subscribe { isPro ->
                settings_ads_view.showOrHide(!isPro)
                if (!isPro) settings_ads_view.loadAd(AdRequest.Builder().build())
            }
            .addTo(compositeDisposable)
    }

    private fun setToolbar() {
        setSupportActionBar(settings_toolbar)
        supportActionBar?.set()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        internal fun launch(context: Context) {
            context.startActivity(context.prepareLaunchIntent(SettingsActivity::class.java))
        }
    }
}
