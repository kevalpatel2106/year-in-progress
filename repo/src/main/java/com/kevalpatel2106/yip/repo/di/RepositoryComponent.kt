package com.kevalpatel2106.yip.repo.di

import android.app.AlarmManager
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.validator.Validator
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@EntryPoint
interface RepositoryComponent {
    fun getNightModeRepo(): NightModeRepo
    fun getAlarmManager(): AlarmManager
    fun getDatabase(): YipDatabase
    fun getSharedPrefs(): SharedPrefsProvider
    fun getBillingRepo(): BillingRepo
    fun getDeadlineRepo(): DeadlineRepo
    fun getAlarmRepo(): AlarmRepo
    fun getDateFormatter(): DateFormatter
    fun getValidator(): Validator
}
