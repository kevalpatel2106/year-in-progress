package com.kevalpatel2106.yip.detail

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.*
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.repo.utils.NtpProvider
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject


internal class DetailFragment : Fragment() {

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    internal lateinit var ntpProvider: NtpProvider

    private lateinit var model: DetailViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.getAppComponent().inject(this@DetailFragment)
        model = ViewModelProviders
                .of(this@DetailFragment, viewModelProvider)
                .get(DetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        model.progressId = arguments?.getLong(ARG_ID) ?: throw IllegalArgumentException("Invalid progress id.")
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up progress info
        model.progressTitle.nullSafeObserve(this@DetailFragment) {
            detail_title_tv.text = it
        }
        model.progressStartTime.nullSafeObserve(this@DetailFragment) {
            detail_start_time_tv.text = it
        }
        model.progressEndTime.nullSafeObserve(this@DetailFragment) {
            detail_end_time_tv.text = it
        }
        model.progressPercent.nullSafeObserve(this@DetailFragment) {
            detail_progressbar.progress = it.toInt()
            detail_progress_percent_tv.text = getString(R.string.progress_percentage, it)
        }
        model.progressTimeLeft.nullSafeObserve(this@DetailFragment) {
            detail_time_left_tv.text = it
        }

        // Set colors
        model.progressColor.nullSafeObserve(this@DetailFragment) {
            detail_progressbar.setProgressTint(it)
            detail_progress_percent_tv.setTextColor(it)
            detail_card.setCardBackgroundColor(darkenColor(it))
        }

        // Set up the delete button
        detail_delete_iv.setOnClickListener { model.progressTitle.value?.let { title -> conformDelete(title) } }
        model.isDeleteOptionVisible.nullSafeObserve(this@DetailFragment) { isVisible ->
            detail_delete_iv.showOrHide(isVisible)
        }
        var deleteInProgressSnackbar: Snackbar? = null
        model.isDeleting.nullSafeObserve(this@DetailFragment) { isDeleting ->
            detail_delete_iv.isEnabled = !isDeleting

            if (isDeleting) {
                deleteInProgressSnackbar = activity?.showSnack(getString(R.string.detail_deleting_progress_message), Snackbar.LENGTH_INDEFINITE)
            } else {
                deleteInProgressSnackbar?.dismiss()
            }
        }

        // Set the progress complete view.
        model.isProgressComplete.nullSafeObserve(this@DetailFragment) { isComplete ->
            detail_time_left_tv.showOrHide(!isComplete)
            detail_complete_container.showOrHideAll(detail_constraint_layout, isComplete)
        }
        detail_complete_share_btn.setOnClickListener {
            startActivity(model.prepareShareAchievement())
        }

        // Set up close button
        detail_close_iv.setOnClickListener { (activity as? DashboardActivity)?.collapseDetail() }

        // Handle errors.
        model.errorMessage.nullSafeObserve(this@DetailFragment) {
            activity?.showSnack(it)
        }
        model.closeDetail.nullSafeObserve(this@DetailFragment) {
            (activity as? DashboardActivity)?.collapseDetail()
        }
    }

    private fun conformDelete(title: String) {
        AlertDialog.Builder(context, R.style.YipAlertDialogTheme)
                .setMessage(getString(R.string.progress_delete_confirmation_message, title))
                .setPositiveButton(getString(R.string.progress_delete_confirmation_delete_title)) { _, _ -> model.deleteProgress() }
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(true)
                .show()
    }

    companion object {
        private const val ARG_ID = "arg_id"
        internal fun newInstance(progressId: Long): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply { putLong(ARG_ID, progressId) }
                retainInstance = true
            }
        }
    }
}