package com.kevalpatel2106.yip.payment

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.getLaunchIntent
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.databinding.ActivityPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_payment.payment_toolbar

@AndroidEntryPoint
internal class PaymentActivity : AppCompatActivity() {

    private val model: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityPaymentBinding>(
            this@PaymentActivity,
            R.layout.activity_payment
        ).apply {
            lifecycleOwner = this@PaymentActivity
            viewModel = model
            view = this@PaymentActivity
        }

        // Set toolbar
        setSupportActionBar(payment_toolbar)
        supportActionBar?.set()

        monitorSingleEvents()
    }

    private fun monitorSingleEvents() {
        model.singleEvent.nullSafeObserve(this@PaymentActivity) { event ->
            when (event) {
                is ShowUserMessage -> {
                    Toast.makeText(this@PaymentActivity, event.message, Toast.LENGTH_SHORT).show()
                    if (event.closeScreen) onBackPressed()
                }
            }
        }
    }

    fun purchase() = model.purchase(this)

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(context.getLaunchIntent(PaymentActivity::class.java))
        }
    }
}
