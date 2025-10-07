package com.example.helipagos_android.features.paymentrequests.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentStatus
import com.example.helipagos_android.ui.theme.HelipagosandroidTheme
import org.junit.Rule
import org.junit.Test

class PaymentDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun paymentDetailContent_displaysAllFields() {
        // Given
        val mockPayment = PaymentRequest(
            id = "123",
            amount = 250.50,
            description = "Test payment description",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01T10:00:00Z",
            updatedAt = "2024-01-02T15:30:00Z",
            currency = "ARS",
            reference = "REF-001",
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then - verify all fields are displayed
        composeTestRule.onNodeWithText("ID").assertIsDisplayed()
        composeTestRule.onNodeWithText("123").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Descripción").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test payment description").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Moneda").assertIsDisplayed()
        composeTestRule.onNodeWithText("ARS").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Referencia").assertIsDisplayed()
        composeTestRule.onNodeWithText("REF-001").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Creado").assertIsDisplayed()
        composeTestRule.onNodeWithText("2024-01-01T10:00:00Z").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Actualizado").assertIsDisplayed()
        composeTestRule.onNodeWithText("2024-01-02T15:30:00Z").assertIsDisplayed()
    }

    @Test
    fun paymentDetailContent_displaysAmount() {
        // Given
        val mockPayment = PaymentRequest(
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
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Importe").assertIsDisplayed()
    }

    @Test
    fun paymentDetailContent_displaysStatusChip() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test",
            status = PaymentStatus.APPROVED,
            createdAt = "2024-01-01",
            currency = "ARS"
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Aprobado").assertIsDisplayed()
    }

    @Test
    fun checkoutButton_showsWhenUrlIsPresent() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test payment",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01",
            currency = "ARS",
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Continuar checkout").assertIsDisplayed()
    }

    @Test
    fun checkoutButton_hidesWhenUrlIsNull() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test payment",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01",
            currency = "ARS",
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Continuar checkout").assertDoesNotExist()
    }

    @Test
    fun checkoutButton_isClickable() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test payment",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01",
            currency = "ARS",
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then - verify button can be clicked
        composeTestRule.onNodeWithText("Continuar checkout").assertIsDisplayed()
        composeTestRule.onNodeWithText("Continuar checkout").performClick()
    }

    @Test
    fun paymentStatusChip_showsPendingStatus() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01",
            currency = "ARS"
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Pendiente").assertIsDisplayed()
    }

    @Test
    fun paymentStatusChip_showsApprovedStatus() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test",
            status = PaymentStatus.APPROVED,
            createdAt = "2024-01-01",
            currency = "ARS"
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Aprobado").assertIsDisplayed()
    }

    @Test
    fun paymentStatusChip_showsRejectedStatus() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test",
            status = PaymentStatus.REJECTED,
            createdAt = "2024-01-01",
            currency = "ARS"
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Rechazado").assertIsDisplayed()
    }

    @Test
    fun paymentStatusChip_showsCancelledStatus() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test",
            status = PaymentStatus.CANCELLED,
            createdAt = "2024-01-01",
            currency = "ARS"
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Cancelado").assertIsDisplayed()
    }

    @Test
    fun paymentStatusChip_showsProcessingStatus() {
        // Given
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test",
            status = PaymentStatus.PROCESSING,
            createdAt = "2024-01-01",
            currency = "ARS"
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Procesando").assertIsDisplayed()
    }

    @Test
    fun paymentDetail_hidesOptionalFieldsWhenNull() {
        // Given - payment without optional fields
        val mockPayment = PaymentRequest(
            id = "1",
            amount = 100.0,
            description = "Test payment",
            status = PaymentStatus.PENDING,
            createdAt = "2024-01-01",
            updatedAt = null,
            currency = "ARS",
            reference = null,
        )

        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                PaymentDetailContent(paymentRequest = mockPayment)
            }
        }

        // Then - optional fields should not be displayed
        composeTestRule.onNodeWithText("Actualizado").assertDoesNotExist()
        composeTestRule.onNodeWithText("Referencia").assertDoesNotExist()
        composeTestRule.onNodeWithText("Continuar checkout").assertDoesNotExist()
    }

    @Test
    fun detailRow_displaysLabelAndValue() {
        // When
        composeTestRule.setContent {
            HelipagosandroidTheme {
                DetailRow(
                    label = "Test Label",
                    value = "Test Value"
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Test Label").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Value").assertIsDisplayed()
    }
}
