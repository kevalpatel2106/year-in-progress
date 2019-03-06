package com.kevalpatel2106.yip.detail

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.databinding.FragmentDetailBinding
import com.kevalpatel2106.yip.di.getAppComponent
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

internal class DetailFragment : Fragment() {

    private val popupMenu: PopupMenu by lazy {
        PopupMenu(option_menu.context, option_menu).apply {
            menu.add(
                    R.id.menu_progress_group,
                    R.id.menu_delete_progress,
                    Menu.NONE,
                    getString(R.string.meu_title_delete)
            )
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.menu_delete_progress) {
                    model.viewState.value?.progressTitleText?.let { title -> conformDelete(title) }
                }
                true
            }
        }
    }

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: DetailViewModel by lazy {
        provideViewModel(viewModelProvider, DetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.getAppComponent().inject(this@DetailFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        model.progressId = arguments?.getLong(ARG_ID)
            ?: throw IllegalArgumentException("Invalid progress id.")

        return DataBindingUtil
                .inflate<FragmentDetailBinding>(inflater, R.layout.fragment_detail, container, false)
                .apply {
                    lifecycleOwner = this@DetailFragment
                    view = this@DetailFragment
                    viewModel = model
                }
                .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle the delete
        var deleteInProgressSnackbar: Snackbar? = null
        model.isDeleting.nullSafeObserve(this@DetailFragment) { isDeleting ->
            if (isDeleting) {
                deleteInProgressSnackbar = activity?.showSnack(getString(R.string.detail_deleting_progress_message), Snackbar.LENGTH_INDEFINITE)
            } else {
                deleteInProgressSnackbar?.dismiss()
            }
        }

        // Handle errors.
        model.errorMessage.nullSafeObserve(this@DetailFragment) { activity?.showSnack(it) }
        model.closeDetail.nullSafeObserve(this@DetailFragment) { closeDetail() }
    }

    private fun conformDelete(title: String) {
        AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
                .setMessage(getString(R.string.progress_delete_confirmation_message, title))
                .setPositiveButton(getString(R.string.progress_delete_confirmation_delete_title)) { _, _ -> model.deleteProgress() }
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(true)
                .show()
    }

    fun showDetailOptions() = popupMenu.show()

    fun showShareAchievements() {
        context?.let {
            startActivity(DetailUseCase.prepareShareAchievementIntent(
                    context = it,
                    title = model.viewState.value?.progressTitleText
            ))
        }
    }

    fun closeDetail() = (activity as? DashboardActivity)?.collapseDetail()

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