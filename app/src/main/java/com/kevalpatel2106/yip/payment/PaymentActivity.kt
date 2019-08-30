package com.kevalpatel2106.yip.payment

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.core.set
import com.kevalpatel2106.yip.databinding.ActivityPaymentBinding
import com.kevalpatel2106.yip.di.getAppComponent
import kotlinx.android.synthetic.main.activity_payment.payment_toolbar
import javax.inject.Inject

internal class PaymentActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: PaymentViewModel by lazy {
        provideViewModel(viewModelProvider, PaymentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@PaymentActivity)
        DataBindingUtil.setContentView<ActivityPaymentBinding>(
            this@PaymentActivity,
            R.layout.activity_payment
        ).apply {
            lifecycleOwner = this@PaymentActivity
            viewModel = model
            activity = this@PaymentActivity
        }
        setSupportActionBar(payment_toolbar)
        supportActionBar?.set()

        // Monitor
        model.userMessage.nullSafeObserve(this@PaymentActivity) {
            Toast.makeText(this@PaymentActivity, it, Toast.LENGTH_SHORT).show()
        }
        model.closeSignal.nullSafeObserve(this@PaymentActivity) { onBackPressed() }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(context.prepareLaunchIntent(PaymentActivity::class.java))
        }
    }
}
