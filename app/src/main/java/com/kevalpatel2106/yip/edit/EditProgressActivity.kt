package com.kevalpatel2106.yip.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.core.showOrHideLoader
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.edit.EditProgressUseCases.conformBeforeExit
import com.kevalpatel2106.yip.edit.EditProgressUseCases.getDatePicker
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.repo.utils.DateFormatter
import com.kevalpatel2106.yip.utils.ColorPicker
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
    lateinit var sdf: DateFormatter

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: EditViewProgressModel by lazy {
        provideViewModel(viewModelProvider, EditViewProgressModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@EditProgressActivity)
        setContentView(R.layout.activity_edit_progress)

        // Actionbar set up
        setSupportActionBar(edit_toolbar)
        supportActionBar?.set(showTitle = false)

        setUpTitle()
        setUpDates()
        setUpColorPicker()
        monitorUserMessages()

        // Monitor loader
        model.isLoadingProgress.nullSafeObserve(this@EditProgressActivity) { isLoading ->
            invalidateOptionsMenu()
            edit_color.isEnabled = !isLoading
        }
        model.isPrebuiltProgress.nullSafeObserve(this@EditProgressActivity) { isPrebuilt ->
            edit_start_time.isEnabled = !isPrebuilt
            edit_end_time.isEnabled = !isPrebuilt
        }

        model.currentNotificationsList.nullSafeObserve(this@EditProgressActivity) {
            notification_times.notificationPercents = it.toMutableList()
        }

        onNewIntent(intent)
    }

    private fun monitorUserMessages() {
        model.closeSignal.nullSafeObserve(this@EditProgressActivity) { finish() }
        model.userMessage.nullSafeObserve(this@EditProgressActivity) { showSnack(it) }
        model.errorInvalidTitle.observe(this@EditProgressActivity, Observer { error ->
            edit_progress_title_til.error = error
        })
    }

    private fun setUpColorPicker() {
        edit_color.colorSelectedListener = object : ColorPicker.ColorPickerListener {
            override fun onLockedColorClicked() {
                PaymentActivity.launch(this@EditProgressActivity)
            }

            override fun onColorSelected(color: Int) {
                model.onProgressColorSelected(color)
            }
        }
        model.colors.nullSafeObserve(this@EditProgressActivity) { edit_color.setColors(it) }
        model.currentColor.nullSafeObserve(this@EditProgressActivity) {
            edit_color.setSelectedColor(it.value)
        }
        model.lockColorPicker.nullSafeObserve(this@EditProgressActivity) { lock ->
            edit_color.lock(lock)
        }
    }

    private fun setUpDates() {
        // Start time set up
        edit_start_time.setOnClickListener {
            getDatePicker(listener = { date -> model.onProgressStartDateSelected(date) }).show()
        }
        model.currentStartDate.nullSafeObserve(this@EditProgressActivity) {
            edit_start_time.text = sdf.formatDateOnly(it)
        }

        // End time set up
        edit_end_time.setOnClickListener {
            getDatePicker(listener = { date ->
                model.onProgressEndDateSelected(date)
            }).apply {
                model.currentStartDate.value?.let { start -> datePicker.minDate = start.time }
            }.show()
        }
        model.currentEndDate.nullSafeObserve(this@EditProgressActivity) {
            edit_end_time.text = sdf.formatDateOnly(it)
        }
    }

    private fun setUpTitle() {
        edit_progress_title.doAfterTextChanged { model.onProgressTitleChanged(it.toString()) }
        model.initialTitle.nullSafeObserve(this@EditProgressActivity) { title ->
            edit_progress_title.setText(title)
            edit_progress_title.setSelection(title.length)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        model.progressId = intent?.getLongExtra(ARG_EDIT_PROGRESS_ID, 0) ?: 0
        super.onNewIntent(intent)
    }

    override fun onBackPressed() {
        if (model.isLoadingProgress.value != true) {
            if (model.isSomethingChanged || model.isTitleChanged) {
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
            isShow = model.isLoadingProgress.value == true
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.menu_progress_save -> model.saveProgress(notification_times.notificationPercents)
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
            context.startActivity(context.prepareLaunchIntent(EditProgressActivity::class.java).apply {
                putExtra(ARG_EDIT_PROGRESS_ID, progressId)
            })
        }
    }
}