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
import androidx.navigation.navArgs
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.getLaunchIntent
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.livedata.nullSafeValue
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.core.showOrHideLoader
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.databinding.ActivityEditDeadlineBinding
import com.kevalpatel2106.yip.edit.EditDeadlineUseCases.conformBeforeExit
import com.kevalpatel2106.yip.edit.EditDeadlineUseCases.showDatePicker
import com.kevalpatel2106.yip.edit.EditDeadlineUseCases.showNotificationPickerDialog
import com.kevalpatel2106.yip.edit.colorPicker.ColorPickerListener
import com.kevalpatel2106.yip.edit.colorPicker.ColorsAdapter
import com.kevalpatel2106.yip.edit.notificationList.NotificationViewer
import com.kevalpatel2106.yip.payment.PaymentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_color
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_deadline_title
import kotlinx.android.synthetic.main.activity_edit_deadline.edit_toolbar
import kotlinx.android.synthetic.main.activity_edit_deadline.notification_times

@AndroidEntryPoint
internal class EditDeadlineActivity : AppCompatActivity(), ColorPickerListener,
    NotificationViewer.NotificationViewerInterface {

    private val navArgs by navArgs<EditDeadlineActivityArgs>()
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
                OpenDatePicker -> {
                    showDatePicker(
                        fragmentManager = supportFragmentManager,
                        startDateSelection = model.viewState.nullSafeValue().startTime,
                        endDateSelection = model.viewState.nullSafeValue().endTime,
                        listener = { start, end -> model.onDateRangeSelected(start, end) }
                    )
                }
            }
        }
    }

    override fun addNotificationClicked() = model.onAddNotificationClicked()

    override fun onNotificationRemoved(percent: Float) = model.onNotificationRemoved(percent)

    override fun onColorSelected(color: Int) = model.onColorSelected(color)

    override fun onNewIntent(intent: Intent?) {
        model.setDeadlineId(navArgs.deadlineToEdit)
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
        internal fun createNew(context: Context) = context.startActivity(createNewIntent(context))

        internal fun createNewIntent(context: Context): Intent {
            return context.getLaunchIntent(EditDeadlineActivity::class.java) {
                putExtras(EditDeadlineActivityArgs().toBundle())
            }
        }

        internal fun edit(context: Context, args: EditDeadlineActivityArgs) {
            val intent = context.getLaunchIntent(EditDeadlineActivity::class.java) {
                putExtras(args.toBundle())
            }
            context.startActivity(intent)
        }
    }
}
