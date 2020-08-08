package com.kevalpatel2106.yip.repo.di

import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepoImpl
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepoImpl
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatterImpl
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepoImpl
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepoImpl
import com.kevalpatel2106.yip.repo.timeProvider.TimeProvider
import com.kevalpatel2106.yip.repo.timeProvider.TimeProviderImpl
import com.kevalpatel2106.yip.repo.validator.Validator
import com.kevalpatel2106.yip.repo.validator.ValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
internal abstract class RepositoryBindings {

    @Binds
    abstract fun bindNightModeRepo(repo: NightModeRepoImpl): NightModeRepo

    @Binds
    abstract fun bindAlarmRepo(repo: AlarmRepoImpl): AlarmRepo

    @Binds
    abstract fun bindBillingRepo(repo: BillingRepoImpl): BillingRepo

    @Binds
    abstract fun bindTimeProvider(repo: TimeProviderImpl): TimeProvider

    @Binds
    abstract fun bindDeadlineRepo(repo: DeadlineRepoImpl): DeadlineRepo

    @Binds
    abstract fun bindValidator(repo: ValidatorImpl): Validator

    @Binds
    abstract fun bindDateFormatter(repo: DateFormatterImpl): DateFormatter
}
