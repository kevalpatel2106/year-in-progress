package com.kevalpatel2106.yip.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commitNow
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.AppConstants
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.openPlayStorePage
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
internal class DashboardActivity : AppCompatActivity(),
    DeadlineAdapterEventListener,
    PageStateChangeCallbacks {

    private val bottomNavigationSheet: BottomNavigationDialog by lazy { BottomNavigationDialog() }
    private val model: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityDashboardBinding>(this, R.layout.activity_dashboard)
            .apply {
                lifecycleOwner = this@DashboardActivity
                viewModel = model
            }

        setSupportActionBar(bottom_app_bar)
        bottom_app_bar.setNavigationOnClickListener { model.onHamburgerMenuClicked() }

        setUpDeadlinesList()

        observeSingleEvents(prepareInterstitialAd())
        observeExpandedDetail()

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val detailId = intent.getLongExtra(
            ARG_DEADLINE_DETAIL_ID,
            AppConstants.INVALID_DEADLINE_ID
        )
        model.onOpenDeadlineDetail(detailId)
    }

    private fun observeSingleEvents(interstitialAd: InterstitialAd) {
        model.singleEvents.nullSafeObserve(this@DashboardActivity) { event ->
            when (event) {
                is ShowUserMessage -> showSnack(event.message)
                is OpenEditDeadline -> EditDeadlineActivity.edit(this, event.deadlineId)
                AskForRating -> {
                    showRatingDialog(
                        rateNow = { model.onRateNowClicked() },
                        neverAsk = { model.onRateNeverClicked() }
                    )
                }
                ShowInterstitialAd -> interstitialAd.loadAd(AdRequest.Builder().build())
                OpenCreateNewDeadline -> EditDeadlineActivity.createNew(this)
                OpenPlayStore -> openPlayStorePage()
                CloseScreen -> finish()
                OpenBottomNavigationSheet -> openBottomNavigation()
            }
        }
    }

    private fun observeExpandedDetail() {
        model.expandViewState.nullSafeObserve(this@DashboardActivity) { viewState ->
            when (viewState) {
                DetailViewCollapsed -> deadline_list_rv.collapse()
                is DetailViewExpanded -> {
                    supportFragmentManager.commitNow(allowStateLoss = true) {
                        replace(
                            R.id.expandable_page_container,
                            DetailFragment.newInstance(viewState.deadlineId)
                        )
                    }
                    deadline_list_rv.expandItem(viewState.deadlineId)
                }
            }
        }
    }

    private fun openBottomNavigation() {
        if (!bottomNavigationSheet.isVisible) {
            bottomNavigationSheet.show(
                supportFragmentManager,
                BottomNavigationDialog::class.simpleName
            )
        }
    }

    private fun setUpDeadlinesList() {
        // Set up the expandable layout
        expandable_page_container.addStateChangeCallbacks(this@DashboardActivity)

        // Prepare the list
        val deadlineAdapter = DeadlineAdapter(this@DashboardActivity)
        deadline_list_rv.apply {
            setUp(expandable_page_container)
            adapter = deadlineAdapter
        }
        model.deadlines.nullSafeObserve(this@DashboardActivity, deadlineAdapter::submitList)
    }

    internal fun collapseDetail() = model.onCloseDeadlineDetail()

    override fun onBackPressed() = model.onBackPressed()

    override fun onDeadlineClicked(deadline: Deadline) = model.onOpenDeadlineDetail(deadline.id)

    override fun onPageAboutToCollapse(collapseAnimDuration: Long) =
        bottom_app_bar.slideUp(collapseAnimDuration)

    override fun onPageAboutToExpand(expandAnimDuration: Long) =
        bottom_app_bar.slideDown(expandAnimDuration)

    override fun onPageCollapsed() {
        add_deadline_fab.setImageResource(R.drawable.ic_add)
        model.onCloseDeadlineDetail()
    }

    override fun onPageExpanded() = add_deadline_fab.setImageResource(R.drawable.ic_edit)

    companion object {
        private const val ARG_DEADLINE_DETAIL_ID = "deadlineId"

        internal fun launch(context: Context, expandedDeadlineId: Long? = null) {
            context.startActivity(launchIntent(context, expandedDeadlineId))
        }

        internal fun launchIntent(context: Context, expandedDeadlineId: Long? = null): Intent {
            return context.prepareLaunchIntent(DashboardActivity::class.java)
                .apply { putExtra(ARG_DEADLINE_DETAIL_ID, expandedDeadlineId) }
        }
    }
}
