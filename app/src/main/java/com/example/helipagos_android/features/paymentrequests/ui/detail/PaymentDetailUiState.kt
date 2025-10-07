package com.example.helipagos_android.features.paymentrequests.ui.detail

import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest

data class PaymentDetailUiState(
    val isLoading: Boolean = false,
    val paymentRequest: PaymentRequest? = null,
    val error: String? = null
)