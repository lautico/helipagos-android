package com.example.helipagos_android.features.paymentrequests.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequestDto(
    @SerialName("id_sp")
    val id: Int,
    @SerialName("codigo_barra")
    val codigoBarra: String? = null,
    @SerialName("estado_pago")
    val estadoPago: String,
    @SerialName("medio_pago")
    val medioPago: String? = null,
    @SerialName("descripcion")
    val description: String,
    @SerialName("importe_pagado")
    val importePagado: Double? = null,
    @SerialName("referencia_externa")
    val referenciaExterna: String? = null,
    @SerialName("referencia_externa_2")
    val referenciaExterna2: String? = null,
    @SerialName("fecha_creacion")
    val fechaCreacion: String,
    @SerialName("fecha_pago")
    val fechaPago: String? = null,
    @SerialName("fecha_contracargo")
    val fechaContracargo: String? = null,
    @SerialName("fecha_vencimiento")
    val fechaVencimiento: String,
    @SerialName("segunda_fecha_vencimiento")
    val segundaFechaVencimiento: String? = null
)

@Serializable
data class PageableDto(
    @SerialName("sort")
    val sort: SortDto,
    @SerialName("offset")
    val offset: Int,
    @SerialName("pageNumber")
    val pageNumber: Int,
    @SerialName("pageSize")
    val pageSize: Int,
    @SerialName("paged")
    val paged: Boolean,
    @SerialName("unpaged")
    val unpaged: Boolean
)

@Serializable
data class SortDto(
    @SerialName("sorted")
    val sorted: Boolean,
    @SerialName("unsorted")
    val unsorted: Boolean,
    @SerialName("empty")
    val empty: Boolean
)

@Serializable
data class PaymentRequestsResponseDto(
    @SerialName("content")
    val content: List<PaymentRequestDto>,
    @SerialName("pageable")
    val pageable: PageableDto,
    @SerialName("totalElements")
    val totalElements: Int,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("last")
    val last: Boolean,
    @SerialName("size")
    val size: Int,
    @SerialName("number")
    val number: Int,
    @SerialName("sort")
    val sort: SortDto,
    @SerialName("numberOfElements")
    val numberOfElements: Int,
    @SerialName("first")
    val first: Boolean,
    @SerialName("empty")
    val empty: Boolean
)

@Serializable
data class PaginationDto(
    @SerialName("page")
    val page: Int,
    @SerialName("limit")
    val limit: Int,
    @SerialName("total")
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int
)

@Serializable
data class CreatePaymentRequestDto(
    @SerialName("importe")
    val importe: Int,
    @SerialName("fecha_vto")
    val fechaVto: String,
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("recargo")
    val recargo: Double? = null,
    @SerialName("fecha_2do_vto")
    val fecha2doVto: String? = null,
    @SerialName("referencia_externa")
    val referenciaExterna: String? = null,
    @SerialName("referencia_externa_2")
    val referenciaExterna2: String? = null,
    @SerialName("url_redirect")
    val urlRedirect: String? = null,
    @SerialName("webhook")
    val webhook: String? = null,
    @SerialName("qr")
    val qr: Boolean? = null
)

@Serializable
data class CreatePaymentResponseDto(
    @SerialName("id_sp")
    val idSp: Int,
    @SerialName("id_cliente")
    val idCliente: Int,
    @SerialName("estado")
    val estado: String,
    @SerialName("referencia_externa")
    val referenciaExterna: String? = null,
    @SerialName("fecha_creacion")
    val fechaCreacion: String,
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("codigo_barra")
    val codigoBarra: String? = null,
    @SerialName("id_url")
    val idUrl: String,
    @SerialName("checkout_url")
    val checkoutUrl: String,
    @SerialName("short_url")
    val shortUrl: String,
    @SerialName("fecha_vencimiento")
    val fechaVencimiento: String,
    @SerialName("importe")
    val importe: Double,
    @SerialName("recargo")
    val recargo: Double? = null,
    @SerialName("fecha_vencimiento_2do")
    val fechaVencimiento2do: String? = null,
    @SerialName("qr_data")
    val qrData: String? = null
)

@Serializable
data class ApiErrorDto(
    @SerialName("error")
    val error: String,
    @SerialName("message")
    val message: String,
    @SerialName("code")
    val code: Int? = null
)