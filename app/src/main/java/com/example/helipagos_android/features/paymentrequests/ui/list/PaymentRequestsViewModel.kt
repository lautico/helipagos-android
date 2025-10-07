package com.example.helipagos_android.features.paymentrequests.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helipagos_android.features.paymentrequests.domain.model.CreatePaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.usecases.CreatePaymentRequestUseCase
import com.example.helipagos_android.features.paymentrequests.domain.usecases.GetPaymentRequestsUseCase
import com.example.helipagos_android.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PaymentRequestsViewModel @Inject constructor(
    private val getPaymentRequestsUseCase: GetPaymentRequestsUseCase,
    private val createPaymentRequestUseCase: CreatePaymentRequestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentRequestsUiState())
    val uiState: StateFlow<PaymentRequestsUiState> = _uiState.asStateFlow()

    private val _createPaymentState = MutableStateFlow(CreatePaymentUiState())
    val createPaymentState: StateFlow<CreatePaymentUiState> = _createPaymentState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 20

    init {
        loadPaymentRequests()
    }

    fun loadPaymentRequests(page: Int = 1) {
        currentPage = page
        getPaymentRequestsUseCase(page, pageSize)
            .onEach { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = page == 1,
                            isRefreshing = page == 1,
                            error = null
                        )
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRefreshing = false,
                            paymentRequests = result.data.data,
                            pagination = result.data.pagination,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun refreshPaymentRequests() {
        loadPaymentRequests(1)
    }

    fun loadNextPage() {
        val pagination = _uiState.value.pagination
        if (pagination != null && pagination.hasNextPage) {
            loadPaymentRequests(currentPage + 1)
        }
    }

    fun loadPreviousPage() {
        if (currentPage > 1) {
            loadPaymentRequests(currentPage - 1)
        }
    }

    fun retry() {
        loadPaymentRequests(currentPage)
    }

    fun updateAmount(amount: String) {
        _createPaymentState.value = _createPaymentState.value.copy(
            amount = amount,
            error = null
        )
    }

    fun updateDescription(description: String) {
        _createPaymentState.value = _createPaymentState.value.copy(
            description = description,
            error = null
        )
    }

    fun updateDueDate(dueDate: String) {
        _createPaymentState.value = _createPaymentState.value.copy(
            dueDate = dueDate,
            error = null
        )
    }

    fun updateSecondDueDate(secondDueDate: String) {
        _createPaymentState.value = _createPaymentState.value.copy(
            secondDueDate = secondDueDate,
            error = null
        )
    }

    fun updateExternalReference(externalReference: String) {
        _createPaymentState.value = _createPaymentState.value.copy(
            externalReference = externalReference,
            error = null
        )
    }

    fun updateExternalReference2(externalReference2: String) {
        _createPaymentState.value = _createPaymentState.value.copy(
            externalReference2 = externalReference2,
            error = null
        )
    }

    fun updateUrlRedirect(urlRedirect: String) {
        _createPaymentState.value = _createPaymentState.value.copy(
            urlRedirect = urlRedirect,
            error = null
        )
    }

    fun createPaymentRequest() {
        val state = _createPaymentState.value
        if (!state.isFormValid) return

        val request = CreatePaymentRequest(
            amount = state.amount.toInt(),
            description = state.description.trim(),
            dueDate = state.dueDate,
            secondDueDate = state.secondDueDate,
            externalReference = state.externalReference.trim(),
            externalReference2 = state.externalReference2.trim(),
            urlRedirect = state.urlRedirect.trim()
        )

        createPaymentRequestUseCase(request)
            .onEach { result ->
                when (result) {
                    is Result.Loading -> {
                        _createPaymentState.value = _createPaymentState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Result.Success -> {
                        _createPaymentState.value = _createPaymentState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            error = null
                        )
                        refreshPaymentRequests()
                    }
                    is Result.Error -> {
                        _createPaymentState.value = _createPaymentState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to create payment request"
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun resetCreatePaymentState() {
        _createPaymentState.value = CreatePaymentUiState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}