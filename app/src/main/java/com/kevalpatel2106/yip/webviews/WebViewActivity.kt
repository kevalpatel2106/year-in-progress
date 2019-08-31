package com.kevalpatel2106.yip.webviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.databinding.ActivityWebViewBinding
import com.kevalpatel2106.yip.di.getAppComponent
import kotlinx.android.synthetic.main.activity_web_view.webview
import kotlinx.android.synthetic.main.activity_web_view.webview_toolbar
import javax.inject.Inject

internal class WebViewActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: WebViewViewModel by lazy {
        provideViewModel(viewModelProvider, WebViewViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@WebViewActivity)
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
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                model.onPageLoaded()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                model.onPageLoadingFailed()
            }
        }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val args = intent.getParcelableExtra<WebviewActivityArgs>(ARGS)
            ?: throw IllegalStateException("Null argument.")
        model.submitLink(args.link, args.titleRes)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        private const val ARGS = "args"

        private fun launch(context: Context, args: WebviewActivityArgs) {
            context.startActivity(context.prepareLaunchIntent(WebViewActivity::class.java).apply {
                putExtra(ARGS, args)
            })
        }

        fun showPrivacyPolicy(context: Context) {
            launch(
                context, WebviewActivityArgs(
                    R.string.title_activity_privacy_policy,
                    context.getString(R.string.privacy_policy_url)
                )
            )
        }

        fun showChangelog(context: Context) {
            launch(
                context, WebviewActivityArgs(
                    R.string.title_activity_changelog,
                    context.getString(R.string.changelog_url)
                )
            )
        }

        fun showWidgetGuide(context: Context) {
            launch(
                context, WebviewActivityArgs(
                    R.string.title_activity_widget_guide,
                    context.getString(R.string.add_widget_guide_url)
                )
            )
        }
    }
}
