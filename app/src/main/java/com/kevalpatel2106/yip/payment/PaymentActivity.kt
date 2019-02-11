package com.kevalpatel2106.yip.payment

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.di.getAppComponent
import kotlinx.android.synthetic.main.activity_payment.*
import javax.inject.Inject

internal class PaymentActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var model: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        setActionbar()

        getAppComponent().inject(this@PaymentActivity)
        model = provideViewModel(viewModelProvider, PaymentViewModel::class.java)

        purchase_btn.setOnClickListener { model.purchase(this@PaymentActivity) }

        // Monitor
        model.userMessage.nullSafeObserve(this@PaymentActivity) {
            Toast.makeText(this@PaymentActivity, it, Toast.LENGTH_SHORT).show()
        }
        model.closeActivity.nullSafeObserve(this@PaymentActivity) {
            finish()
        }
        model.isPurchasing.nullSafeObserve(this@PaymentActivity) {
            purchase_btn.isEnabled = !it
        }
    }

    private fun setActionbar() {
        setSupportActionBar(payment_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.title_activity_payment)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (model.isPurchasing.value != true) super.onBackPressed()
    }

    companion object {
        fun launch(context: Context) = context.startActivity(context.prepareLaunchIntent(PaymentActivity::class.java))
    }
}