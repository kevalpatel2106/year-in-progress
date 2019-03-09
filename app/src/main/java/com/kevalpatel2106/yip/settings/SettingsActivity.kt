package com.kevalpatel2106.yip.settings

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.showOrHide
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_settings.settings_ads_view
import kotlinx.android.synthetic.main.activity_settings.settings_toolbar
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()

    @Inject
    internal lateinit var billingRepo: BillingRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getAppComponent().inject(this@SettingsActivity)

        setContentView(R.layout.activity_settings)
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        internal fun launch(context: Context) {
            context.startActivity(context.prepareLaunchIntent(SettingsActivity::class.java))
        }
    }
}