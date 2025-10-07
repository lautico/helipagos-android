package com.example.helipagos_android.features.paymentrequests.domain.usecases

import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.repository.PaymentRequestRepository
import com.example.helipagos_android.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentRequestByIdUseCase @Inject constructor(
    private val repository: PaymentRequestRepository
) {
    operator fun invoke(id: String): Flow<Result<PaymentRequest>> {
        return repository.getPaymentRequestById(id)
    }
}