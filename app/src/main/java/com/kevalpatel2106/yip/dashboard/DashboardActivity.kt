package com.kevalpatel2106.yip.dashboard

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cocosw.bottomsheet.BottomSheet
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.openPlayStorePage
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.sendMailToDev
import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapter
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.edit.EditProgressActivity
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.settings.SettingsActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_main.*
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks
import javax.inject.Inject


class DashboardActivity : AppCompatActivity() {
    private var isDetailExpanded = false

    private lateinit var bottomNavigationSheet: BottomSheet

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    internal lateinit var adapter: Lazy<ProgressAdapter>

    private lateinit var model: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottom_app_bar)

        // Inject dependency
        getAppComponent().inject(this@DashboardActivity)
        model = ViewModelProviders
                .of(this@DashboardActivity, viewModelProvider)
                .get(DashboardViewModel::class.java)

        setUpBottomNavigation()
        setUpFab()
        setUpList()

        // Start monitoring progress.
        model.progresses.nullSafeObserve(this@DashboardActivity) { adapter.get().submitList(it.toMutableList()) }
    }

    private fun setUpFab() {
        add_progress_fab.setOnClickListener {
            EditProgressActivity.createNew(this@DashboardActivity)
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
                bottom_app_bar.visibility = View.VISIBLE
            }

            override fun onPageAboutToExpand(expandAnimDuration: Long) {
                bottomNavigationSheet.hide()
                bottom_app_bar.visibility = View.GONE
            }

            override fun onPageCollapsed() {
                // Do nothing
            }

            override fun onPageExpanded() {
                // Do nothing
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
        supportFragmentManager.commit {
            replace(R.id.expandable_page_container, DetailFragment.newInstance(clicked.id))
        }
        progress_list_rv.expandItem(clicked.id)
        isDetailExpanded = true
    }

    internal fun collapseDetail() {
        progress_list_rv.collapse()
        isDetailExpanded = false
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
            isDetailExpanded -> collapseDetail()
            bottomNavigationSheet.isShowing -> bottomNavigationSheet.hide()
            else -> super.onBackPressed()
        }
    }

    companion object {
        fun launch(context: Context) = context.startActivity(context.prepareLaunchIntent(DashboardActivity::class.java))
    }
}
