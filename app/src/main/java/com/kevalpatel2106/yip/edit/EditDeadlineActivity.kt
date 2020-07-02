package com.kevalpatel2106.yip.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.core.showOrHideLoader
import com.kevalpatel2106.yip.core.showSnack

import com.kevalpatel2106.yip.edit.EditDeadlineUseCases.conformBeforeExit
import com.kevalpatel2106.yip.edit.EditDeadlineUseCases.getDatePicker
import com.kevalpatel2106.yip.edit.colorPicker.ColorPickerListener
import com.kevalpatel2106.yip.edit.colorPicker.ColorsAdapter
import com.kevalpatel2106.yip.edit.notificationList.NotificationViewer
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.repo.utils.dateFormatter.DateFormatter
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_color
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_deadline_title
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_deadline_title_til
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_end_time
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_start_time
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_toolbar
import kotlinx.android.synthetic.main.activity_edit_deadline.notification_times
import javax.inject.Inject

@AndroidEntryPoint
internal class EditDeadlineActivity : AppCompatActivity() {

    @Inject
    lateinit var sdf: Lazy<DateFormatter>

    private val model: EditDeadlineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_deadline)

        // Actionbar set up
        setSupportActionBar(edit_toolbar)
        supportActionBar?.set(showTitle = false)

        // Set up color picker
        val colorsAdapter = ColorsAdapter(this, object : ColorPickerListener {
            override fun onLockedColorClicked() = PaymentActivity.launch(this@EditDeadlineActivity)
            override fun onColorSelected(color: Int) = model.onColorSelected(color)
        })
        edit_color.adapter = colorsAdapter

        setUpDatePickers()
        setUpNotificationList()

        // Set title
        edit_deadline_title.doAfterTextChanged { model.onTitleChanged(it.toString()) }

        // Monitor view model
        monitorViewState(colorsAdapter)
        model.closeSignal.nullSafeObserve(this@EditDeadlineActivity) { finish() }
        model.userMessage.nullSafeObserve(this@EditDeadlineActivity) { showSnack(it) }

        onNewIntent(intent)
    }

    private fun setUpNotificationList() {
        notification_times.callback = object : NotificationViewer.NotificationViewerInterface {
            override fun onLockClicked() {
                PaymentActivity.launch(this@EditDeadlineActivity)
            }

            override fun addNotificationClicked() {
                showNotificationPickerDialog { percent -> model.onNotificationAdded(percent) }
            }

            override fun onNotificationRemoved(percent: Float) {
                model.onNotificationRemoved(percent)
            }
        }
    }

    private fun monitorViewState(colorsAdapter: ColorsAdapter) {
        model.viewState.nullSafeObserve(this@EditDeadlineActivity) { viewState ->

            with(viewState) {
                invalidateOptionsMenu()

                edit_start_time.apply {
                    isEnabled = allowEditDate && !isLoading
                    text = sdf.get().formatDateOnly(startTime)
                }
                edit_end_time.apply {
                    isEnabled = allowEditDate && !isLoading
                    text = sdf.get().formatDateOnly(endTime)
                }

                edit_color.isEnabled = !isLoading
                colorsAdapter.setSelectedColor(selectedColor.colorInt)
                colorsAdapter.isLocked = lockColorPicker

                if (!viewState.isTitleChanged()) {
                    edit_deadline_title.setText(initialTitle)
                    edit_deadline_title.setSelection(initialTitle.length)
                }
                edit_deadline_title_til.error = titleErrorMsg

                notification_times.apply {
                    isEnabled = !isLoading
                    isLocked = lockNotification
                    updateList(notificationList)
                }
            }
        }
    }

    private fun setUpDatePickers() {
        // Start time set up
        edit_start_time.setOnClickListener {
            getDatePicker(listener = { date -> model.onStartDateSelected(date) }).show()
        }

        // End time set up
        edit_end_time.setOnClickListener {
            getDatePicker(listener = { date -> model.onEndDateSelected(date) })
                .apply {
                    model.viewState.value?.startTime?.let { start ->
                        datePicker.minDate = start.time
                    }
                }
                .show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        model.setDeadlineId(intent?.getLongExtra(ARG_EDIT_DEADLINE_ID, 0) ?: 0)
        super.onNewIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (model.viewState.value?.isLoading != true) {
            if (model.viewState.value?.isSomethingChanged == true) {
                conformBeforeExit()
            } else {
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_deadline_save, menu)
        menu?.findItem(R.id.menu_deadline_save)?.showOrHideLoader(
            context = this@EditDeadlineActivity,
            isShow = model.viewState.value?.isLoading == true
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_deadline_save -> model.saveDeadline()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ARG_EDIT_DEADLINE_ID = "deadline_id"

        internal fun createNew(context: Context) =
            context.startActivity(createNewDeadlineIntent(context))

        internal fun createNewDeadlineIntent(context: Context): Intent =
            context.prepareLaunchIntent(EditDeadlineActivity::class.java)

        internal fun edit(context: Context, deadlineId: Long) {
            context.startActivity(
                context.prepareLaunchIntent(EditDeadlineActivity::class.java).apply {
                    putExtra(ARG_EDIT_DEADLINE_ID, deadlineId)
                })
        }
    }
}
