package com.kevalpatel2106.yip.edit

import android.app.Application
import android.content.res.Resources
import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.repo.utils.validator.Validator
import io.reactivex.subjects.BehaviorSubject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class EditViewProgressModelTest {
    private val testString = "test message"

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Application

    @Mock
    lateinit var progressRepo: ProgressRepo

    @Mock
    lateinit var alarmRepo: AlarmRepo

    @Mock
    lateinit var billingRepo: BillingRepo

    @Mock
    lateinit var resources: Resources

    @Mock
    internal lateinit var viewStateObserver: Observer<EditViewState>

    @Mock
    internal lateinit var userMessageObserver: Observer<String>

    @Mock
    internal lateinit var validator: Validator

    @Captor
    internal lateinit var viewStateCaptor: ArgumentCaptor<EditViewState>

    @Captor
    internal lateinit var userMessageCaptor: ArgumentCaptor<String>

    private lateinit var viewModel: EditViewProgressModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(billingRepo.observeIsPurchased()).thenReturn(BehaviorSubject.create())
        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(context.getString(anyInt())).thenReturn(testString)
        Mockito.`when`(context.getString(anyInt(), anyInt())).thenReturn(testString)
        Mockito.`when`(resources.getInteger(ArgumentMatchers.anyInt())).thenReturn(20)

        viewModel = EditViewProgressModel(context, progressRepo, alarmRepo, validator, billingRepo)

        viewModel.userMessage.observeForever(userMessageObserver)
    }

    @After
    fun after() {
        viewModel.userMessage.removeObserver(userMessageObserver)
    }

    @Test
    fun checkOnNotificationRemoved_notificationInList() {
        // Prepare
        viewModel.onNotificationAdded(0.1F)
        viewModel.viewState.observeForever(viewStateObserver)

        // Remove notification value
        viewModel.onNotificationRemoved(0.1F)

        // Verify
        Mockito.verify(viewStateObserver, times(1 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertTrue(viewStateCaptor.value.notificationList.isEmpty())
        Assert.assertTrue(viewStateCaptor.value.isSomethingChanged)
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkOnNotificationRemoved_notificationNotInList() {
        // Prepare
        viewModel.onNotificationAdded(0.2F)
        viewModel.viewState.observeForever(viewStateObserver)

        // Remove notification value
        viewModel.onNotificationRemoved(0.1F)

        // Verify
        Mockito.verify(viewStateObserver, times(1 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertTrue(viewStateCaptor.value.notificationList.size == 1)
        Assert.assertTrue(viewStateCaptor.value.isSomethingChanged)
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkOnNotificationAdded_differentValue() {
        // Prepare
        viewModel.viewState.observeForever(viewStateObserver)

        // Add notification value
        viewModel.onNotificationAdded(0.1F)
        viewModel.onNotificationAdded(0.2F)

        // Verify
        Assert.assertTrue(viewModel.viewState.value?.notificationList?.size == 2)
        Mockito.verify(viewStateObserver, times(2 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertTrue(viewStateCaptor.value.notificationList.size == 2)
        Assert.assertEquals(0.1F, viewStateCaptor.value.notificationList[0])
        Assert.assertEquals(0.2F, viewStateCaptor.value.notificationList[1])
        Assert.assertTrue(viewStateCaptor.value.isSomethingChanged)
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }


    @Test
    fun checkOnNotificationAdded_sameValue() {
        // Prepare
        viewModel.viewState.observeForever(viewStateObserver)

        // Add notification value
        viewModel.onNotificationAdded(0.1F)
        viewModel.onNotificationAdded(0.1F)

        // Verify
        Assert.assertTrue(viewModel.viewState.value?.notificationList?.size == 1)
        Mockito.verify(viewStateObserver, times(2 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertTrue(viewStateCaptor.value.notificationList.size == 1)
        Assert.assertTrue(viewStateCaptor.value.isSomethingChanged)
        Assert.assertEquals(0.1F, viewStateCaptor.value.notificationList.first())
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkOnProgressTitleChanged_withSameAsInitialTitle() {
        // Prepare
        val initialTitle = "xyz"
        viewModel._viewState.value = viewModel.viewState.value?.copy(
            initialTitle = initialTitle,
            currentTitle = initialTitle
        )
        viewModel.viewState.observeForever(viewStateObserver)

        // Change title
        viewModel.onProgressTitleChanged(viewModel.viewState.value!!.initialTitle)

        // Verify
        Mockito.verify(viewStateObserver, times(INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertNull(viewStateCaptor.value.titleErrorMsg)
        Assert.assertFalse(viewStateCaptor.value.isSomethingChanged)
        Assert.assertFalse(viewStateCaptor.value.isTitleChanged())
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkOnProgressTitleChanged_withInitialTitleBlankSpace() {
        // Prepare
        val initialTitle = "xyz"
        viewModel._viewState.value = viewModel.viewState.value?.copy(
            initialTitle = initialTitle,
            currentTitle = initialTitle
        )
        viewModel.viewState.observeForever(viewStateObserver)

        // Change title
        viewModel.onProgressTitleChanged("${viewModel.viewState.value!!.initialTitle}   ")

        // Verify
        Mockito.verify(viewStateObserver, times(INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertNull(viewStateCaptor.value.titleErrorMsg)
        Assert.assertFalse(viewStateCaptor.value.isSomethingChanged)
        Assert.assertFalse(viewStateCaptor.value.isTitleChanged())
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkOnProgressTitleChanged_withInvalidTitle() {
        // Prepare
        Mockito.`when`(validator.isValidTitle(ArgumentMatchers.anyString())).thenReturn(false)
        val longTitle = "123456789012345678901"
        viewModel.viewState.observeForever(viewStateObserver)

        // Change title
        viewModel.onProgressTitleChanged(longTitle)

        // Verify
        Mockito.verify(viewStateObserver, times(1 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertEquals(testString, viewStateCaptor.value.titleErrorMsg)
        Assert.assertFalse(viewStateCaptor.value.isSomethingChanged)
        Assert.assertFalse(viewStateCaptor.value.isTitleChanged())
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkOnProgressTitleChanged_withValidTitle() {
        // Prepare
        Mockito.`when`(validator.isValidTitle(ArgumentMatchers.anyString())).thenReturn(true)
        val longTitle = "12345678901234567890"
        viewModel.viewState.observeForever(viewStateObserver)

        // Change title
        viewModel.onProgressTitleChanged(longTitle)

        // Verify
        Mockito.verify(viewStateObserver, times(1 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertNull(viewStateCaptor.value.titleErrorMsg)
        Assert.assertTrue(viewStateCaptor.value.isSomethingChanged)
        Assert.assertTrue(viewStateCaptor.value.isTitleChanged())
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkOnProgressColorSelected_withInvalidColor() {
        // Prepare
        Mockito.`when`(validator.isValidProgressColor(ArgumentMatchers.anyInt())).thenReturn(false)
        val initialColor = viewModel.viewState.value!!.progressColor.colorInt
        viewModel.viewState.observeForever(viewStateObserver)

        // Change color
        val color = Color.WHITE
        viewModel.onProgressColorSelected(color)

        // Verify
        Mockito.verify(viewStateObserver, times(1 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertEquals(initialColor, viewStateCaptor.value.progressColor.colorInt)
        Assert.assertFalse(viewStateCaptor.value.isSomethingChanged)
        Mockito.verify(userMessageObserver, times(1))
            .onChanged(userMessageCaptor.capture())
        Assert.assertEquals(testString, userMessageCaptor.value)

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkOnProgressColorSelected_withValidColor() {
        // Prepare
        Mockito.`when`(validator.isValidProgressColor(ArgumentMatchers.anyInt())).thenReturn(true)
        viewModel.viewState.observeForever(viewStateObserver)

        // Change color
        val color = ProgressColor.COLOR_BLUE.colorInt
        viewModel.onProgressColorSelected(color)

        // Verify
        Mockito.verify(viewStateObserver, times(1 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Assert.assertEquals(color, viewStateCaptor.value.progressColor.colorInt)
        Assert.assertTrue(viewStateCaptor.value.isSomethingChanged)
        Mockito.verify(userMessageObserver, never()).onChanged(userMessageCaptor.capture())

        // Clean up
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    companion object {
        private const val INITIAL_STATE_ON_CHANGE = 1
    }
}
