package com.example.helipagos_android.features.paymentrequests.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helipagos_android.common.ui.EmptyStateView
import com.example.helipagos_android.common.ui.ErrorView
import com.example.helipagos_android.common.ui.LoadingView
import com.example.helipagos_android.common.ui.PaginationControls
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentRequest
import com.example.helipagos_android.features.paymentrequests.domain.model.PaymentStatus
import java.text.NumberFormat
import com.example.helipagos_android.features.paymentrequests.ui.create.CreatePaymentRequestDialog
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentRequestsScreen(
    onPaymentRequestClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentRequestsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Solicitudes de Pago") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Solicitud de Pago")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingView(message = "Loading payment requests...")
                }
                uiState.hasError -> {
                    ErrorView(
                        error = uiState.error!!,
                        onRetryClick = viewModel::retry
                    )
                }
                uiState.showEmptyState -> {
                    EmptyStateView(
                        title = "No Payment Requests",
                        description = "Create your first payment request by tapping the + button"
                    )
                }
                else -> {
                    PaymentRequestsList(
                        paymentRequests = uiState.paymentRequests,
                        pagination = uiState.pagination,
                        onPaymentRequestClick = onPaymentRequestClick,
                        onPreviousPage = viewModel::loadPreviousPage,
                        onNextPage = viewModel::loadNextPage,
                        onRefresh = viewModel::refreshPaymentRequests,
                        isRefreshing = uiState.isRefreshing
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        CreatePaymentRequestDialog(
            onDismiss = {
                showCreateDialog = false
                viewModel.resetCreatePaymentState()
            },
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentRequestsList(
    paymentRequests: List<PaymentRequest>,
    pagination: com.example.helipagos_android.features.paymentrequests.domain.model.Pagination?,
    onPaymentRequestClick: (String) -> Unit,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(paymentRequests) { paymentRequest ->
            PaymentRequestItem(
                paymentRequest = paymentRequest,
                onClick = { onPaymentRequestClick(paymentRequest.id) }
            )
        }

        if (pagination != null && pagination.totalPages > 1) {
            item {
                PaginationControls(
                    currentPage = pagination.page,
                    totalPages = pagination.totalPages,
                    hasPreviousPage = pagination.hasPreviousPage,
                    hasNextPage = pagination.hasNextPage,
                    onPreviousClick = onPreviousPage,
                    onNextClick = onNextPage
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentRequestItem(
    paymentRequest: PaymentRequest,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatCurrency(paymentRequest.amount, paymentRequest.currency),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                PaymentStatusChip(status = paymentRequest.status)
            }

            Text(
                text = paymentRequest.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Created: ${paymentRequest.createdAt}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            paymentRequest.reference?.let { reference ->
                Text(
                    text = "Reference: $reference",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PaymentStatusChip(
    status: PaymentStatus,
    modifier: Modifier = Modifier
) {
    val (containerColor, contentColor, text) = when (status) {
        PaymentStatus.PENDING -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "Pendiente"
        )
        PaymentStatus.APPROVED -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "Aprobado"
        )
        PaymentStatus.REJECTED -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "Rechazado"
        )
        PaymentStatus.CANCELLED -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Cancelado"
        )
        PaymentStatus.PROCESSING -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "Procesando"
        )
    }

    AssistChip(
        onClick = { },
        label = { Text(text) },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor,
            labelColor = contentColor
        )
    )
}

private fun formatCurrency(amount: Double, currency: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    format.currency = Currency.getInstance(currency)
    return format.format(amount)
}