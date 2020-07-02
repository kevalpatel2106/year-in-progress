package com.kevalpatel2106.yip.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commitNow
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.core.slideDown
import com.kevalpatel2106.yip.core.slideUp
import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapter
import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapterEventListener
import com.kevalpatel2106.yip.dashboard.navDrawer.BottomNavigationDialog
import com.kevalpatel2106.yip.databinding.ActivityDashboardBinding
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.edit.EditDeadlineActivity
import com.kevalpatel2106.yip.entity.Deadline
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dashboard.add_deadline_fab
import kotlinx.android.synthetic.main.activity_dashboard.bottom_app_bar
import kotlinx.android.synthetic.main.activity_dashboard.deadline_list_rv
import kotlinx.android.synthetic.main.activity_dashboard.expandable_page_container
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks

@AndroidEntryPoint
internal class DashboardActivity : AppCompatActivity(), DeadlineAdapterEventListener {

    private val bottomNavigationSheet: BottomNavigationDialog by lazy { BottomNavigationDialog() }

    private val pageStateChangeCallbacks = object : PageStateChangeCallbacks {
        override fun onPageAboutToCollapse(collapseAnimDuration: Long) {
            bottom_app_bar.slideUp(collapseAnimDuration)
        }

        override fun onPageAboutToExpand(expandAnimDuration: Long) {
            bottom_app_bar.slideDown(expandAnimDuration)
        }

        override fun onPageCollapsed() {
            add_deadline_fab.setImageResource(R.drawable.ic_add)
            collapseDetail()
        }

        override fun onPageExpanded() {
            add_deadline_fab.setImageResource(R.drawable.ic_edit)
        }
    }

    private val model: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val detailId = intent?.getLongExtra(
            ARG_DEADLINE_DETAIL_ID,
            DashboardViewModel.RESET_COLLAPSED_ID
        ) ?: DashboardViewModel.RESET_COLLAPSED_ID
        if (detailId > 0) model.userWantsToOpenDetail(detailId)
    }

    private fun setUpInterstitialAd() {
        val interstitialAd = prepareInterstitialAd()
        model.showInterstitialAdSignal.nullSafeObserve(this@DashboardActivity) {
            interstitialAd.loadAd(AdRequest.Builder().build())
        }
    }

    private fun setUpFab() {
        add_deadline_fab.setOnClickListener {
            if (model.isDetailExpanded()) {
                EditDeadlineActivity.edit(
                    context = this@DashboardActivity,
                    deadlineId = requireNotNull(model.expandDeadline.value)
                )
            } else {
                EditDeadlineActivity.createNew(this@DashboardActivity)
            }
        }
    }

    private fun setUpList() {
        // Set up the expandable layout
        expandable_page_container.addStateChangeCallbacks(pageStateChangeCallbacks)

        // Set up the adapter
        val deadlineAdapter = DeadlineAdapter(this@DashboardActivity)

        // Prepare the list
        deadline_list_rv.apply {
            setUp(expandable_page_container)
            adapter = deadlineAdapter
        }

        // Start monitoring deadline.
        model.deadlines.nullSafeObserve(this@DashboardActivity) {
            deadlineAdapter.submitList(it.toMutableList())
        }

        // Expand detail
        model.expandDeadline.nullSafeObserve(this@DashboardActivity) {
            if (it == DashboardViewModel.RESET_COLLAPSED_ID) {
                deadline_list_rv.collapse()
            } else {
                supportFragmentManager.commitNow(allowStateLoss = true) {
                    replace(R.id.expandable_page_container, DetailFragment.newInstance(it))
                }
                deadline_list_rv.expandItem(it)
            }
        }
    }

    internal fun collapseDetail() = model.resetExpandedDeadline()

    override fun onDeadlineClicked(deadline: Deadline) = model.userWantsToOpenDetail(deadline.id)

    override fun onSupportNavigateUp(): Boolean {
        if (!model.isDetailExpanded() && !bottomNavigationSheet.isVisible) {
            bottomNavigationSheet.show(
                supportFragmentManager,
                BottomNavigationDialog::class.java.name
            )
            return false
        }
        return true
    }

    override fun onBackPressed() {
        when {
            model.isDetailExpanded() -> collapseDetail()
            else -> super.onBackPressed()
        }
    }

    companion object {
        private const val ARG_DEADLINE_DETAIL_ID = "deadlineId"

        internal fun launch(
            context: Context,
            expandedDeadlineId: Long = DashboardViewModel.RESET_COLLAPSED_ID
        ) {
            context.startActivity(launchIntent(context, expandedDeadlineId))
        }

        internal fun launchIntent(
            context: Context,
            expandedDeadlineId: Long = DashboardViewModel.RESET_COLLAPSED_ID
        ): Intent {
            return context.prepareLaunchIntent(DashboardActivity::class.java)
                .apply { putExtra(ARG_DEADLINE_DETAIL_ID, expandedDeadlineId) }
        }
    }
}
