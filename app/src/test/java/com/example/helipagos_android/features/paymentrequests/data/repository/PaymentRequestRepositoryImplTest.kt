package com.example.helipagos_android.features.paymentrequests.data.repository

import app.cash.turbine.test
import com.example.helipagos_android.features.paymentrequests.data.api.PaymentApi
import com.example.helipagos_android.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.example.helipagos_android.features.paymentrequests.data.model.CreatePaymentResponseDto
import com.example.helipagos_android.features.paymentrequests.data.model.PageableDto
import com.example.helipagos_android.features.paymentrequests.data.model.PaymentRequestDto
import com.example.helipagos_android.features.paymentrequests.data.model.PaymentRequestsResponseDto
import com.example.helipagos_android.features.paymentrequests.data.model.SortDto
import com.example.helipagos_android.features.paymentrequests.domain.model.CreatePaymentRequest
import com.example.helipagos_android.utils.NetworkException
import com.example.helipagos_android.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaymentRequestRepositoryImplTest {

    private val api = mockk<PaymentApi>()
    private lateinit var repository: PaymentRequestRepositoryImpl

    @Before
    fun setUp() {
        repository = PaymentRequestRepositoryImpl(api)
    }

    @Test
    fun `getPaymentRequests should emit loading then success`() = runTest {
        // Given
        val mockResponse = createMockPaymentRequestsResponse()
        coEvery { api.getPaymentRequests(0, 20) } returns Response.success(mockResponse)

        // When & Then
        repository.getPaymentRequests(1, 20).test {
            // First emission should be Loading
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            // Second emission should be Success
            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertEquals(1, success.data.data.size)
            assertEquals(1, success.data.pagination.page)
            assertEquals(20, success.data.pagination.limit)

            awaitComplete()
        }
        
        coVerify { api.getPaymentRequests(0, 20) }
    }

    @Test
    fun `getPaymentRequests should emit error on API failure`() = runTest {
        // Given
        coEvery { api.getPaymentRequests(0, 20) } returns Response.error(
            404,
            "Not found".toResponseBody()
        )

        // When & Then
        repository.getPaymentRequests(1, 20).test {
            // First emission should be Loading
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            // Second emission should be Error
            val error = awaitItem()
            assertTrue(error is Result.Error)
            assertTrue(error.exception is NetworkException)

            awaitComplete()
        }
    }

    @Test
    fun `getPaymentRequests should handle network exception`() = runTest {
        // Given
        coEvery { api.getPaymentRequests(0, 20) } throws Exception("Network error")

        // When & Then
        repository.getPaymentRequests(1, 20).test {
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            val error = awaitItem()
            assertTrue(error is Result.Error)

            awaitComplete()
        }
    }

    @Test
    fun `getPaymentRequestById should emit loading then success`() = runTest {
        // Given
        val mockResponse = createMockPaymentRequestsResponse()
        coEvery { api.getPaymentRequestById("1", 0, 1) } returns Response.success(mockResponse)

        // When & Then
        repository.getPaymentRequestById("1").test {
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertEquals("1", success.data.id)
            assertEquals(100.0, success.data.amount)

            awaitComplete()
        }
        
        coVerify { api.getPaymentRequestById("1", 0, 1) }
    }

    @Test
    fun `getPaymentRequestById should emit error when not found`() = runTest {
        // Given
        val emptyResponse = createEmptyPaymentRequestsResponse()
        coEvery { api.getPaymentRequestById("999", 0, 1) } returns Response.success(emptyResponse)

        // When & Then
        repository.getPaymentRequestById("999").test {
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            val error = awaitItem()
            assertTrue(error is Result.Error)
            assertTrue(error.exception.message?.contains("not found") == true)

            awaitComplete()
        }
    }

    @Test
    fun `createPaymentRequest should emit loading then success`() = runTest {
        // Given
        val request = CreatePaymentRequest(
            amount = 100,
            description = "Test payment",
            dueDate = "2025-12-31",
            secondDueDate = "2026-01-15",
            externalReference = "REF001",
            externalReference2 = "REF002",
            urlRedirect = "https://example.com/redirect"
        )
        
        val mockResponse = createMockCreatePaymentResponse()
        coEvery { api.createPaymentRequest(any()) } returns Response.success(mockResponse)

        // When & Then
        repository.createPaymentRequest(request).test {
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertEquals("1", success.data.id)
            assertEquals(100.0, success.data.amount)
            assertEquals("Test payment", success.data.description)

            awaitComplete()
        }
        
        coVerify { api.createPaymentRequest(any()) }
    }

    @Test
    fun `createPaymentRequest should emit error on API failure`() = runTest {
        // Given
        val request = CreatePaymentRequest(
            amount = 100,
            description = "Test payment",
            dueDate = "2025-12-31",
            secondDueDate = "2026-01-15",
            externalReference = "REF001",
            externalReference2 = "REF002",
            urlRedirect = "https://example.com/redirect"
        )
        
        coEvery { api.createPaymentRequest(any()) } returns Response.error(
            400,
            """{"error":"Bad Request","message":"Invalid data"}""".toResponseBody()
        )

        // When & Then
        repository.createPaymentRequest(request).test {
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            val error = awaitItem()
            assertTrue(error is Result.Error)
            assertTrue(error.exception is NetworkException)

            awaitComplete()
        }
    }

    // Helper functions to create mock data
    private fun createMockPaymentRequestDto() = PaymentRequestDto(
        id = 1,
        importePagado = 100.0,
        codigoBarra = "123456789",
        estadoPago = "GENERADA",
        medioPago = null,
        description = "Test payment",
        referenciaExterna = "REF001",
        referenciaExterna2 = "REF002",
        fechaCreacion = "2024-01-01T00:00:00Z",
        fechaPago = null,
        fechaContracargo = null,
        fechaVencimiento = "2025-12-31",
        segundaFechaVencimiento = "2026-01-15"
    )

    private fun createMockPaymentRequestsResponse() = PaymentRequestsResponseDto(
        content = listOf(createMockPaymentRequestDto()),
        pageable = PageableDto(
            sort = SortDto(sorted = false, unsorted = true, empty = true),
            offset = 0,
            pageNumber = 0,
            pageSize = 20,
            paged = true,
            unpaged = false
        ),
        totalElements = 1,
        totalPages = 1,
        last = true,
        size = 20,
        number = 0,
        sort = SortDto(sorted = false, unsorted = true, empty = true),
        numberOfElements = 1,
        first = true,
        empty = false
    )

    private fun createEmptyPaymentRequestsResponse() = PaymentRequestsResponseDto(
        content = emptyList(),
        pageable = PageableDto(
            sort = SortDto(sorted = false, unsorted = true, empty = true),
            offset = 0,
            pageNumber = 0,
            pageSize = 1,
            paged = true,
            unpaged = false
        ),
        totalElements = 0,
        totalPages = 0,
        last = true,
        size = 1,
        number = 0,
        sort = SortDto(sorted = false, unsorted = true, empty = true),
        numberOfElements = 0,
        first = true,
        empty = true
    )

    private fun createMockCreatePaymentResponse() = CreatePaymentResponseDto(
        idSp = 1,
        idCliente = 100,
        estado = "GENERADA",
        referenciaExterna = "REF001",
        fechaCreacion = "2024-01-01T00:00:00Z",
        descripcion = "Test payment",
        codigoBarra = "123456789",
        idUrl = "abc123",
        checkoutUrl = "https://checkout.helipagos.com/123",
        shortUrl = "https://short.url/abc",
        fechaVencimiento = "2025-12-31",
        importe = 100.0,
        recargo = null,
        fechaVencimiento2do = "2026-01-15",
        qrData = null
    )
}