package com.example.helipagos_android.features.paymentrequests.domain.usecases

import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequestsPage
import com.example.helipagos_android.features.paymentrequests.domain.repository.PaymentRequestRepository
import com.example.helipagos_android.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentRequestsUseCase @Inject constructor(
    private val repository: PaymentRequestRepository
) {
    operator fun invoke(page: Int = 1, limit: Int = 20): Flow<Result<PaymentRequestsPage>> {
        return repository.getPaymentRequests(page, limit)
    }
}