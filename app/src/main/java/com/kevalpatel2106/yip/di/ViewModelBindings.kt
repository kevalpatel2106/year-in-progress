package com.kevalpatel2106.yip.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.core.di.ViewModelFactory
import com.kevalpatel2106.yip.core.di.ViewModelKey
import com.kevalpatel2106.yip.dashboard.DashboardViewModel
import com.kevalpatel2106.yip.detail.DetailViewModel
import com.kevalpatel2106.yip.edit.EditViewProgressModel
import com.kevalpatel2106.yip.payment.PaymentViewModel
import com.kevalpatel2106.yip.settings.SettingsViewModel
import com.kevalpatel2106.yip.splash.SplashViewModel
import com.kevalpatel2106.yip.webviews.WebViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelBindings {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    internal abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun bindDetailViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditViewProgressModel::class)
    internal abstract fun bindEditViewProgressModel(viewModel: EditViewProgressModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PaymentViewModel::class)
    internal abstract fun bindPaymentViewModel(viewViewModel: PaymentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    internal abstract fun bindSettingsViewModel(viewViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModel(viewViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WebViewViewModel::class)
    internal abstract fun bindWebViewViewModel(viewViewModel: WebViewViewModel): ViewModel
}