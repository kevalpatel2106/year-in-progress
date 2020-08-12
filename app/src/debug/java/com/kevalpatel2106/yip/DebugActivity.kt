package com.kevalpatel2106.yip

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.kevalpatel2106.yip.core.ext.getLaunchIntent
import io.palaima.debugdrawer.commons.BuildModule
import io.palaima.debugdrawer.commons.DeviceModule
import io.palaima.debugdrawer.commons.NetworkModule
import io.palaima.debugdrawer.commons.SettingsModule
import io.palaima.debugdrawer.timber.TimberModule
import kotlinx.android.synthetic.debug.activity_debug.debug_view


class DebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        supportActionBar?.apply {
            title = "Debug screen"
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        debug_view.modules(
            BuildModule(),
            DeviceModule(),
            SettingsModule(),
            NetworkModule(),
            TimberModule()
        )
    }

    override fun onResume() {
        super.onResume()
        debug_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        debug_view.onPause()
    }

    override fun onStart() {
        super.onStart()
        debug_view.onStart()
    }

    override fun onStop() {
        super.onStop()
        debug_view.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {

        internal fun launch(context: Context) {
            context.startActivity(context.getLaunchIntent(DebugActivity::class.java))
        }
    }
}
