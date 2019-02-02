package com.kevalpatel2106.yip.webviews

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import kotlinx.android.synthetic.main.activity_web_view.*

internal class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web_view)

        // Parse the arguments
        if (!intent.hasExtra(ARG_LINK) || !intent.hasExtra(ARG_TITLE)) {
            throw IllegalArgumentException("The launch intent must contain title and the link url.")
        }

        setToolbar(getString(intent.getIntExtra(ARG_TITLE, 0)))

        //Setup web view for url
        webview.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                webview_flipper.displayedChild = 0
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webview_flipper.displayedChild = 1
            }
        }
        webview.settings.loadsImagesAutomatically = true
        webview.settings.javaScriptEnabled = false
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.loadUrl(intent.getStringExtra(ARG_LINK))
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(webview_toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
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
    }
}
