package com.kevalpatel2106.yip.core

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Base class for [ViewModel].
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * [CompositeDisposable] to hold all the disposables from Rx and repository.
     */
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @CallSuper
    override fun onCleared() {
        super.onCleared()

        //Delete all the API connections.
        compositeDisposable.dispose()
    }
}
