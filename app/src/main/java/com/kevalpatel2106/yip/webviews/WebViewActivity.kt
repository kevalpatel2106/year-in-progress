package com.kevalpatel2106.yip.webviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.ext.getLaunchIntent
import com.kevalpatel2106.yip.core.ext.set
import com.kevalpatel2106.yip.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_web_view.webview
import kotlinx.android.synthetic.main.activity_web_view.webview_toolbar

@AndroidEntryPoint
internal class WebViewActivity : AppCompatActivity() {

    private val args by navArgs<WebViewActivityArgs>()
    private val model: WebViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityWebViewBinding>(
            this@WebViewActivity,
            R.layout.activity_web_view
        ).apply {
            lifecycleOwner = this@WebViewActivity
            viewModel = model
        }

        setSupportActionBar(webview_toolbar)
        supportActionBar?.set()

        webview.setUp()
        webview.webViewClient = CustomWebViewClient(model)

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        model.submitLink(getString(args.content.link), getString(args.content.title))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        fun launch(context: Context, args: WebViewActivityArgs) {
            val intent = context.getLaunchIntent(WebViewActivity::class.java).apply {
                putExtras(args.toBundle())
            }
            context.startActivity(intent)
        }
    }
}
