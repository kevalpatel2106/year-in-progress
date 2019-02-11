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
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.*
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapter
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.edit.EditProgressActivity
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import com.kevalpatel2106.yip.settings.SettingsActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_dashboard.*
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks
import javax.inject.Inject
import kotlin.random.Random


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

        // Start monitoring progress.
        model.progresses.nullSafeObserve(this@DashboardActivity) { adapter.get().submitList(it.toMutableList()) }
    }

    private fun setUpFab() {
        add_progress_fab.setOnClickListener {
            if (model.expandedProgressId > 0) {
                EditProgressActivity.edit(this@DashboardActivity, model.expandedProgressId)
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
            }

            override fun onPageExpanded() {
                add_progress_fab.setImageResource(com.kevalpatel2106.yip.R.drawable.ic_edit)
            }
        })
        progress_list_rv.layoutManager = LinearLayoutManager(this@DashboardActivity)
        progress_list_rv.setExpandablePage(expandable_page_container)
        progress_list_rv.adapter = adapter.get().apply {
            this.setHasStableIds(true)
            this.clickListener = { expandDetail(it) }
        }

    }

    private fun expandDetail(clicked: Progress) {
        askForRating()
        supportFragmentManager.commit {
            replace(R.id.expandable_page_container, DetailFragment.newInstance(clicked.id))
        }
        progress_list_rv.expandItem(clicked.id)
        model.expandedProgressId = clicked.id
    }

    internal fun collapseDetail() {
        progress_list_rv.collapse()
        model.expandedProgressId = -1
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
            model.expandedProgressId != -1L -> collapseDetail()
            bottomNavigationSheet.isShowing -> bottomNavigationSheet.hide()
            else -> super.onBackPressed()
        }
    }

    private fun askForRating() {
        if (!sharedPrefsProvider.getBoolFromPreference(PREF_KEY_RATED, false)
                && Random.nextInt(9) == 1) {
            AlertDialog.Builder(this@DashboardActivity, R.style.AppTheme_Dialog_Alert)
                    .setTitle(R.string.rate_us_dialog_title)
                    .setMessage(R.string.rate_us_dialog_message)
                    .setPositiveButton(R.string.rate_us_dialog_positive_btn) { _, _ ->
                        openPlayStorePage()
                        sharedPrefsProvider.savePreferences(PREF_KEY_RATED, true)
                    }
                    .setNegativeButton(R.string.rate_us_dialog_negative_btn, null)
                    .setCancelable(false)
                    .show()
        }
    }

    companion object {
        private const val PREF_KEY_RATED = "pref_key_rated"
        fun launch(context: Context) = context.startActivity(context.prepareLaunchIntent(DashboardActivity::class.java))
    }
}
