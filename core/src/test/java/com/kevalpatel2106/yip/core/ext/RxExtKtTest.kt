package com.kevalpatel2106.yip.core.ext

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class RxExtKtTest {

    private lateinit var compositeDisposable: CompositeDisposable

    @Mock
    lateinit var disposable: Disposable

    @Before
    fun before() {
        compositeDisposable = CompositeDisposable()
        MockitoAnnotations.initMocks(this@RxExtKtTest)
    }

    @Test
    fun testAddToComposite() {
        Assert.assertEquals(0, compositeDisposable.size())
        disposable.addTo(compositeDisposable)
        Assert.assertEquals(1, compositeDisposable.size())
    }
}
