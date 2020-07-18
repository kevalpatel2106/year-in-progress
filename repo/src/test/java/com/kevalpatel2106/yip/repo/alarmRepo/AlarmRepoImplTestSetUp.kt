package com.kevalpatel2106.yip.repo.alarmRepo

import android.app.AlarmManager
import android.content.Context
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.db.DeadlineDao
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import com.kevalpatel2106.yip.repo.timeProvider.TimeProvider
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Date

internal abstract class AlarmRepoImplTestSetUp {

    @Mock
    lateinit var alarmManager: AlarmManager

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var timeProvider: TimeProvider

    @Mock
    lateinit var db: YipDatabase

    @Mock
    lateinit var deadlineDao: DeadlineDao

    protected val nowDate = Date()
    protected lateinit var alarmRepo: AlarmRepoImpl
    protected val kFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(db.getDeviceDao()).thenReturn(deadlineDao)
        whenever(timeProvider.nowAsync()).thenReturn(Single.just(nowDate))

        alarmRepo = AlarmRepoImpl(alarmManager, context, timeProvider, db)
    }

    protected fun generateCustomDeadlineDto(notifications: List<Float>): DeadlineDto {
        return DeadlineDto(
            id = kFixture(),
            title = kFixture(),
            description = kFixture(),
            color = DeadlineColor.COLOR_YELLOW,
            start = Date(nowDate.time - 1_000_000), // 50% passed deadline
            end = Date(nowDate.time + 1_000_000),   // 50% remaining deadline
            type = DeadlineType.CUSTOM,
            notifications = notifications
        )
    }
}
