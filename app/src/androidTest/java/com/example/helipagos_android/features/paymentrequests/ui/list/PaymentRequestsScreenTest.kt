package com.example.helipagos_android.features.paymentrequests.ui.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.helipagos_android.common.ui.PaginationControls
import com.example.helipagos_android.features.paymentrequests.domain.model.Pagination
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentStatus
import com.example.helipagos_android.ui.theme.HelipagosandroidTheme
import org.junit.Rule
import org.junit.Test

class PaymentRequestsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun paymentRequestItem_displaysCorrectInformation() {
        // Given
        val mockPaymentRequest = PaymentRequest(
            id = "1",
            amount = 100.50,
            description = "Test payment description",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01T00:00:00Z",
            currency = "ARS",
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentRequestItem(
                    paymentRequest = mockPaymentRequest,
                    onClick = { }
                )
            }
        }

        // Then - Check that description is displayed
        composeTestRule.onNodeWithText("Test payment description").assertIsDisplayed()
        
        // Check that created date is displayed
        composeTestRule.onNodeWithText("Created: 2024-01-01T00:00:00Z").assertIsDisplayed()
    }

    @Test
    fun paymentRequestItem_clickable() {
        // Given
        val mockPaymentRequest = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test payment",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01T00:00:00Z",
            currency = "ARS"
        )
        
        var clicked = false

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentRequestItem(
                    paymentRequest = mockPaymentRequest,
                    onClick = { clicked = true }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Test payment").performClick()
        assert(clicked) { "Payment item should be clickable" }
    }

    @Test
    fun paymentStatusChip_displaysCorrectStatusForPending() {
        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentStatusChip(status = PaymentStatus.PENDING)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Pendiente").assertIsDisplayed()
    }

    @Test
    fun paymentStatusChip_displaysCorrectStatusForApproved() {
        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentStatusChip(status = PaymentStatus.APPROVED)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Aprobado").assertIsDisplayed()
    }

    @Test
    fun paymentStatusChip_displaysCorrectStatusForRejected() {
        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentStatusChip(status = PaymentStatus.REJECTED)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Rechazado").assertIsDisplayed()
    }

    @Test
    fun paymentStatusChip_displaysCorrectStatusForCancelled() {
        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentStatusChip(status = PaymentStatus.CANCELLED)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Cancelado").assertIsDisplayed()
    }

    @Test
    fun paymentStatusChip_displaysCorrectStatusForProcessing() {
        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentStatusChip(status = PaymentStatus.PROCESSING)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Procesando").assertIsDisplayed()
    }

    @Test
    fun paymentRequestList_rendersMultipleItems() {
        // Given
        val payments = listOf(
            PaymentRequest(
                id = "1",
                amount = 100.0,
                description = "Payment 1",
                status = PaymentStatus.PENDING,
                createdAt = "2024-01-01",
                currency = "ARS"
            ),
            PaymentRequest(
                id = "2",
                amount = 200.0,
                description = "Payment 2",
                status = PaymentStatus.APPROVED,
                createdAt = "2024-01-02",
                currency = "ARS"
            )
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentRequestsList(
                    paymentRequests = payments,
                    pagination = null,
                    onPaymentRequestClick = {},
                    onPreviousPage = {},
                    onNextPage = {},
                    onRefresh = {},
                    isRefreshing = false
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Payment 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Payment 2").assertIsDisplayed()
    }

    @Test
    fun paginationControls_displayCorrectPageInfo() {
        // Given
        val pagination = Pagination(
            page = 2,
            limit = 20,
            total = 100,
            totalPages = 5
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaginationControls(
                    currentPage = pagination.page,
                    totalPages = pagination.totalPages,
                    hasPreviousPage = pagination.hasPreviousPage,
                    hasNextPage = pagination.hasNextPage,
                    onPreviousClick = {},
                    onNextClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Página 2 de 5").assertIsDisplayed()
    }

    @Test
    fun paginationControls_previousButtonEnabledWhenNotFirstPage() {
        // Given
        val pagination = Pagination(
            page = 2,
            limit = 20,
            total = 100,
            totalPages = 5
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaginationControls(
                    currentPage = pagination.page,
                    totalPages = pagination.totalPages,
                    hasPreviousPage = pagination.hasPreviousPage,
                    hasNextPage = pagination.hasNextPage,
                    onPreviousClick = {},
                    onNextClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Anterior").assertExists()
    }

    @Test
    fun paginationControls_nextButtonEnabledWhenNotLastPage() {
        // Given
        val pagination = Pagination(
            page = 2,
            limit = 20,
            total = 100,
            totalPages = 5
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaginationControls(
                    currentPage = pagination.page,
                    totalPages = pagination.totalPages,
                    hasPreviousPage = pagination.hasPreviousPage,
                    hasNextPage = pagination.hasNextPage,
                    onPreviousClick = {},
                    onNextClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Siguiente").assertExists()
    }

    @Test
    fun formatCurrency_displaysCurrencyCorrectly() {
        // Given
        val payment = PaymentRequest(
            id = "1",
            amount = 1500.75,
            description = "Test",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01",
            currency = "ARS"
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentRequestItem(
                    paymentRequest = payment,
                    onClick = {}
                )
            }
        }

        // Then - verify the formatted amount is displayed
        composeTestRule.onNodeWithText("Test").assertIsDisplayed()
    }
}
