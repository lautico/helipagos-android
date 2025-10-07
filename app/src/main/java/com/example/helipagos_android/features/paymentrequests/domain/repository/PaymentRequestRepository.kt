package com.example.helipagos_android.features.paymentrequests.domain.repository

import com.example.helipagos_android.features.paymentrequests.domain.model.CreatePaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequestsPage
import com.example.helipagos_android.utils.Result
import kotlinx.coroutines.flow.Flow

interface PaymentRequestRepository {
    
    fun getPaymentRequests(page: Int, limit: Int): Flow<Result<PaymentRequestsPage>>
    
    fun getPaymentRequestById(id: String): Flow<Result<PaymentRequest>>
    
    fun createPaymentRequest(request: CreatePaymentRequest): Flow<Result<PaymentRequest>>
}