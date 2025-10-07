package com.example.helipagos_android.features.paymentrequests.domain.usecases

import com.example.helipagos_android.features.paymentrequests.domain.model.CreatePaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.repository.PaymentRequestRepository
import com.example.helipagos_android.utils.Result
import com.example.helipagos_android.utils.ValidationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreatePaymentRequestUseCase @Inject constructor(
    private val repository: PaymentRequestRepository
) {
    operator fun invoke(request: CreatePaymentRequest): Flow<Result<PaymentRequest>> = flow {
        val validationResult = validateRequest(request)
        if (validationResult != null) {
            emit(Result.Error(validationResult))
            return@flow
        }
        
        repository.createPaymentRequest(request).collect { result ->
            emit(result)
        }
    }
    
    private fun validateRequest(request: CreatePaymentRequest): ValidationException? {
        if (request.amount <= 0) {
            return ValidationException("Amount must be greater than 0")
        }
        
        if (request.description.trim().length < 5) {
            return ValidationException("Description must be at least 5 characters")
        }
        
        return null
    }
}