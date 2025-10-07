package com.example.helipagos_android.features.paymentrequests.domain.model

data class PaymentRequest(
    val id: String,
    val amount: Double,
    val description: String,
    val status: PaymentStatus,
    val createdAt: String,
    val updatedAt: String? = null,
    val currency: String = "USD",
    val reference: String? = null,
)

enum class PaymentStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED,
    PROCESSING;
    
    companion object {
        fun fromString(status: String): PaymentStatus {
            return when (status.uppercase()) {
                "GENERADA" -> PENDING
                "PROCESADA" -> APPROVED
                "RECHAZADA" -> REJECTED
                "PENDING" -> PENDING
                "APPROVED" -> APPROVED
                "REJECTED" -> REJECTED
                "CANCELLED" -> CANCELLED
                "PROCESSING" -> PROCESSING
                else -> PENDING
            }
        }
    }
}

data class PaymentRequestsPage(
    val data: List<PaymentRequest>,
    val pagination: Pagination
)

data class Pagination(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
) {
    val hasNextPage: Boolean
        get() = page < totalPages
    
    val hasPreviousPage: Boolean
        get() = page > 1
}

data class CreatePaymentRequest(
    val amount: Int,
    val description: String,
    val dueDate: String,
    val secondDueDate: String,
    val externalReference: String,
    val externalReference2: String,
    val urlRedirect: String
)