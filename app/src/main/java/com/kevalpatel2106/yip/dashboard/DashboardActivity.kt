package com.kevalpatel2106.yip.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cocosw.bottomsheet.BottomSheet
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.openPlayStorePage
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.sendMailToDev
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.core.slideDown
import com.kevalpatel2106.yip.core.slideUp
import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapter
import com.kevalpatel2106.yip.databinding.ActivityDashboardBinding
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.edit.EditProgressActivity
import com.kevalpatel2106.yip.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_dashboard.add_progress_fab
import kotlinx.android.synthetic.main.activity_dashboard.bottom_app_bar
import kotlinx.android.synthetic.main.activity_dashboard.expandable_page_container
import kotlinx.android.synthetic.main.activity_dashboard.progress_list_rv
import me.saket.inboxrecyclerview.dimming.TintPainter
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks
import timber.log.Timber
import javax.inject.Inject

internal class DashboardActivity : AppCompatActivity() {

    private val bottomNavigationSheet: BottomSheet by lazy {
        BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
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

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: DashboardViewModel by lazy {
        provideViewModel(viewModelProvider, DashboardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@DashboardActivity)
        DataBindingUtil.setContentView<ActivityDashboardBinding>(this, R.layout.activity_dashboard)
            .apply {
                lifecycleOwner = this@DashboardActivity
                viewModel = model
            }

        setSupportActionBar(bottom_app_bar)
        setUpFab()
        setUpList()
        setUpInterstitialAd()

        model.userMessage.nullSafeObserve(this@DashboardActivity) { showSnack(it) }

        // Rating and ads section
        model.askForRating.nullSafeObserve(this@DashboardActivity) { showRatingDialog() }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val detailId = intent?.getLongExtra(ARG_PROGRESS_DETAIL_ID, -1) ?: -1
        if (detailId > 0) model.userWantsToOpenDetail(detailId)
    }

    private fun setUpInterstitialAd() {
        val interstitialAd = InterstitialAd(this).apply {
            adUnitId = getString(R.string.detail_interstitial_ad_id)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    show()
                }

                override fun onAdFailedToLoad(p0: Int) {
                    super.onAdFailedToLoad(p0)
                    Timber.i("The interstitial ad loading failed.")
                }
            }
        }
        model.showInterstitialAd.nullSafeObserve(this@DashboardActivity) {
            interstitialAd.loadAd(AdRequest.Builder().build())
        }
    }

    private fun setUpFab() {
        add_progress_fab.setOnClickListener {
            if (model.isDetailExpanded()) {
                model.expandProgress.value?.let { progressId ->
                    EditProgressActivity.edit(this@DashboardActivity, progressId)
                }
            } else {
                EditProgressActivity.createNew(this@DashboardActivity)
            }
        }
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
        progress_list_rv.tintPainter =
            TintPainter.uncoveredArea(color = Color.WHITE, opacity = 0.65F)
        progress_list_rv.setExpandablePage(expandable_page_container)

        val adapter = ProgressAdapter { model.userWantsToOpenDetail(it.id) }
        progress_list_rv.adapter = adapter

        // Start monitoring progress.
        model.progresses.nullSafeObserve(this@DashboardActivity) {
            adapter.submitList(it.toMutableList())
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
        private const val ARG_PROGRESS_DETAIL_ID = "progressId"

        internal fun launch(context: Context, progressId: Long = -1) =
            context.startActivity(launchIntent(context, progressId))

        internal fun launchIntent(context: Context, progressId: Long = -1): Intent =
            context.prepareLaunchIntent(DashboardActivity::class.java)
                .apply { putExtra(ARG_PROGRESS_DETAIL_ID, progressId) }
    }
}
