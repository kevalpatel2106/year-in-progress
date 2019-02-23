package com.kevalpatel2106.yip.dashboard

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cocosw.bottomsheet.BottomSheet
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.*
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapter
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.edit.EditProgressActivity
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import com.kevalpatel2106.yip.settings.SettingsActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_dashboard.*
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks
import timber.log.Timber
import javax.inject.Inject


class DashboardActivity : AppCompatActivity() {
    private lateinit var bottomNavigationSheet: BottomSheet

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    internal lateinit var adapter: Lazy<ProgressAdapter>

    @Inject
    internal lateinit var sharedPrefsProvider: SharedPrefsProvider

    private lateinit var model: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(bottom_app_bar)

        // Inject dependency
        getAppComponent().inject(this@DashboardActivity)
        model = provideViewModel(viewModelProvider, DashboardViewModel::class.java)

        setUpBottomNavigation()
        setUpFab()
        setUpList()
        setUpAd()

        // Start monitoring progress.
        model.progresses.nullSafeObserve(this@DashboardActivity) { adapter.get().submitList(it.toMutableList()) }

        // Rating and ads section
        model.askForRating.nullSafeObserve(this@DashboardActivity) { showRatingDialog() }
    }

    private fun setUpAd() {
        val interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.detail_interstitial_ad_id)
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                interstitialAd.show()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                Timber.i("The interstitial ad loading failed.")
            }
        }

        model.showAd.nullSafeObserve(this@DashboardActivity) {
            interstitialAd.loadAd(AdRequest.Builder().build())
        }
    }

    private fun setUpFab() {
        add_progress_fab.setOnClickListener {
            if (model.isDetailExpanded()) {
                EditProgressActivity.edit(this@DashboardActivity, model.expandProgress.value!!)
            } else {
                EditProgressActivity.createNew(this@DashboardActivity)
            }
        }
    }

    private fun setUpBottomNavigation() {
        bottomNavigationSheet = BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
                .title(R.string.application_name)
                .sheet(R.menu.menu_botom_sheet)
                .listener { _, which ->
                    when (which) {
                        R.id.menu_settings -> SettingsActivity.launch(this@DashboardActivity)
                        R.id.menu_send_feedback -> sendMailToDev()
                        R.id.menu_rate_us -> openPlayStorePage()
                    }
                }
                .build()
    }

    private fun setUpList() {
        expandable_page_container.addStateChangeCallbacks(object : PageStateChangeCallbacks {
            override fun onPageAboutToCollapse(collapseAnimDuration: Long) {
                bottom_app_bar.slideUp(collapseAnimDuration)
            }

            override fun onPageAboutToExpand(expandAnimDuration: Long) {
                bottom_app_bar.slideDown(expandAnimDuration)
            }

            override fun onPageCollapsed() {
                add_progress_fab.setImageResource(com.kevalpatel2106.yip.R.drawable.ic_add)
                model.expandProgress.value = -1
            }

            override fun onPageExpanded() {
                add_progress_fab.setImageResource(com.kevalpatel2106.yip.R.drawable.ic_edit)
            }
        })
        progress_list_rv.layoutManager = LinearLayoutManager(this@DashboardActivity)
        progress_list_rv.setExpandablePage(expandable_page_container)
        progress_list_rv.adapter = adapter.get().apply {
            this.setHasStableIds(true)
            this.clickListener = { model.userWantsToOpenDetail(it) }
        }

        // Expand detail
        model.expandProgress.nullSafeObserve(this@DashboardActivity) {
            if (it < 0L) return@nullSafeObserve

            supportFragmentManager.commit {
                replace(R.id.expandable_page_container, DetailFragment.newInstance(it))
            }
            progress_list_rv.expandItem(it)
        }
    }

    internal fun collapseDetail() {
        progress_list_rv.collapse()
        model.expandProgress.value = -1
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> if (!bottomNavigationSheet.isShowing) {
                bottomNavigationSheet.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            model.isDetailExpanded() -> collapseDetail()
            bottomNavigationSheet.isShowing -> bottomNavigationSheet.hide()
            else -> super.onBackPressed()
        }
    }

    private fun showRatingDialog() {
        AlertDialog.Builder(this@DashboardActivity, R.style.AppTheme_Dialog_Alert)
                .setTitle(R.string.rate_us_dialog_title)
                .setMessage(R.string.rate_us_dialog_message)
                .setPositiveButton(R.string.rate_us_dialog_positive_btn) { _, _ -> model.userWantsToRateNow() }
                .setNegativeButton(R.string.rate_us_dialog_negative_btn, null)
                .setCancelable(false)
                .show()
    }

    companion object {
        fun launch(context: Context) = context.startActivity(context.prepareLaunchIntent(DashboardActivity::class.java))
    }
}
