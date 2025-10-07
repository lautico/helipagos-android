package com.example.helipagos_android.features.paymentrequests.ui.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helipagos_android.features.paymentrequests.ui.list.PaymentRequestsViewModel

@Composable
fun CreatePaymentRequestDialog(
    onDismiss: () -> Unit,
    viewModel: PaymentRequestsViewModel,
    modifier: Modifier = Modifier
) {
    val createState by viewModel.createPaymentState.collectAsStateWithLifecycle()

    LaunchedEffect(createState.isSuccess) {
        if (createState.isSuccess) {
            onDismiss()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Crear Solicitud de Pago",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = createState.amount,
                    onValueChange = viewModel::updateAmount,
                    label = { Text("Importe") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = createState.amountError != null,
                    supportingText = createState.amountError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !createState.isLoading
                )

                OutlinedTextField(
                    value = createState.description,
                    onValueChange = viewModel::updateDescription,
                    label = { Text("Descripción") },
                    minLines = 3,
                    maxLines = 5,
                    isError = createState.descriptionError != null,
                    supportingText = createState.descriptionError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !createState.isLoading
                )

                OutlinedTextField(
                    value = createState.dueDate,
                    onValueChange = viewModel::updateDueDate,
                    label = { Text("Fecha de Vencimiento (AAAA-MM-DD)") },
                    placeholder = { Text("2025-12-31") },
                    isError = createState.dueDateError != null,
                    supportingText = createState.dueDateError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !createState.isLoading
                )

                OutlinedTextField(
                    value = createState.secondDueDate,
                    onValueChange = viewModel::updateSecondDueDate,
                    label = { Text("Segunda Fecha de Vencimiento (AAAA-MM-DD)") },
                    placeholder = { Text("2025-12-31") },
                    isError = createState.secondDueDateError != null,
                    supportingText = createState.secondDueDateError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !createState.isLoading
                )

                OutlinedTextField(
                    value = createState.externalReference,
                    onValueChange = viewModel::updateExternalReference,
                    label = { Text("Referencia Externa") },
                    placeholder = { Text("TEST") },
                    isError = createState.externalReferenceError != null,
                    supportingText = createState.externalReferenceError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !createState.isLoading
                )

                OutlinedTextField(
                    value = createState.externalReference2,
                    onValueChange = viewModel::updateExternalReference2,
                    label = { Text("Referencia Externa 2") },
                    placeholder = { Text("TEST2") },
                    isError = createState.externalReference2Error != null,
                    supportingText = createState.externalReference2Error?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !createState.isLoading
                )

                OutlinedTextField(
                    value = createState.urlRedirect,
                    onValueChange = viewModel::updateUrlRedirect,
                    label = { Text("URL de Redirección") },
                    placeholder = { Text("https://www.helipagos.com") },
                    isError = createState.urlRedirectError != null,
                    supportingText = createState.urlRedirectError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !createState.isLoading
                )

                createState.error?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        enabled = !createState.isLoading
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = viewModel::createPaymentRequest,
                        enabled = createState.isFormValid,
                        modifier = Modifier.widthIn(min = 100.dp)
                    ) {
                        if (createState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Crear")
                        }
                    }
                }
            }
        }
    }
}