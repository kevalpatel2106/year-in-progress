package com.kevalpatel2106.yip.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.livedata.nullSafeValue
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.core.showOrHideLoader
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.databinding.ActivityEditDeadlineBinding
import com.kevalpatel2106.yip.edit.EditDeadlineUseCases.conformBeforeExit
import com.kevalpatel2106.yip.edit.EditDeadlineUseCases.getDatePicker
import com.kevalpatel2106.yip.edit.EditDeadlineUseCases.showNotificationPickerDialog
import com.kevalpatel2106.yip.edit.EditDeadlineViewModel.Companion.NEW_DEADLINE_ID
import com.kevalpatel2106.yip.edit.colorPicker.ColorPickerListener
import com.kevalpatel2106.yip.edit.colorPicker.ColorsAdapter
import com.kevalpatel2106.yip.edit.notificationList.NotificationViewer
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_color
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_deadline_title
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_toolbar
import kotlinx.android.synthetic.main.activity_edit_deadline.notification_times
import javax.inject.Inject

@AndroidEntryPoint
internal class EditDeadlineActivity : AppCompatActivity(), ColorPickerListener,
    NotificationViewer.NotificationViewerInterface {

    @Inject
    lateinit var sdf: Lazy<DateFormatter>

    private val model: EditDeadlineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityEditDeadlineBinding>(
            this,
            R.layout.activity_edit_deadline
        ).apply {
            lifecycleOwner = this@EditDeadlineActivity
            viewModel = model
        }

        // Actionbar set up
        setSupportActionBar(edit_toolbar)
        supportActionBar?.set(showTitle = false)

        notification_times.callback = this
        edit_deadline_title.doAfterTextChanged { model.onTitleChanged(it.toString()) }
        val colorsAdapter = setUpColorPicker()

        // Monitor view model
        monitorViewState(colorsAdapter)
        monitorSingleViewEvent()

        onNewIntent(intent)
    }

    private fun setUpColorPicker(): ColorsAdapter {
        val colorsAdapter = ColorsAdapter(context = this, colorSelectedListener = this)
        edit_color.adapter = colorsAdapter
        return colorsAdapter
    }

    private fun monitorViewState(colorsAdapter: ColorsAdapter) {
        model.viewState.nullSafeObserve(this@EditDeadlineActivity) { viewState ->
            with(viewState) {
                invalidateOptionsMenu()
                if (!viewState.isTitleChanged()) {
                    edit_deadline_title.setText(initialTitle)
                    edit_deadline_title.setSelection(initialTitle.length)
                }
                colorsAdapter.setSelectedColor(selectedColor.colorInt)
                colorsAdapter.isLocked = showLockedColorPicker
            }
        }
    }

    private fun monitorSingleViewEvent() {
        model.singleViewState.nullSafeObserve(this@EditDeadlineActivity) { state ->
            when (state) {
                OpenPaymentScreen -> PaymentActivity.launch(this@EditDeadlineActivity)
                CloseScreen -> finish()
                ShowConfirmationDialog -> conformBeforeExit()
                is ShowUserMessage -> {
                    showSnack(state.message, dismissListener = { if (state.closeScreen) finish() })
                }
                ShowNotificationPicker -> {
                    showNotificationPickerDialog { percent -> model.onNotificationAdded(percent) }
                }
                OpenStartDatePicker -> {
                    getDatePicker(listener = { date -> model.onEndDateSelected(date) })
                        .apply {
                            datePicker.minDate = model.viewState.nullSafeValue().startTime.time
                        }
                        .show()
                }
                OpenEndDatePicker -> {
                    getDatePicker(listener = { date -> model.onStartDateSelected(date) }).show()
                }
            }
        }
    }

    override fun addNotificationClicked() = model.onAddNotificationClicked()

    override fun onNotificationRemoved(percent: Float) = model.onNotificationRemoved(percent)

    override fun onColorSelected(color: Int) = model.onColorSelected(color)

    override fun onNewIntent(intent: Intent?) {
        val deadlineId =
            intent?.getLongExtra(ARG_EDIT_DEADLINE_ID, NEW_DEADLINE_ID) ?: NEW_DEADLINE_ID
        model.setDeadlineId(deadlineId)
        super.onNewIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() = model.onClosePressed()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_deadline_save, menu)
        menu?.findItem(R.id.menu_deadline_save)?.showOrHideLoader(
            context = this@EditDeadlineActivity,
            isShow = model.viewState.nullSafeValue().isLoading
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
                context.prepareLaunchIntent(EditDeadlineActivity::class.java)
                    .apply { putExtra(ARG_EDIT_DEADLINE_ID, deadlineId) }
            )
        }
    }
}
