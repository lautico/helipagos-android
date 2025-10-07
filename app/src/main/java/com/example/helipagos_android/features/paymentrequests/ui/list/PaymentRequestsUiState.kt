package com.example.helipagos_android.features.paymentrequests.ui.list

import com.example.helipagos_android.features.paymentrequests.domain.model.Pagination
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest

data class PaymentRequestsUiState(
    val isLoading: Boolean = false,
    val paymentRequests: List<PaymentRequest> = emptyList(),
    val pagination: Pagination? = null,
    val error: String? = null,
    val isRefreshing: Boolean = false
) {
    val hasData: Boolean = paymentRequests.isNotEmpty()
    val hasError: Boolean = error != null
    val showEmptyState: Boolean = !isLoading && !hasData && !hasError
}

data class CreatePaymentUiState(
    val amount: String = "",
    val description: String = "",
    val dueDate: String = "",
    val secondDueDate: String = "",
    val externalReference: String = "",
    val externalReference2: String = "",
    val urlRedirect: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) {
    val isAmountValid: Boolean = amount.toDoubleOrNull()?.let { it > 0 } ?: false
    val isDescriptionValid: Boolean = description.trim().length >= 5
    val isDueDateValid: Boolean = dueDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
    val isSecondDueDateValid: Boolean = secondDueDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
    val isExternalReferenceValid: Boolean = externalReference.trim().isNotEmpty()
    val isExternalReference2Valid: Boolean = externalReference2.trim().isNotEmpty()
    val isUrlRedirectValid: Boolean = urlRedirect.trim().isNotEmpty()
    
    val isFormValid: Boolean = isAmountValid && isDescriptionValid && isDueDateValid && 
                                isSecondDueDateValid && isExternalReferenceValid && 
                                isExternalReference2Valid && isUrlRedirectValid && !isLoading
    
    val amountError: String? = if (amount.isNotEmpty() && !isAmountValid) {
        "El importe debe ser mayor a 0"
    } else null
    
    val descriptionError: String? = if (description.isNotEmpty() && !isDescriptionValid) {
        "La descripción debe tener al menos 5 caracteres"
    } else null
    
    val dueDateError: String? = if (dueDate.isNotEmpty() && !isDueDateValid) {
        "La fecha debe estar en formato AAAA-MM-DD"
    } else null
    
    val secondDueDateError: String? = if (secondDueDate.isNotEmpty() && !isSecondDueDateValid) {
        "La fecha debe estar en formato AAAA-MM-DD"
    } else null
    
    val externalReferenceError: String? = if (externalReference.isNotEmpty() && !isExternalReferenceValid) {
        "La referencia externa es requerida"
    } else null
    
    val externalReference2Error: String? = if (externalReference2.isNotEmpty() && !isExternalReference2Valid) {
        "La referencia externa 2 es requerida"
    } else null
    
    val urlRedirectError: String? = if (urlRedirect.isNotEmpty() && !isUrlRedirectValid) {
        "La URL de redirección es requerida"
    } else null
}