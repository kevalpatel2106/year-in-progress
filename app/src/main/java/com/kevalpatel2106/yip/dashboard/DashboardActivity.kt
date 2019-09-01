package com.kevalpatel2106.yip.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.cocosw.bottomsheet.BottomSheet
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.core.slideDown
import com.kevalpatel2106.yip.core.slideUp
import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapter
import com.kevalpatel2106.yip.databinding.ActivityDashboardBinding
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.edit.EditProgressActivity
import kotlinx.android.synthetic.main.activity_dashboard.add_progress_fab
import kotlinx.android.synthetic.main.activity_dashboard.bottom_app_bar
import kotlinx.android.synthetic.main.activity_dashboard.expandable_page_container
import kotlinx.android.synthetic.main.activity_dashboard.progress_list_rv
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks
import javax.inject.Inject

internal class DashboardActivity : AppCompatActivity() {

    private val bottomNavigationSheet: BottomSheet by lazy { prepareBottomSheetMenu() }

    private val pageStateChangeCallbacks = object : PageStateChangeCallbacks {
        override fun onPageAboutToCollapse(collapseAnimDuration: Long) {
            bottom_app_bar.slideUp(collapseAnimDuration)
        }

        override fun onPageAboutToExpand(expandAnimDuration: Long) {
            bottom_app_bar.slideDown(expandAnimDuration)
        }

        override fun onPageCollapsed() {
            add_progress_fab.setImageResource(R.drawable.ic_add)
            model.resetExpandedProgress()
        }

        override fun onPageExpanded() {
            add_progress_fab.setImageResource(R.drawable.ic_edit)
        }
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

        model.userMessages.nullSafeObserve(this@DashboardActivity) { showSnack(it) }

        // Rating and ads section
        model.askForRatingSignal.nullSafeObserve(this@DashboardActivity) {
            showRatingDialog({ model.userWantsToRateNow() }, { model.userWantsToNeverRate() })
        }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val detailId = intent?.getLongExtra(ARG_PROGRESS_DETAIL_ID, -1) ?: -1
        if (detailId > 0) model.userWantsToOpenDetail(detailId)
    }

    private fun setUpInterstitialAd() {
        val interstitialAd = prepareInterstitialAd()
        model.showInterstitialAdSignal.nullSafeObserve(this@DashboardActivity) {
            interstitialAd.loadAd(AdRequest.Builder().build())
        }
    }

    private fun setUpFab() {
        add_progress_fab.setOnClickListener {
            if (model.isDetailExpanded()) {
                EditProgressActivity.edit(
                    context = this@DashboardActivity,
                    progressId = requireNotNull(model.expandProgress.value)
                )
            } else {
                EditProgressActivity.createNew(this@DashboardActivity)
            }
        }
    }

    private fun setUpList() {
        // Set up the expandable layout
        expandable_page_container.addStateChangeCallbacks(pageStateChangeCallbacks)

        // Set up the adapter
        val progressListAdapter = ProgressAdapter { model.userWantsToOpenDetail(it.id) }

        // Prepare the list
        progress_list_rv.apply {
            setUp(expandable_page_container)
            adapter = progressListAdapter
        }

        // Start monitoring progress.
        model.progresses.nullSafeObserve(this@DashboardActivity) {
            progressListAdapter.submitList(it.toMutableList())
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

    internal fun collapseDetail() = progress_list_rv.collapse()

    override fun onSupportNavigateUp(): Boolean {
        if (!bottomNavigationSheet.isShowing) {
            bottomNavigationSheet.show()
        }
        return true
    }

    override fun onBackPressed() {
        when {
            model.isDetailExpanded() -> collapseDetail()
            bottomNavigationSheet.isShowing -> bottomNavigationSheet.hide()
            else -> super.onBackPressed()
        }
    }

    companion object {
        private const val ARG_PROGRESS_DETAIL_ID = "progressId"

        internal fun launch(context: Context, expandedProgressId: Long = -1) =
            context.startActivity(launchIntent(context, expandedProgressId))

        internal fun launchIntent(context: Context, expandedProgressId: Long = -1): Intent =
            context.prepareLaunchIntent(DashboardActivity::class.java)
                .apply { putExtra(ARG_PROGRESS_DETAIL_ID, expandedProgressId) }
    }
}
