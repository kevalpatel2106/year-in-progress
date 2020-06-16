package com.kevalpatel2106.yip.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.core.showOrHideLoader
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.edit.EditProgressUseCases.conformBeforeExit
import com.kevalpatel2106.yip.edit.EditProgressUseCases.getDatePicker
import com.kevalpatel2106.yip.edit.colorPicker.ColorPickerListener
import com.kevalpatel2106.yip.edit.colorPicker.ColorsAdapter
import com.kevalpatel2106.yip.edit.notificationList.NotificationViewer
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.repo.utils.DateFormatter
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_edit_progress.edit_color
import kotlinx.android.synthetic.main.activity_edit_progress.edit_end_time
import kotlinx.android.synthetic.main.activity_edit_progress.edit_progress_title
import kotlinx.android.synthetic.main.activity_edit_progress.edit_progress_title_til
import kotlinx.android.synthetic.main.activity_edit_progress.edit_start_time
import kotlinx.android.synthetic.main.activity_edit_progress.edit_toolbar
import kotlinx.android.synthetic.main.activity_edit_progress.notification_times
import javax.inject.Inject

internal class EditProgressActivity : AppCompatActivity() {

    @Inject
    lateinit var sdf: Lazy<DateFormatter>

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: EditViewProgressModel by viewModels { viewModelProvider }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@EditProgressActivity)
        setContentView(R.layout.activity_edit_progress)

        // Actionbar set up
        setSupportActionBar(edit_toolbar)
        supportActionBar?.set(showTitle = false)

        // Set up color picker
        val colorsAdapter = ColorsAdapter(this, object : ColorPickerListener {
            override fun onLockedColorClicked() = PaymentActivity.launch(this@EditProgressActivity)
            override fun onColorSelected(color: Int) = model.onProgressColorSelected(color)
        })
        edit_color.adapter = colorsAdapter

        setUpDatePickers()
        setUpNotificationList()

        // Set title
        edit_progress_title.doAfterTextChanged { model.onProgressTitleChanged(it.toString()) }

        // Monitor view model
        monitorViewState(colorsAdapter)
        model.closeSignal.nullSafeObserve(this@EditProgressActivity) { finish() }
        model.userMessage.nullSafeObserve(this@EditProgressActivity) { showSnack(it) }

        onNewIntent(intent)
    }

    private fun setUpNotificationList() {
        notification_times.callback = object : NotificationViewer.NotificationViewerInterface {
            override fun onLockClicked() {
                PaymentActivity.launch(this@EditProgressActivity)
            }

            override fun addNotificationClicked() {
                showNotificationPickerDialog { progress -> model.onNotificationAdded(progress) }
            }

            override fun onNotificationRemoved(percent: Float) {
                model.onNotificationRemoved(percent)
            }
        }
    }

    private fun monitorViewState(colorsAdapter: ColorsAdapter) {
        model.viewState.nullSafeObserve(this@EditProgressActivity) { viewState ->

            with(viewState) {
                invalidateOptionsMenu()

                edit_start_time.apply {
                    isEnabled = allowEditDate && !isLoadingProgress
                    text = sdf.get().formatDateOnly(progressStartTime)
                }
                edit_end_time.apply {
                    isEnabled = allowEditDate && !isLoadingProgress
                    text = sdf.get().formatDateOnly(progressEndTime)
                }

                edit_color.isEnabled = !isLoadingProgress
                colorsAdapter.setSelectedColor(progressColor.colorInt)
                colorsAdapter.isLocked = lockColorPicker

                if (!viewState.isTitleChanged()) {
                    edit_progress_title.setText(initialTitle)
                    edit_progress_title.setSelection(initialTitle.length)
                }
                edit_progress_title_til.error = titleErrorMsg

                notification_times.apply {
                    isEnabled = !isLoadingProgress
                    isLocked = lockNotification
                    updateList(notificationList)
                }
            }
        }
    }

    private fun setUpDatePickers() {
        // Start time set up
        edit_start_time.setOnClickListener {
            getDatePicker(listener = { date -> model.onProgressStartDateSelected(date) }).show()
        }

        // End time set up
        edit_end_time.setOnClickListener {
            getDatePicker(listener = { date -> model.onProgressEndDateSelected(date) })
                .apply {
                    model.viewState.value?.progressStartTime?.let { start ->
                        datePicker.minDate = start.time
                    }
                }
                .show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        model.setProgressId(intent?.getLongExtra(ARG_EDIT_PROGRESS_ID, 0) ?: 0)
        super.onNewIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (model.viewState.value?.isLoadingProgress != true) {
            if (model.viewState.value?.isSomethingChanged == true) {
                conformBeforeExit()
            } else {
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_progress_save, menu)
        menu?.findItem(R.id.menu_progress_save)?.showOrHideLoader(
            context = this@EditProgressActivity,
            isShow = model.viewState.value?.isLoadingProgress == true
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_progress_save -> model.saveProgress()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ARG_EDIT_PROGRESS_ID = "progress_id"

        internal fun createNew(context: Context) =
            context.startActivity(createNewDeadlineIntent(context))

        internal fun createNewDeadlineIntent(context: Context): Intent =
            context.prepareLaunchIntent(EditProgressActivity::class.java)

        internal fun edit(context: Context, progressId: Long) {
            context.startActivity(
                context.prepareLaunchIntent(EditProgressActivity::class.java).apply {
                    putExtra(ARG_EDIT_PROGRESS_ID, progressId)
                })
        }
    }
}
