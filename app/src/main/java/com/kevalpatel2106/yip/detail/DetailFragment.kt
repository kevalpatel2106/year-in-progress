package com.kevalpatel2106.yip.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.option_menu

@AndroidEntryPoint
internal class DetailFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private val popupMenu: PopupMenu by lazy {
        DetailUseCase.preparePopupMenu(anchor = option_menu, clickListener = this@DetailFragment)
    }

    private val model: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model.setDeadlineIdToMonitor(requireNotNull(arguments?.getLong(ARG_ID)))

        return DataBindingUtil
            .inflate<FragmentDetailBinding>(inflater, R.layout.fragment_detail, container, false)
            .apply {
                lifecycleOwner = this@DetailFragment
                viewModel = model
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monitorViewState()
        monitorSingleEvents()
    }

    private fun monitorViewState() {
        var deleteDeadlineSnackbar: Snackbar? = null
        model.viewState.nullSafeObserve(this@DetailFragment) { viewState ->
            if (viewState.isDeleting) {
                deleteDeadlineSnackbar = activity?.showSnack(
                    getString(R.string.detail_deleting_deadline_message),
                    Snackbar.LENGTH_INDEFINITE
                )
            } else {
                deleteDeadlineSnackbar?.dismiss()
            }
        }
    }

    private fun monitorSingleEvents() {
        model.singleEvent.nullSafeObserve(viewLifecycleOwner) { event ->
            when (event) {
                CloseDetailScreen -> closeDetail()
                is ShowUserMessage -> {
                    activity?.showSnack(event.message)
                    if (event.closeScreen) closeDetail()
                }
                is ShowDeleteConfirmationDialog -> {
                    DetailUseCase.conformDelete(requireContext(), event.deadlineTitle) {
                        model.onDeleteDeadlineConfirmed()
                    }
                }
                OpenPopUpMenu -> popupMenu.show()
            }
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_delete_deadline -> model.onDeleteDeadlineClicked()
            R.id.menu_pin_shortcut -> model.requestPinShortcut()
        }
        return true
    }

    private fun closeDetail() = (activity as? DashboardActivity)?.collapseDetail()

    companion object {
        private const val ARG_ID = "arg_id"

        internal fun newInstance(deadlineId: Long): DetailFragment {
            return DetailFragment().apply {
                arguments = bundleOf(ARG_ID to deadlineId)
                retainInstance = true
            }
        }
    }
}
