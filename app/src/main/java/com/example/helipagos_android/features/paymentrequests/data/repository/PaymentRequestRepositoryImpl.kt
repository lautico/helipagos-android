package com.example.helipagos_android.features.paymentrequests.data.repository

import com.example.helipagos_android.features.paymentrequests.data.api.PaymentApi
import com.example.helipagos_android.features.paymentrequests.data.model.toDomain
import com.example.helipagos_android.features.paymentrequests.data.model.toDto
import com.example.helipagos_android.features.paymentrequests.domain.model.CreatePaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequestsPage
import com.example.helipagos_android.features.paymentrequests.domain.repository.PaymentRequestRepository
import com.example.helipagos_android.utils.NetworkException
import com.example.helipagos_android.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRequestRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRequestRepository {

    override fun getPaymentRequests(page: Int, limit: Int): Flow<Result<PaymentRequestsPage>> = flow {
        emit(Result.Loading)
        try {
            val apiPage = if (page > 0) page - 1 else 0
            val response = api.getPaymentRequests(apiPage, limit)
            emit(response.toResult { it.toDomain() })
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getPaymentRequestById(id: String): Flow<Result<PaymentRequest>> = flow {
        emit(Result.Loading)
        try {
            val response = api.getPaymentRequestById(id)
            emit(response.toResult { responseDto ->
                val paymentRequest = responseDto.content.firstOrNull()
                    ?: throw NetworkException("Payment request not found")
                paymentRequest.toDomain()
            })
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun createPaymentRequest(request: CreatePaymentRequest): Flow<Result<PaymentRequest>> = flow {
        emit(Result.Loading)
        try {
            val response = api.createPaymentRequest(request.toDto())
            emit(response.toResult { it.toDomain() })
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    private fun <T, R> Response<T>.toResult(mapper: (T) -> R): Result<R> {
        return if (isSuccessful) {
            val body = body()
            if (body != null) {
                Result.Success(mapper(body))
            } else {
                Result.Error(NetworkException("Empty response body"))
            }
        } else {
            val errorMessage = errorBody()?.string() ?: "Unknown error"
            Result.Error(NetworkException(errorMessage, code()))
        }
    }
}