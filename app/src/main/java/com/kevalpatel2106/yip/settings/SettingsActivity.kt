package com.kevalpatel2106.yip.settings

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.edit.EditViewProgressModel
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.settings_activity.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // Set title
        setSupportActionBar(settings_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        BillingRepo.isPurchased
            .subscribe { isPro ->
                if (isPro) {
                    settings_ads_view.visibility = View.GONE
                } else {
                    settings_ads_view.visibility = View.VISIBLE
                    settings_ads_view.loadAd(AdRequest.Builder().build())
                }
            }
            .addTo(compositeDisposable)

        // Set preference
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment.getNewInstance())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun launch(context: Context) = context.startActivity(context.prepareLaunchIntent(SettingsActivity::class.java))
    }
}