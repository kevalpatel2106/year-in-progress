package com.kevalpatel2106.yip.settings

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.DebugActivity
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (BuildConfig.DEBUG) menuInflater.inflate(R.menu.menu_settings_debug, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_debug_drawer) DebugActivity.launch(this)
        return super.onOptionsItemSelected(item)
    }

    companion object {
        internal fun launch(context: Context) {
            context.startActivity(context.prepareLaunchIntent(SettingsActivity::class.java))
        }
    }
}
