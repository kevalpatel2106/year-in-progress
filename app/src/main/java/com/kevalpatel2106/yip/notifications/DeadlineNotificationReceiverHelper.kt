package com.kevalpatel2106.yip.notifications

import android.annotation.SuppressLint
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import timber.log.Timber
import javax.inject.Inject

internal class DeadlineNotificationReceiverHelper @Inject constructor(
    private val deadlineRepo: DeadlineRepo,
    private val deadlineNotificationHandler: DeadlineNotificationHandler
) {
    @SuppressLint("CheckResult")
    internal fun onReceive(deadlineId: Long) {
        deadlineRepo.observeDeadline(deadlineId)
            .firstOrError()
            .filter { deadline -> !isItTooLate(deadline) }
            .subscribe(deadlineNotificationHandler::notify, Timber::e)
    }

    private fun isItTooLate(deadline: Deadline): Boolean {
        val validPercentRange =
            deadline.percent - TOLERANCE_PERCENT..deadline.percent + TOLERANCE_PERCENT
        return deadline.notificationPercent.find { it in validPercentRange } == null
    }

    companion object {
        private const val TOLERANCE_PERCENT = 1 // %
    }
}
