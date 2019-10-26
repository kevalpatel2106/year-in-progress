package com.kevalpatel2106.yip

import android.app.Application
import android.content.BroadcastReceiver
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.github.anrwatchdog.ANRWatchDog
import com.google.android.gms.ads.MobileAds
import com.kevalpatel2106.yip.core.di.CoreComponent
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import com.kevalpatel2106.yip.settings.SettingsUseCase
import dagger.Lazy
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasBroadcastReceiverInjector
import io.palaima.debugdrawer.timber.data.LumberYard
import timber.log.Timber
import javax.inject.Inject

internal class YIPApplication : Application(), HasBroadcastReceiverInjector {

    @Inject
    lateinit var broadcastReceiverInjector: Lazy<DispatchingAndroidInjector<BroadcastReceiver>>

    @Inject
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    internal val coreComponent by lazy { CoreComponent.build(this@YIPApplication) }

    override fun onCreate() {
        super.onCreate()
        getAppComponent().inject(this@YIPApplication)

        // Initialize admob
        MobileAds.initialize(this, getString(R.string.admob_app_id))

        // Debugging
        Timber.plant(LumberYard.getInstance(this).apply { cleanUp() }.tree())
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)

        // Start ANR warchdog
        ANRWatchDog(5_000).setReportMainThreadOnly().start()

        // Set up dark mode
        val darkModeSetting = SettingsUseCase.getNightModeSettings(
            this,
            sharedPrefsProvider.getStringFromPreference(getString(R.string.pref_key_dark_mode))
        )
        AppCompatDelegate.setDefaultNightMode(darkModeSetting)
    }

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> {
        return broadcastReceiverInjector.get()
    }
}
