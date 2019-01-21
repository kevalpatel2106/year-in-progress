package com.kevalpatel2106.yip.core


class TestViewModel : BaseViewModel() {

    fun clear() = onCleared()

    fun getDisposable() = compositeDisposable
}