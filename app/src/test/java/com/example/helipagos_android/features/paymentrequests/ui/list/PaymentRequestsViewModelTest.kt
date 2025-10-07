package com.example.helipagos_android.features.paymentrequests.ui.list

import app.cash.turbine.test
import com.example.helipagos_android.features.paymentrequests.domain.model.Pagination
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequestsPage
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentStatus
import com.example.helipagos_android.features.paymentrequests.domain.usecases.CreatePaymentRequestUseCase
import com.example.helipagos_android.features.paymentrequests.domain.usecases.GetPaymentRequestsUseCase
import com.example.helipagos_android.utils.Result
import io.mockk.coEvery
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
class PaymentRequestsViewModelTest {

    private val getPaymentRequestsUseCase = mockk<GetPaymentRequestsUseCase>()
    private val createPaymentRequestUseCase = mockk<CreatePaymentRequestUseCase>()
    
    private lateinit var viewModel: PaymentRequestsViewModel
    
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load payment requests on init`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(1, 20) } returns flowOf(
            Result.Loading,
            Result.Success(createMockPaymentRequestsPage(page = 1))
        )
        
        // When
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.paymentRequests.size)
            assertEquals(1, state.pagination?.page)
            assertNull(state.error)
        }
        
        verify { getPaymentRequestsUseCase(1, 20) }
    }

    @Test
    fun `should show loading state when fetching payment requests`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(1, 20) } returns flowOf(Result.Loading)
        
        // When
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isLoading)
        }
    }

    @Test
    fun `should show error state on failure`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(1, 20) } returns flowOf(
            Result.Loading,
            Result.Error(Exception("Network error"))
        )
        
        // When
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.error)
            assertEquals("Network error", state.error)
        }
    }

    @Test
    fun `should load next page when pagination has next`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(1, 20) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage(page = 1, totalPages = 3))
        )
        every { getPaymentRequestsUseCase(2, 20) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage(page = 2, totalPages = 3))
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When
        viewModel.loadNextPage()
        
        // Then
        verify { getPaymentRequestsUseCase(2, 20) }
    }

    @Test
    fun `should not load next page when on last page`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(1, 20) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage(page = 3, totalPages = 3))
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When
        viewModel.loadNextPage()
        
        // Then - should only be called once (on init)
        verify(exactly = 1) { getPaymentRequestsUseCase(any(), any()) }
    }

    @Test
    fun `should load previous page when not on first page`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(2, 20) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage(page = 2, totalPages = 3))
        )
        every { getPaymentRequestsUseCase(1, 20) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage(page = 1, totalPages = 3))
        )
        
        // When
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        viewModel.loadPaymentRequests(2)
        viewModel.loadPreviousPage()
        
        // Then
        verify { getPaymentRequestsUseCase(1, 20) }
    }

    @Test
    fun `should refresh payment requests`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(1, 20) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage(page = 1))
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When
        viewModel.refreshPaymentRequests()
        
        // Then - should be called twice (init + refresh)
        verify(exactly = 2) { getPaymentRequestsUseCase(1, 20) }
    }

    @Test
    fun `should validate amount correctly`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(any(), any()) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage())
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When - valid amount
        viewModel.updateAmount("100")
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertEquals("100", state.amount)
            assertTrue(state.isAmountValid)
            assertNull(state.amountError)
        }
        
        // When - invalid amount (zero)
        viewModel.updateAmount("0")
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertEquals("0", state.amount)
            assertFalse(state.isAmountValid)
            assertNotNull(state.amountError)
        }
        
        // When - invalid amount (negative)
        viewModel.updateAmount("-50")
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertFalse(state.isAmountValid)
        }
    }

    @Test
    fun `should validate description correctly`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(any(), any()) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage())
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When - invalid description (too short)
        viewModel.updateDescription("Test")
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertEquals("Test", state.description)
            assertFalse(state.isDescriptionValid)
            assertNotNull(state.descriptionError)
        }
        
        // When - valid description
        viewModel.updateDescription("Test description")
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertEquals("Test description", state.description)
            assertTrue(state.isDescriptionValid)
            assertNull(state.descriptionError)
        }
    }

    @Test
    fun `should validate date format correctly`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(any(), any()) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage())
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When - valid date
        viewModel.updateDueDate("2025-12-31")
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertTrue(state.isDueDateValid)
            assertNull(state.dueDateError)
        }
        
        // When - invalid date format
        viewModel.updateDueDate("31/12/2025")
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertFalse(state.isDueDateValid)
            assertNotNull(state.dueDateError)
        }
    }

    @Test
    fun `should validate form is valid only when all fields are valid`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(any(), any()) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage())
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When - set all valid fields
        viewModel.updateAmount("100")
        viewModel.updateDescription("Test payment description")
        viewModel.updateDueDate("2025-12-31")
        viewModel.updateSecondDueDate("2026-01-15")
        viewModel.updateExternalReference("REF001")
        viewModel.updateExternalReference2("REF002")
        viewModel.updateUrlRedirect("https://example.com")
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertTrue(state.isFormValid)
        }
    }

    @Test
    fun `should create payment request when form is valid`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(any(), any()) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage())
        )
        
        val createdPayment = createMockPaymentRequest()
        coEvery { createPaymentRequestUseCase(any()) } returns flowOf(
            Result.Loading,
            Result.Success(createdPayment)
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When - setup valid form
        viewModel.updateAmount("100")
        viewModel.updateDescription("Test payment description")
        viewModel.updateDueDate("2025-12-31")
        viewModel.updateSecondDueDate("2026-01-15")
        viewModel.updateExternalReference("REF001")
        viewModel.updateExternalReference2("REF002")
        viewModel.updateUrlRedirect("https://example.com")
        
        viewModel.createPaymentRequest()
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isSuccess)
            assertNull(state.error)
        }
        
        verify { createPaymentRequestUseCase(any()) }
    }

    @Test
    fun `should show error when create payment fails`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(any(), any()) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage())
        )
        
        coEvery { createPaymentRequestUseCase(any()) } returns flowOf(
            Result.Loading,
            Result.Error(Exception("Failed to create payment"))
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When - setup valid form and create
        viewModel.updateAmount("100")
        viewModel.updateDescription("Test payment description")
        viewModel.updateDueDate("2025-12-31")
        viewModel.updateSecondDueDate("2026-01-15")
        viewModel.updateExternalReference("REF001")
        viewModel.updateExternalReference2("REF002")
        viewModel.updateUrlRedirect("https://example.com")
        
        viewModel.createPaymentRequest()
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isSuccess)
            assertNotNull(state.error)
            assertEquals("Failed to create payment", state.error)
        }
    }

    @Test
    fun `should reset create payment state`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(any(), any()) } returns flowOf(
            Result.Success(createMockPaymentRequestsPage())
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        viewModel.updateAmount("100")
        viewModel.updateDescription("Test")
        
        // When
        viewModel.resetCreatePaymentState()
        
        // Then
        viewModel.createPaymentState.test {
            val state = awaitItem()
            assertEquals("", state.amount)
            assertEquals("", state.description)
            assertFalse(state.isSuccess)
            assertNull(state.error)
        }
    }

    @Test
    fun `should clear error`() = runTest {
        // Given
        every { getPaymentRequestsUseCase(any(), any()) } returns flowOf(
            Result.Error(Exception("Test error"))
        )
        
        viewModel = PaymentRequestsViewModel(
            getPaymentRequestsUseCase,
            createPaymentRequestUseCase
        )
        
        // When
        viewModel.clearError()
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }

    // Helper functions
    private fun createMockPaymentRequest(
        id: String = "1",
        amount: Double = 100.0
    ) = PaymentRequest(
        id = id,
        amount = amount,
        description = "Test payment",
        status = PaymentStatus.PENDING,
        createdAt = "2024-01-01T00:00:00Z",
        currency = "ARS",
    )

    private fun createMockPaymentRequestsPage(
        page: Int = 1,
        totalPages: Int = 1
    ) = PaymentRequestsPage(
        data = listOf(
            createMockPaymentRequest(id = "1", amount = 100.0),
            createMockPaymentRequest(id = "2", amount = 200.0)
        ),
        pagination = Pagination(
            page = page,
            limit = 20,
            total = totalPages * 20,
            totalPages = totalPages
        )
    )
}