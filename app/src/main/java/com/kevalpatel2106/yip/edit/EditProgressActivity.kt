package com.kevalpatel2106.yip.edit

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.showOrHideLoader
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.di.getAppComponent
import kotlinx.android.synthetic.main.activity_edit_progress.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


internal class EditProgressActivity : AppCompatActivity() {
    private val sdf by lazy { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            model.onProgressTitleChanged(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Do nothing
        }
    }

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var model: EditViewProgressModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_progress)

        getAppComponent().inject(this@EditProgressActivity)
        model = ViewModelProviders
            .of(this@EditProgressActivity, viewModelProvider)
            .get(EditViewProgressModel::class.java)

        // Actionbar set up
        setSupportActionBar(edit_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        model.currentTitle.nullSafeObserve(this@EditProgressActivity) {
            edit_progress_title.removeTextChangedListener(textWatcher)
            edit_progress_title.setText(it)
            edit_progress_title.setSelection(it.length)
            edit_progress_title.addTextChangedListener(textWatcher)
        }

        // Start time set up
        edit_start_time.setOnClickListener {
            getDatePicker(listener = { date -> model.onProgressStartDateSelected(date) }).show()
        }
        model.currentStartDate.nullSafeObserve(this@EditProgressActivity) {
            edit_start_time.text = sdf.format(it)
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
            edit_end_time.text = sdf.format(it)
        }

        // Color set up
        edit_color.setOnColorSelectedListener { model.onProgressColorSelected(it) }
        model.colors.nullSafeObserve(this@EditProgressActivity) { edit_color.setColors(it) }
        model.currentColor.nullSafeObserve(this@EditProgressActivity) {
            edit_color.setSelectedColor(it.value)
        }

        // Monitor userMessage
        model.close.nullSafeObserve(this@EditProgressActivity) { finish() }
        model.userMessage.nullSafeObserve(this@EditProgressActivity) { showSnack(it) }
        model.errorInvalidTitle.observe(this@EditProgressActivity, Observer { error ->
            edit_progress_title_til.error = error
        })

        // Monitor loader
        model.isLoadingProgress.nullSafeObserve(this@EditProgressActivity) {
            invalidateOptionsMenu()
            edit_color.isEnabled = !it
        }
        model.isPrebuiltProgress.nullSafeObserve(this@EditProgressActivity){
            edit_start_time.isEnabled = !it
            edit_end_time.isEnabled = !it
        }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        model.progressId = intent?.getLongExtra(ARG_EDIT_PROGRESS_ID, 0) ?: 0
        super.onNewIntent(intent)
    }

    override fun onBackPressed() = confirmAndClose()

    private fun confirmAndClose() {
        if (model.isLoadingProgress.value != true) {
            if (model.isSomethingChanged) {
                // TODO confirmation dialog
                finish()
            } else {
                finish()
            }
        }
    }

    private fun getDatePicker(
        preset: Calendar = Calendar.getInstance(),
        listener: (date: Date) -> Unit
    ): DatePickerDialog {
        return DatePickerDialog(
            this@EditProgressActivity,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val cal = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0)
                }
                listener.invoke(Date(cal.timeInMillis))
            },
            preset.get(Calendar.YEAR),
            preset.get(Calendar.MONTH),
            preset.get(Calendar.DAY_OF_MONTH)
        )
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
            android.R.id.home -> confirmAndClose()
            R.id.menu_progress_save -> model.saveProgress()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ARG_EDIT_PROGRESS_ID = "progress_id"

        internal fun createNew(context: Context) {
            context.startActivity(context.prepareLaunchIntent(EditProgressActivity::class.java))
        }

        internal fun edit(context: Context, progressId: Long) {
            context.startActivity(context.prepareLaunchIntent(EditProgressActivity::class.java).apply {
                putExtra(ARG_EDIT_PROGRESS_ID, progressId)
            })
        }
    }
}