package com.example.helipagos_android.features.paymentrequests.data.model

import com.example.helipagos_android.features.paymentrequests.domain.model.CreatePaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.Pagination
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequestsPage
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentStatus

fun PaymentRequestDto.toDomain(): PaymentRequest {
    return PaymentRequest(
        id = id.toString(),
        amount = importePagado?.toDouble() ?:0.0 ,
        description = description,
        status = PaymentStatus.fromString(estadoPago),
        createdAt = fechaCreacion,
        updatedAt = fechaPago,
        currency = "ARS",
        reference = referenciaExterna)
}

fun PaymentRequestsResponseDto.toDomain(): PaymentRequestsPage {
    return PaymentRequestsPage(
        data = content.map { it.toDomain() },
        pagination = Pagination(
            page = number + 1,
            limit = size,
            total = totalElements,
            totalPages = totalPages
        )
    )
}

fun PaginationDto.toDomain(): Pagination {
    return Pagination(
        page = page,
        limit = limit,
        total = total,
        totalPages = totalPages
    )
}

fun CreatePaymentRequest.toDto(): CreatePaymentRequestDto {
    return CreatePaymentRequestDto(
        importe = amount,
        fechaVto = dueDate,
        descripcion = description,
        recargo = null,
        fecha2doVto = secondDueDate,
        referenciaExterna = externalReference,
        referenciaExterna2 = externalReference2,
        urlRedirect = urlRedirect,
        webhook = null,
        qr = null
    )
}

fun CreatePaymentResponseDto.toDomain(): PaymentRequest {
    return PaymentRequest(
        id = idSp.toString(),
        amount = importe,
        description = descripcion,
        status = PaymentStatus.fromString(estado),
        createdAt = fechaCreacion,
        updatedAt = null,
        currency = "ARS",
        reference = referenciaExterna)
}