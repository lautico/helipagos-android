package com.example.helipagos_android.features.paymentrequests.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helipagos_android.features.paymentrequests.domain.usecases.GetPaymentRequestByIdUseCase
import com.example.helipagos_android.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PaymentDetailViewModel @Inject constructor(
    private val getPaymentRequestByIdUseCase: GetPaymentRequestByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentDetailUiState())
    val uiState: StateFlow<PaymentDetailUiState> = _uiState.asStateFlow()

    fun loadPaymentRequest(id: String) {
        getPaymentRequestByIdUseCase(id)
            .onEach { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            paymentRequest = result.data,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to load payment request"
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun retry(id: String) {
        loadPaymentRequest(id)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}