package com.example.helipagos_android.features.paymentrequests.data.api

import com.example.helipagos_android.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.example.helipagos_android.features.paymentrequests.data.model.CreatePaymentResponseDto
import com.example.helipagos_android.features.paymentrequests.data.model.PaymentRequestsResponseDto
import retrofit2.Response
import retrofit2.http.*

interface PaymentApi {
    
    @GET("solicitud_pago/v1/page/solicitud_pago")
    suspend fun getPaymentRequests(
        @Query("pageNumber") page: Int = 0,
        @Query("pageSize") limit: Int = 20
    ): Response<PaymentRequestsResponseDto>
    
    @GET("solicitud_pago/v1/page/solicitud_pago")
    suspend fun getPaymentRequestById(
        @Query("id") id: String,
        @Query("pageNumber") page: Int = 0,
        @Query("pageSize") limit: Int = 1
    ): Response<PaymentRequestsResponseDto>
    
    @POST("solicitud_pago/v1/checkout/solicitud_pago")
    suspend fun createPaymentRequest(
        @Body request: CreatePaymentRequestDto
    ): Response<CreatePaymentResponseDto>
}