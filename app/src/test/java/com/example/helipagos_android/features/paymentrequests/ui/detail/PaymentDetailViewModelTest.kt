package com.example.helipagos_android.features.paymentrequests.ui.detail

import app.cash.turbine.test
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentStatus
import com.example.helipagos_android.features.paymentrequests.domain.usecases.GetPaymentRequestByIdUseCase
import com.example.helipagos_android.utils.Result
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class PaymentDetailViewModelTest {

    private val getPaymentRequestByIdUseCase = mockk<GetPaymentRequestByIdUseCase>()
    
    private lateinit var viewModel: PaymentDetailViewModel
    
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PaymentDetailViewModel(getPaymentRequestByIdUseCase)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should start with empty state`() = runTest {
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNull(state.paymentRequest)
            assertNull(state.error)
        }
    }

    @Test
    fun `should load payment request successfully`() = runTest {
        // Given
        val mockPayment = createMockPaymentRequest()
        every { getPaymentRequestByIdUseCase("1") } returns flowOf(
            Result.Loading,
            Result.Success(mockPayment)
        )
        
        // When
        viewModel.loadPaymentRequest("1")
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.paymentRequest)
            assertEquals("1", state.paymentRequest?.id)
            assertEquals(100.0, state.paymentRequest?.amount)
            assertEquals("Test payment", state.paymentRequest?.description)
            assertNull(state.error)
        }
        
        verify { getPaymentRequestByIdUseCase("1") }
    }

    @Test
    fun `should show loading state when fetching payment`() = runTest {
        // Given
        every { getPaymentRequestByIdUseCase("1") } returns flowOf(Result.Loading)
        
        // When
        viewModel.loadPaymentRequest("1")
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isLoading)
            assertNull(state.paymentRequest)
            assertNull(state.error)
        }
    }

    @Test
    fun `should show error state when loading fails`() = runTest {
        // Given
        every { getPaymentRequestByIdUseCase("1") } returns flowOf(
            Result.Loading,
            Result.Error(Exception("Payment not found"))
        )
        
        // When
        viewModel.loadPaymentRequest("1")
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNull(state.paymentRequest)
            assertNotNull(state.error)
            assertEquals("Payment not found", state.error)
        }
    }

    @Test
    fun `should retry loading payment request`() = runTest {
        // Given
        val mockPayment = createMockPaymentRequest()
        every { getPaymentRequestByIdUseCase("1") } returns flowOf(
            Result.Loading,
            Result.Success(mockPayment)
        )
        
        // When
        viewModel.retry("1")
        
        // Then
        verify { getPaymentRequestByIdUseCase("1") }
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertNotNull(state.paymentRequest)
            assertNull(state.error)
        }
    }

    @Test
    fun `should clear error`() = runTest {
        // Given
        every { getPaymentRequestByIdUseCase("1") } returns flowOf(
            Result.Error(Exception("Test error"))
        )
        
        viewModel.loadPaymentRequest("1")
        
        // When
        viewModel.clearError()
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }

    @Test
    fun `should load payment with all fields populated`() = runTest {
        // Given
        val mockPayment = PaymentRequest(
            id = "123",
            amount = 250.50,
            description = "Full payment details",
            status = PaymentStatus.APPROVED,
            createdAt = "2024-01-01T10:00:00Z",
            updatedAt = "2024-01-02T15:30:00Z",
            currency = "ARS",
            reference = "REF-001",
        )
        
        every { getPaymentRequestByIdUseCase("123") } returns flowOf(
            Result.Loading,
            Result.Success(mockPayment)
        )
        
        // When
        viewModel.loadPaymentRequest("123")
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            val payment = state.paymentRequest
            
            assertNotNull(payment)
            assertEquals("123", payment.id)
            assertEquals(250.50, payment.amount)
            assertEquals("Full payment details", payment.description)
            assertEquals(PaymentStatus.APPROVED, payment.status)
            assertEquals("2024-01-01T10:00:00Z", payment.createdAt)
            assertEquals("2024-01-02T15:30:00Z", payment.updatedAt)
            assertEquals("ARS", payment.currency)
            assertEquals("REF-001", payment.reference)
        }
    }

    @Test
    fun `should handle different payment statuses`() = runTest {
        // Test PENDING status
        testPaymentWithStatus(PaymentStatus.PENDING)
        
        // Test APPROVED status
        testPaymentWithStatus(PaymentStatus.APPROVED)
        
        // Test REJECTED status
        testPaymentWithStatus(PaymentStatus.REJECTED)
        
        // Test CANCELLED status
        testPaymentWithStatus(PaymentStatus.CANCELLED)
        
        // Test PROCESSING status
        testPaymentWithStatus(PaymentStatus.PROCESSING)
    }

    private fun testPaymentWithStatus(status: PaymentStatus) = runTest {
        // Given
        val mockPayment = createMockPaymentRequest(status = status)
        every { getPaymentRequestByIdUseCase("1") } returns flowOf(
            Result.Success(mockPayment)
        )
        
        // When
        viewModel.loadPaymentRequest("1")
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(status, state.paymentRequest?.status)
        }
    }

    @Test
    fun `should handle payment without optional fields`() = runTest {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Minimal payment",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = null,
            currency = "ARS",
            reference = null,
        )
        
        every { getPaymentRequestByIdUseCase("1") } returns flowOf(
            Result.Success(mockPayment)
        )
        
        // When
        viewModel.loadPaymentRequest("1")
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            val payment = state.paymentRequest
            
            assertNotNull(payment)
            assertNull(payment.updatedAt)
            assertNull(payment.reference)
        }
    }

    // Helper functions
    private fun createMockPaymentRequest(
        id: String = "1",
        amount: Double = 100.0,
        status: PaymentStatus = PaymentStatus.PENDING
    ) = PaymentRequest(
        id = id,
        amount = amount,
        description = "Test payment",
        status = status,
        createdAt = "2024-01-01T00:00:00Z",
        updatedAt = null,
        currency = "ARS",
        reference = "REF-001",
    )
}
