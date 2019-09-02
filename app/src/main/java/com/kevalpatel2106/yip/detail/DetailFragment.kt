package com.kevalpatel2106.yip.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
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
import kotlinx.android.synthetic.main.fragment_detail.option_menu
import javax.inject.Inject

internal class DetailFragment : Fragment() {

    private val popupMenu: PopupMenu by lazy {
        DetailUseCase.preparePopupMenu(option_menu).apply {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_delete_progress -> {
                        model.viewState.value?.progressTitleText?.let { title ->
                            DetailUseCase.conformDelete(
                                requireContext(),
                                title
                            ) { model.deleteProgress() }
                        }
                    }
                    R.id.menu_pin_shortcut -> model.requestPinShortcut()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                deleteInProgressSnackbar = activity?.showSnack(
                    getString(R.string.detail_deleting_progress_message),
                    Snackbar.LENGTH_INDEFINITE
                )
            } else {
                deleteInProgressSnackbar?.dismiss()
            }
        }

        // Handle errors.
        model.userMessage.nullSafeObserve(this@DetailFragment) { activity?.showSnack(it) }
        model.closeDetail.nullSafeObserve(this@DetailFragment) { closeDetail() }
    }

    fun showDetailOptions() = popupMenu.show()

    fun showShareAchievements() {
        context?.let { ctx ->
            startActivity(
                DetailUseCase.prepareShareAchievementIntent(
                    context = ctx,
                    title = model.viewState.value?.progressTitleText
                )
            )
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
