package com.kevalpatel2106.yip.webviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.databinding.ActivityWebViewBinding
import com.kevalpatel2106.yip.di.getAppComponent
import kotlinx.android.synthetic.main.activity_web_view.*
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
        DataBindingUtil.setContentView<ActivityWebViewBinding>(this@WebViewActivity, R.layout.activity_web_view)
                .apply {
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

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                model.onPageLoadingFailed()
            }
        }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        model.submitLink(intent.getStringExtra(ARG_LINK), intent.getIntExtra(ARG_TITLE, 0))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            //Call on back press when home back button is clicked
            onBackPressed()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_LINK = "arg_link"

        private fun launch(context: Context, @StringRes title: Int, link: String) {
            context.startActivity(context.prepareLaunchIntent(WebViewActivity::class.java).apply {
                putExtra(ARG_LINK, link)
                putExtra(ARG_TITLE, title)
            })
        }

        fun showPrivacyPolicy(context: Context) {
            launch(context, R.string.title_activity_privacy_policy, context.getString(R.string.privacy_policy_url))
        }

        fun showChangelog(context: Context) {
            launch(context, R.string.title_activity_changelog, context.getString(R.string.changelog_url))
        }

        fun showWidgetGuide(context: Context) {
            launch(context, R.string.title_activity_widget_guide, context.getString(R.string.add_widget_guide_url))
        }
    }
}
