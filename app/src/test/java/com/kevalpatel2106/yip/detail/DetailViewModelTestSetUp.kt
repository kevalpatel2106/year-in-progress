package com.kevalpatel2106.yip.detail

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Date

abstract class DetailViewModelTestSetUp {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val rule1 = RxSchedulersOverrideRule()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var deadlineRepo: DeadlineRepo

    @Mock
    lateinit var appLaunchIntentProvider: AppLaunchIntentProvider

    @Mock
    lateinit var appShortcutHelper: AppShortcutHelper

    @Mock
    lateinit var dateFormatter: DateFormatter

    @Mock
    lateinit var resources: Resources

    protected val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    protected val deadlinePercentageString = kFixture<String>()
    protected val deadlineOpenErrorMessage = kFixture<String>()
    protected val deadlineDeletedMessage = kFixture<String>()
    protected val deadlineDeleteErrorMessage = kFixture<String>()
    protected val formattedTime = kFixture<String>()

    protected val deadlineObserver = BehaviorSubject.create<Deadline>()

    internal lateinit var model: DetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(resources.getDimension(anyInt())).thenReturn(2f)
        whenever(context.resources).thenReturn(resources)

        whenever(context.getString(anyInt(), anyFloat())).thenReturn(deadlinePercentageString)
        whenever(context.getString(R.string.deadline_open_error))
            .thenReturn(deadlineOpenErrorMessage)
        whenever(context.getString(R.string.deadline_delete_successful))
            .thenReturn(deadlineDeletedMessage)
        whenever(context.getString(R.string.deadline_delete_error))
            .thenReturn(deadlineDeleteErrorMessage)

        whenever(deadlineRepo.observeDeadline(anyLong()))
            .thenReturn(deadlineObserver.toFlowable(BackpressureStrategy.DROP))
        whenever(dateFormatter.format(any())).thenReturn(formattedTime)

        model = DetailViewModel(
            context,
            deadlineRepo,
            appShortcutHelper,
            appLaunchIntentProvider,
            dateFormatter
        )
    }

    protected fun generateDeadline(deadlinePercent: Float = kFixture.range(1f..100f)): Deadline {
        return Deadline(
            id = kFixture(),
            title = kFixture(),
            color = DeadlineColor.COLOR_YELLOW,
            start = Date(System.currentTimeMillis()),
            end = Date(System.currentTimeMillis() - 1),
            deadlineType = DeadlineType.YEAR_DEADLINE,
            notificationPercent = kFixture(),
            percent = deadlinePercent
        )
    }
}
