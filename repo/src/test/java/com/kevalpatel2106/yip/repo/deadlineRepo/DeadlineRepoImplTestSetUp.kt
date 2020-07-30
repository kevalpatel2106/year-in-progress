package com.kevalpatel2106.yip.repo.deadlineRepo

import android.content.Context
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.db.DeadlineDao
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.timeProvider.TimeProvider
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Date

internal abstract class DeadlineRepoImplTestSetUp {

    @JvmField
    @Rule
    val rule1 = RxSchedulersOverrideRule()

    @Mock
    internal lateinit var context: Context

    @Mock
    internal lateinit var db: YipDatabase

    @Mock
    internal lateinit var deadlineDao: DeadlineDao

    @Mock
    internal lateinit var timeProvider: TimeProvider

    @Mock
    internal lateinit var sharedPrefsProvider: SharedPrefsProvider

    protected lateinit var deadlineRepo: DeadlineRepoImpl
    protected val kFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    protected val sortOrderAtoZ = kFixture<String>()
    protected val sortOrderZtoA = kFixture<String>()
    protected val sortOrderEndingTimeAsc = kFixture<String>()
    protected val sortOrderEndingTimeDesc = kFixture<String>()

    protected val deadlineSubject = BehaviorSubject.create<DeadlineDto>()
    protected val deadlineListSubject = BehaviorSubject.create<List<DeadlineDto>>()
    protected val sortOrderPrefSubject = BehaviorSubject.create<String>()
    protected val minuteObserverSubject = BehaviorSubject.create<Date>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(context.getString(R.string.pref_key_order)).thenReturn(kFixture())
        whenever(context.getString(R.string.order_title_a_to_z)).thenReturn(sortOrderAtoZ)
        whenever(context.getString(R.string.order_title_z_to_a)).thenReturn(sortOrderZtoA)
        whenever(context.getString(R.string.order_end_time_ascending))
            .thenReturn(sortOrderEndingTimeAsc)
        whenever(context.getString(R.string.order_end_time_descending))
            .thenReturn(sortOrderEndingTimeDesc)

        whenever(deadlineDao.observeAll())
            .thenReturn(deadlineListSubject.toFlowable(BackpressureStrategy.DROP))
        whenever(deadlineDao.observe(anyLong()))
            .thenReturn(deadlineSubject.toFlowable(BackpressureStrategy.DROP))
        whenever(db.getDeviceDao()).thenReturn(deadlineDao)

        whenever(sharedPrefsProvider.observeStringFromPreference(anyString(), anyString()))
            .thenReturn(sortOrderPrefSubject)

        whenever(timeProvider.minuteObserver(anyLong()))
            .thenReturn(minuteObserverSubject.toFlowable(BackpressureStrategy.DROP))

        deadlineRepo = DeadlineRepoImpl(context, db, timeProvider, sharedPrefsProvider)
    }

    protected fun getDeadlineList(): List<DeadlineDto> {
        return DeadlineType.values()
            .map { deadlineType ->
                DeadlineDto(
                    id = kFixture(),
                    title = kFixture(),
                    description = kFixture(),
                    color = DeadlineColor.COLOR_YELLOW,
                    start = Date(System.currentTimeMillis()),
                    end = Date(System.currentTimeMillis() + 1_000_000),
                    type = deadlineType,
                    notifications = kFixture()
                )
            }
            .toMutableList()
            .apply { add(generateCustomDeadlineDto()) }
    }

    protected fun generateCustomDeadlineDto(): DeadlineDto {
        return DeadlineDto(
            id = kFixture(),
            title = kFixture(),
            description = kFixture(),
            color = DeadlineColor.COLOR_YELLOW,
            start = Date(System.currentTimeMillis()),
            end = Date(System.currentTimeMillis() + 1_000_000),
            type = DeadlineType.CUSTOM,
            notifications = kFixture()
        )
    }

    protected fun randomSortOrder(): String {
        return listOf(
            sortOrderAtoZ,
            sortOrderZtoA,
            sortOrderEndingTimeAsc,
            sortOrderEndingTimeDesc
        )[kFixture.range(0..3)]
    }
}
